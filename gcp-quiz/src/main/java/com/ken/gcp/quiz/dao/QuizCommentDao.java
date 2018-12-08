package com.ken.gcp.quiz.dao;

import static com.ken.gcp.quiz.model.QuizComment.AUTHOR;
import static com.ken.gcp.quiz.model.QuizComment.COMMENT;
import static com.ken.gcp.quiz.model.QuizComment.DATE;
import static com.ken.gcp.quiz.model.QuizComment.QUIZ_ID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.ken.gcp.quiz.model.QuizComment;
import com.ken.gcp.quiz.model.QuizEntity;
import com.ken.gcp.quiz.utils.MyValueUtils;

@Service
public class QuizCommentDao implements InitializingBean {

	public static final String KIND_QUIZ_COMMENT = "quiz_comment";

	private Datastore datastore;
	private KeyFactory keyFactory;

	@Override
	public void afterPropertiesSet() throws Exception {
		datastore = DatastoreOptions.getDefaultInstance().getService(); // Authorized Datastore service
		keyFactory = datastore.newKeyFactory().setKind(KIND_QUIZ_COMMENT);
	}

	public List<QuizComment> readCommenByQuiz(final String category, final Long num) {
		final List<QuizComment> quizComments = new ArrayList<>();

		final QuizEntity tempEntity = new QuizEntity().category(category).num(num);
		final String gqlQuery = "select * from " + KIND_QUIZ_COMMENT + " where quiz_id = '" + tempEntity.getKey() + "'";
		final Query<?> query = Query.newGqlQueryBuilder(gqlQuery).setAllowLiteral(true).build();
		final QueryResults<?> results = datastore.run(query);

		while (results.hasNext()) {
			final Object object = results.next();
			final Entity result = (Entity) object;

			final QuizComment comment = new QuizComment() //
					.quizId(result.getString(QUIZ_ID)) //
					.author(result.getString(AUTHOR)) //
					.date(new Date(result.getLong(DATE))) //
					.comment(result.getString(COMMENT));

			quizComments.add(comment);

			Collections.sort(quizComments);
			Collections.reverse(quizComments);
		}

		return quizComments;
	}

	public Entity addComment(final QuizComment quizComment) {
		final IncompleteKey key = keyFactory.newKey(); // Key will be assigned once written
		final FullEntity<IncompleteKey> entity = Entity.newBuilder(key) // Create the Entity
				.set(QUIZ_ID, quizComment.getQuizId()) //
				.set(DATE, new Date().getTime()) //
				.set(AUTHOR, MyValueUtils.noIndexString(quizComment.getAuthor())) //
				.set(COMMENT, MyValueUtils.noIndexString(quizComment.getComment())).build();
		final Entity addedEntity = datastore.add(entity); // Save the Entity

		return addedEntity;
	}
}
