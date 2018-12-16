package com.ken.gcp.quiz.dao;

import static com.ken.gcp.quiz.model.QuizEntity.ANSWER;
import static com.ken.gcp.quiz.model.QuizEntity.CHOICES;
import static com.ken.gcp.quiz.model.QuizEntity.DESC;
import static com.ken.gcp.quiz.model.QuizEntity.TITLE;

import org.springframework.stereotype.Service;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.ken.gcp.quiz.model.QuizEntity;
import com.ken.gcp.quiz.utils.MyValueUtils;

@Service
public class QuizEntityDao extends GCPSupportDao {

	public static final String KIND_QUIZ_ENTITY = "quiz_entity";

	@Override
	protected String getKindName() {
		return KIND_QUIZ_ENTITY;
	}

	public QuizEntity readQuiz(final String category, final Long num) {
		final QuizEntity quiz = new QuizEntity().category(category).num(num);
		final Entity entity = getDatastore().get(getKeyFactory().newKey(quiz.getKey())); // Load an Entity for Key(id)

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

	public Entity addQuiz(final QuizEntity quiz) {
		final IncompleteKey key = getKeyFactory().newKey(quiz.getKey()); // Key will be assigned once written
		final FullEntity<IncompleteKey> entity = Entity.newBuilder(key) // Create the Entity
				.set(TITLE, MyValueUtils.noIndexString(quiz.getTitle())) //
				.set(DESC, MyValueUtils.noIndexString(quiz.getDesc())) //
				.set(CHOICES, MyValueUtils.noIndexString(quiz.getChoices())) //
				.set(ANSWER, MyValueUtils.noIndexString(quiz.getAnswer())).build();
		final Entity addedEntity = getDatastore().add(entity); // Save the Entity

		return addedEntity;
	}

}
