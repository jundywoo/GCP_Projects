package com.ken.gcp.quiz.dao;

import static com.ken.gcp.quiz.model.QuizVideo.DESCRIPTION;
import static com.ken.gcp.quiz.model.QuizVideo.LINK;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.ken.gcp.quiz.model.QuizVideo;

@Service
public class QuizVideoDao extends GCPSupportDao {

	public static final String KIND_QUIZ_VIDEO = "quiz_video";

	@Override
	protected String getKindName() {
		return KIND_QUIZ_VIDEO;
	}

	public List<QuizVideo> getQuizVideos(String category) {
		final List<QuizVideo> videos = new ArrayList<>();

		final String gqlQuery = "select * from " + KIND_QUIZ_VIDEO + " where category = '" + category + "'";

		final Query<?> query = Query.newGqlQueryBuilder(gqlQuery).setAllowLiteral(true).build();
		final QueryResults<?> results = getDatastore().run(query);

		while (results.hasNext()) {
			final Object object = results.next();
			final Entity result = (Entity) object;

			final QuizVideo video = new QuizVideo() //
					.link(result.getString(LINK)) //
					.desciption(result.getString(DESCRIPTION));

			videos.add(video);
		}

		Collections.sort(videos);
		return videos;
	}
}
