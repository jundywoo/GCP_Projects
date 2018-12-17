package com.ken.gcp.quiz.dao;

import static com.ken.gcp.quiz.model.QuizControl.DESC;
import static com.ken.gcp.quiz.model.QuizControl.MAX_NUM;
import static com.ken.gcp.quiz.model.QuizControl.MY_CHECK;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.ken.gcp.quiz.model.QuizControl;

@Service
public class QuizControlDao extends GCPSupportDao {

	public static final String KIND_QUIZ_CONTROL = "quiz_control";

	@Override
	protected String getKindName() {
		return KIND_QUIZ_CONTROL;
	}

	public Long getMaxNum(final String category) {
		return getControlFieldNum(category, MAX_NUM);
	}

	public Long getMyCheck(final String category) {
		return getControlFieldNum(category, MY_CHECK);
	}

	protected Long getControlFieldNum(final String category, final String fieldName) {
		final Entity entity = getDatastore().get(getKeyFactory().newKey(category)); // Load an Entity for Key(id)

		if (entity == null) {
			return null;
		}

		return entity.getLong(fieldName);
	}

	public void updateNextNum(final String category, final Long nextNum) {
		final Entity entity = getDatastore().get(getKeyFactory().newKey(category));
		Long maxNum = getMaxNum(category);
		if (maxNum < nextNum) {
			maxNum = nextNum;
		}
		final Entity copyEntity = Entity.newBuilder(entity).set(MY_CHECK, nextNum).set(MAX_NUM, maxNum).build();
		getDatastore().put(copyEntity);
	}

	public List<QuizControl> getAvailableControls() {
		final List<QuizControl> controls = new ArrayList<>();

		final String gqlQuery = "select * from " + KIND_QUIZ_CONTROL + " where available = true";

		final Query<?> query = Query.newGqlQueryBuilder(gqlQuery).setAllowLiteral(true).build();
		final QueryResults<?> results = getDatastore().run(query);

		while (results.hasNext()) {
			final Object object = results.next();
			final Entity result = (Entity) object;

			final QuizControl control = new QuizControl() //
					.category(result.getKey().getName()) //
					.desciption(result.getString(DESC));

			controls.add(control);
		}

		return controls;
	}
}
