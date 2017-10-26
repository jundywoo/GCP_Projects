package com.ken.gcp.quiz.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.ken.gcp.quiz.model.QuizComment;
import com.ken.gcp.quiz.utils.MyValueUtils;

@Service
public class QuizCommentDao implements InitializingBean {

	public static final String KIND_NAME = "quiz_comment";

	private Datastore datastore;
	private KeyFactory keyFactory;

	@Override
	public void afterPropertiesSet() throws Exception {
		datastore = DatastoreOptions.getDefaultInstance().getService(); // Authorized Datastore service
		keyFactory = datastore.newKeyFactory().setKind(KIND_NAME);
	}

	public List<QuizComment> readCommenByQuiz(Long num) {
		List<QuizComment> quizComments = new ArrayList<>();

		String gqlQuery = "select * from " + KIND_NAME + " where num = " + num;
		Query<?> query = Query.newGqlQueryBuilder(gqlQuery).setAllowLiteral(true).build();
		QueryResults<?> results = datastore.run(query);

		while (results.hasNext()) {
			Object object = results.next();
			Entity result = (Entity) object;

			QuizComment comment = new QuizComment() //
					.num(result.getLong(QuizComment.NUM)) //
					.author(result.getString(QuizComment.AUTHOR)) //
					.date(new Date(result.getTimestamp(QuizComment.DATE).toSqlTimestamp().getTime())) //
					.comment(result.getString(QuizComment.COMMENT));

			quizComments.add(comment);

			Collections.sort(quizComments);
			Collections.reverse(quizComments);
		}

		return quizComments;
	}

	public Entity addComment(QuizComment quizComment) {
		IncompleteKey key = keyFactory.newKey(); // Key will be assigned once written
		FullEntity<IncompleteKey> entity = Entity.newBuilder(key) // Create the Entity
				.set(QuizComment.NUM, quizComment.getNum()) //
				.set(QuizComment.DATE, Timestamp.now()) //
				.set(QuizComment.AUTHOR, MyValueUtils.noIndexString(quizComment.getAuthor())) //
				.set(QuizComment.COMMENT, MyValueUtils.noIndexString(quizComment.getComment())).build();
		Entity addedEntity = datastore.add(entity); // Save the Entity

		return addedEntity;
	}
}
