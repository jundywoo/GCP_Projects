package com.ken.gcp.quiz.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ken.gcp.quiz.service.QuizCommentService;
import com.ken.gcp.quiz.service.QuizEntityService;
import com.ken.gcp.quiz.service.content.FrontPageContent;
import com.ken.gcp.quiz.service.content.QuizContent;

@RestController()
public class QuizController {

	@Autowired
	private FrontPageContent FrontPageContent;

	@Autowired
	private QuizContent quizContent;

	@Autowired
	private QuizEntityService quizEntityService;

	@Autowired
	private QuizCommentService quizCommentService;

	private static final Log LOG = LogFactory.getLog(QuizController.class);

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String index() {
		return FrontPageContent.getContent();
	}

	@RequestMapping("/{category}")
	public String list(@PathVariable("category") final String category) {
		return quizContent.getQuizList(category);
	}

	@RequestMapping(path = "/{category}/add", method = RequestMethod.GET)
	public String addQuizPage(@PathVariable("category") final String category, //
			final HttpServletRequest httpServletRequest) {
		final String message = httpServletRequest.getParameter("message");
		final String num = httpServletRequest.getParameter("num");

		return quizContent.getAddQuizEntity(category, message, num);
	}

	@RequestMapping(path = "/{category}/add", method = RequestMethod.POST)
	public void addQuiz(@PathVariable("category") final String category, //
			@RequestParam("Title") final String title, //
			@RequestParam("Desc") final String desc, //
			@RequestParam("Choices") final String choices, //
			@RequestParam("Answer") final String answer, //
			final HttpServletResponse response) throws IOException {
		try {
			final Long nextNum = quizEntityService.addQuizEntity(category, title, desc, choices, answer);
			response.sendRedirect("/" + category + "/add?message=success&num=" + nextNum);
		} catch (final RuntimeException e) {
			LOG.warn(e.getMessage());
			response.sendRedirect(
					"/" + category + "/add?message=" + java.net.URLEncoder.encode(e.getMessage(), "ISO-8859-1"));
		}
	}

	@RequestMapping(path = "/{category}/{id}/comment", method = RequestMethod.POST)
	public void addQuizComment(@PathVariable("category") final String category, //
			@PathVariable("id") final Long id, //
			@RequestParam("author") final String author, //
			@RequestParam("comment") final String comment, //
			final HttpServletResponse response) throws IOException {
		quizCommentService.addQuizComment(category, id, author, comment);

		response.sendRedirect("/" + category + "/" + id);
	}

	@RequestMapping("/{category}/{id}")
	public String getQuiz(@PathVariable("category") final String category, //
			@PathVariable("id") final Long id) {
		return quizContent.getQuizEntityPage(category, id);
	}

}
