package com.ken.gae.quiz;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.KeyFactory;
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

	public Quiz readQuiz(Long quizId) {
		Entity entity = datastore.get(keyFactory.newKey(quizId)); // Load an Entity for Key(id)

		if (entity == null) {
			return null;
		}

		Quiz quiz = new Quiz();
		return quiz.id(quizId) //
				.title(entity.getString(Quiz.TITLE)) //
				.choices(entity.getString(Quiz.CHOICES)) //
				.answer(entity.getString(Quiz.ANSWER));
	}

	public void addQuiz(Quiz quiz) {
		IncompleteKey key = keyFactory.newKey(); // Key will be assigned once written
		FullEntity<IncompleteKey> entity = Entity.newBuilder(key) // Create the Entity
				.set(Quiz.TITLE, quiz.getTitle()) //
				.set(Quiz.CHOICES, quiz.getChoices()) //
				.set(Quiz.ANSWER, quiz.getAnswer()).build();
		Entity addedEntity = datastore.add(entity); // Save the Entity
		System.out.println("Added Quiz, key=" + addedEntity.getKey());
	}
}
