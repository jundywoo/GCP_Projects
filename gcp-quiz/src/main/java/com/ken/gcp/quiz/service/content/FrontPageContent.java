package com.ken.gcp.quiz.service.content;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.ken.gcp.quiz.dao.QuizControlDao;
import com.ken.gcp.quiz.dao.QuizVideoDao;
import com.ken.gcp.quiz.model.QuizControl;
import com.ken.gcp.quiz.model.QuizVideo;
import com.ken.gcp.quiz.service.StaticFileService;

@Component
public class FrontPageContent {

	private static final String PLACE_HOST = "\\$host\\$";
	private static final String PLACE_CATEGOIES = "\\$quiz_categories\\$";
	private static final String PLACE_GCP_VIDEO = "\\$gcp_video\\$";
	private static final String PLACE_AWS_VIDEO = "\\$aws_video\\$";

	@Value("${server.index.page:classpath:/static/index.html}")
	private Resource indexPage;

	@Autowired
	private StaticFileService staticFileService;

	@Autowired
	private QuizControlDao quizControlDao;

	@Autowired
	private QuizVideoDao quizVideoDao;

	public String getContent() {
		final String hostname = staticFileService.hostname();
		String htmlString = staticFileService.readFileToEnd(indexPage);

		if (htmlString == null) {
			htmlString = "<!DOCTYPE html><html><h1>Hi, Welcome to my Server!</h1>"
					+ "<font color='red'>Page not found</font><P>" + "<h3>This is running on GCP " + hostname
					+ " by docker container</h3><html>";
		} else {
			htmlString = htmlString.replaceAll(PLACE_HOST, hostname);
			htmlString = htmlString.replaceAll(PLACE_CATEGOIES, getCategoriesContent());
			htmlString = htmlString.replaceAll(PLACE_GCP_VIDEO, getVideosContent("GCP"));
			htmlString = htmlString.replaceAll(PLACE_AWS_VIDEO, getVideosContent("AWS"));
		}

		return htmlString;
	}

	private String getVideosContent(String category) {
		List<QuizVideo> quizVideos = quizVideoDao.getQuizVideos(category);
		StringBuilder content = new StringBuilder();

		for (QuizVideo video : quizVideos) {
			content.append("<p><a href='").append(video.getLink()).append("' target='_blank'>") //
					.append(video.getDesciption()).append("</a></p>");
		}

		return content.toString();
	}

	private String getCategoriesContent() {
		List<QuizControl> availableControls = quizControlDao.getAvailableControls();
		StringBuilder content = new StringBuilder();

		for (QuizControl control : availableControls) {
			content.append("<p>Goto <a href='/").append(control.getCategory()).append("/'>") //
					.append(control.getDesciption()).append("</a></p>");
		}

		return content.toString();
	}

}
