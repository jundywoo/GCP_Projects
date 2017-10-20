package com.ken.gae.quiz;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.ProjectionEntity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.Value;
import com.ken.gae.quiz.model.Quiz;

@Service
public class QuizDataService implements InitializingBean {

	private Datastore datastore;
	private KeyFactory keyFactory;

	@Override
	public void afterPropertiesSet() throws Exception {
		datastore = DatastoreOptions.getDefaultInstance().getService(); // Authorized Datastore service
		keyFactory = datastore.newKeyFactory().setKind("quiz");
	}

	public Quiz readQuiz(Long num) {
		Entity entity = datastore.get(keyFactory.newKey(num)); // Load an Entity for Key(id)

		if (entity == null) {
			return null;
		}

		Quiz quiz = new Quiz() //
				.num(entity.getLong(Quiz.NUM)) //
				.title(entity.getString(Quiz.TITLE)) //
				.choices(entity.getString(Quiz.CHOICES)) //
				.answer(entity.getString(Quiz.ANSWER));

		if (entity.contains(Quiz.DESC)) {
			quiz.desc(entity.getString(Quiz.DESC));
		}

		return quiz;
	}

	public Long maxNum() {
		String gqlQuery = "select num from quiz order by num desc";
		Query<?> query = Query.newGqlQueryBuilder(gqlQuery).build();
		QueryResults<?> results = datastore.run(query);
		Long num = 0L;
		if (results.hasNext()) {
			Object next = results.next();

			ProjectionEntity result = (ProjectionEntity) next;
			Value<?> value = result.getValue("num");

			num = (Long) value.get();
		}

		return num;
	}

	public void addQuiz(Quiz quiz) {
		IncompleteKey key = keyFactory.newKey(quiz.getNum()); // Key will be assigned once written
		FullEntity<IncompleteKey> entity = Entity.newBuilder(key) // Create the Entity
				.set(Quiz.NUM, quiz.getNum()) //
				.set(Quiz.TITLE, quiz.getTitle()) //
				.set(Quiz.DESC, quiz.getDesc()) //
				.set(Quiz.CHOICES, quiz.getChoices()) //
				.set(Quiz.ANSWER, quiz.getAnswer()).build();
		datastore.add(entity); // Save the Entity
	}
}
