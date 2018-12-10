package com.ken.gcp.quiz.dao;

import static com.ken.gcp.quiz.model.QuizControl.MAX_NUM;
import static com.ken.gcp.quiz.model.QuizControl.MY_CHECK;
import static com.ken.gcp.quiz.model.QuizEntity.ANSWER;
import static com.ken.gcp.quiz.model.QuizEntity.CHOICES;
import static com.ken.gcp.quiz.model.QuizEntity.DESC;
import static com.ken.gcp.quiz.model.QuizEntity.TITLE;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.KeyFactory;
import com.ken.gcp.quiz.model.QuizControl;
import com.ken.gcp.quiz.model.QuizEntity;
import com.ken.gcp.quiz.utils.MyValueUtils;

@Service
public class QuizDao implements InitializingBean {

	public static final String KIND_QUIZ_CONTROL = "quiz_control";
	public static final String KIND_QUIZ_ENTITY = "quiz_entity";

	private Datastore datastore;
	private KeyFactory controlKeyFactory;
	private KeyFactory entityKeyFactory;

	@Override
	public void afterPropertiesSet() throws Exception {
		datastore = DatastoreOptions.getDefaultInstance().getService(); // Authorized Datastore service
		controlKeyFactory = datastore.newKeyFactory().setKind(KIND_QUIZ_CONTROL);
		entityKeyFactory = datastore.newKeyFactory().setKind(KIND_QUIZ_ENTITY);
	}

	public QuizEntity readQuiz(final String category, final Long num) {
		final QuizEntity quiz = new QuizEntity().category(category).num(num);
		final Entity entity = datastore.get(entityKeyFactory.newKey(quiz.getKey())); // Load an Entity for Key(id)

		if (entity == null) {
			return null;
		}

		quiz.title(entity.getString(TITLE)) //
				.choices(entity.getString(CHOICES)) //
				.answer(entity.getString(ANSWER));

		if (entity.contains(DESC)) {
			quiz.desc(entity.getString(DESC));
		}

		return quiz;
	}

	public Long getMaxNum(final String category) {
		return getControlFieldNum(category, MAX_NUM);
	}

	public Long getMyCheck(final String category) {
		return getControlFieldNum(category, MY_CHECK);
	}

	protected Long getControlFieldNum(final String category, final String fieldName) {
		final Entity entity = datastore.get(controlKeyFactory.newKey(category)); // Load an Entity for Key(id)

		if (entity == null) {
			return null;
		}

		return entity.getLong(fieldName);
	}

	public Entity addQuiz(final QuizEntity quiz) {
		final IncompleteKey key = entityKeyFactory.newKey(quiz.getKey()); // Key will be assigned once written
		final FullEntity<IncompleteKey> entity = Entity.newBuilder(key) // Create the Entity
				.set(TITLE, MyValueUtils.noIndexString(quiz.getTitle())) //
				.set(DESC, MyValueUtils.noIndexString(quiz.getDesc())) //
				.set(CHOICES, MyValueUtils.noIndexString(quiz.getChoices())) //
				.set(ANSWER, MyValueUtils.noIndexString(quiz.getAnswer())).build();
		final Entity addedEntity = datastore.add(entity); // Save the Entity

		return addedEntity;
	}

	public static void main(final String[] args) throws Exception {
		final QuizDao quizDao = new QuizDao();
		quizDao.afterPropertiesSet();

		final IncompleteKey newKey = quizDao.controlKeyFactory.newKey("gcp-quiz-ace");

		final FullEntity<IncompleteKey> entity = Entity.newBuilder(newKey) // Create the Entity
				.set(QuizControl.MAX_NUM, 100) //
				.set(QuizControl.MY_CHECK, 100) //
				.set(QuizControl.ALLOW_COMMENT, true).build();

		final Entity addedEntity = quizDao.datastore.add(entity);

		System.out.println(addedEntity);
	}

	public void updateNextNum(final String category, final Long nextNum) {
		final Entity entity = datastore.get(controlKeyFactory.newKey(category));
		Long maxNum = getMaxNum(category);
		if (maxNum < nextNum) {
			maxNum = nextNum;
		}
		final Entity copyEntity = Entity.newBuilder(entity).set(MY_CHECK, nextNum).set(MAX_NUM, maxNum).build();
		datastore.put(copyEntity);
	}
}
