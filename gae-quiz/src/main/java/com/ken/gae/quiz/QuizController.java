package com.ken.gae.quiz;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.datastore.Entity;
import com.ken.gae.quiz.dao.QuizCommentDao;
import com.ken.gae.quiz.dao.QuizDao;
import com.ken.gae.quiz.model.Quiz;
import com.ken.gae.quiz.model.QuizComment;

@RestController()
public class QuizController {

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private QuizCommentDao commentDao;

	private static final String HTML_HEADER = "<!DOCTYPE html><html><head><title>";
	private static final String HTML_HEADER2 = "</title><link rel=\"shortcut icon\" href=\"https://storage.googleapis.com/kennie-ng/quiz-icon.ico\"></head><body>";

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest httpServletRequest) {
		String htmlString = HTML_HEADER + "Kennie's server (GCP)</title>"
				+ "<link rel=\"shortcut icon\" href=\"https://storage.googleapis.com/kennie-ng/hybrid_cloud.ico\"></head><body>";

		htmlString += "<h1>Hi, Welcome to my Server</h1><p>" //
				+ "Goto <a href='/aws-quiz'>AWS Quiz - Developer Associate</a>" //
				+ "<p><h3>For the server breaking through</h3>" //
				+ "<table style='border: 0; '><tr><td>Server</td><td>&lt;See the server FQDN in the URL&gt;</td></tr>" //
				+ "<tr><td>Port</td><td>13348</td></tr>" //
				+ "<tr><td>Encrtypion</td><td>AES-256-CFB</td></tr>" //
				+ "<tr><td>Password</td><td>&lt;Contract me on Wechat, SMS etc.&gt;</td></tr></table></body><html>";
		return htmlString;
	}

	@RequestMapping("/aws-quiz")
	public String list() {
		Long maxNum = quizDao.maxNum();
		String htmlString = HTML_HEADER + "AWS Quiz" + HTML_HEADER2 + "<p>List: ";

		if (maxNum > 0) {
			htmlString += "<a href='/aws-quiz/1'>Quiz 1</a>";
			for (int i = 2; i <= maxNum; i++) {
				htmlString += "&nbsp;|&nbsp;<a href='/aws-quiz/" + i + "'>Quiz " + i + "</a>";
			}
		} else {
			htmlString += "<p>No Question in the list";
		}

		htmlString += "</body><html>";
		return htmlString;
	}

	@RequestMapping(path = "/aws-quiz/add", method = RequestMethod.GET)
	public String addQuizPage(HttpServletRequest httpServletRequest) {
		String message = httpServletRequest.getParameter("message");
		String num = httpServletRequest.getParameter("num");

		String htmlString = HTML_HEADER + "Create AWS Quiz" + HTML_HEADER2;

		if ("success".equals(message) && num != null) {
			htmlString += "<font color='green'><b>Success added record Quiz " + num + "</b></font><p>";
		}

		htmlString += "Adding Quiz: <p>" //
				+ "<form id='quizAdd' action='/aws-quiz/add' method='POST'>" //
				+ "<table><tr><td>Description: </td><td><textarea name='Desc' form='quizAdd' rows=\"10\" cols=\"200\"></textarea></td></tr>" //
				+ "<tr><td>Question: </td><td><textarea name='Title' form='quizAdd' rows=\"5\" cols=\"200\"></textarea></td></tr>" //
				+ "<tr><td>Choices: </td><td><textarea name='Choices' form='quizAdd' rows=\"15\" cols=\"200\"></textarea></td></tr>" //
				+ "<tr><td>Answer: </td><td><input type='text' name='Answer'></td></tr></table>" //
				+ "<input type='submit'/>" //
				+ "</form></body><html>";
		return htmlString;
	}

	@RequestMapping(path = "/aws-quiz/add", method = RequestMethod.POST)
	public void addQuiz(@RequestParam("Title") String title, //
			@RequestParam("Desc") String desc, //
			@RequestParam("Choices") String choices, //
			@RequestParam("Answer") String answer, //
			HttpServletResponse response) throws IOException {
		Long maxNum = quizDao.maxNum();
		Quiz quiz = new Quiz().num(maxNum + 1).answer(answer).choices(choices).title(title).desc(desc);
		Entity addQuiz = quizDao.addQuiz(quiz);

		response.sendRedirect("/aws-quiz/add?message=success&num=" + addQuiz.getLong(Quiz.NUM));
	}

	@RequestMapping(path = "/aws-quiz/{id}/comment", method = RequestMethod.POST)
	public void addQuizComment(@PathVariable("id") Long id, //
			@RequestParam("author") String author, //
			@RequestParam("comment") String comment, //
			HttpServletResponse response) throws IOException {
		QuizComment quizComment = new QuizComment().num(id).author(author).comment(comment);

		commentDao.addComment(quizComment);

		response.sendRedirect("/aws-quiz/" + id);
	}

	@RequestMapping("/aws-quiz/{id}")
	public String getQuiz(@PathVariable("id") Long id) {
		Long maxNum = quizDao.maxNum();
		Quiz quiz = quizDao.readQuiz(id);
		String htmlString = HTML_HEADER + "Quiz " + id + HTML_HEADER2 + "<a href='/aws-quiz'>Quiz List</a><p>Quiz " + id
				+ "  <p>";

		if (quiz != null) {
			htmlString += "<table border='1'>";

			String desc = quiz.getDesc();
			if (desc != null && !"".equals(desc.trim())) {
				htmlString += "<tr><td><b>Description</b></td><td><h1><pre>" + desc + "</pre></h1></td></tr>";
			}

			List<QuizComment> comments = commentDao.readCommenByQuiz(id);
			htmlString += "<tr><td><b>Question</b></td><td><h1><pre>" + quiz.getTitle() + "</pre></h1></td></tr>" //
					+ "<tr><td><b>Choices<b></td><td><h3><pre>" + quiz.getChoices() + "</pre></h3></td></tr>" //
					+ "<tr><td><b>Answer</b></td><td><h3><font id='answerBox' color='red'>"
					+ "<div id='divAnswer' style='display: none'>" + quiz.getAnswer()
					+ "</div></h3></font><button id='btnAnswer' onClick='getElementById(\"divAnswer\").style.display=\"block\"; "
					+ "getElementById(\"btnAnswer\").style.display=\"none\"; ' >Show Answer</button></td></tr>"
					+ "<tr><td><b>" + (comments.isEmpty() ? "No Comment" : "Comment (" + comments.size() + ")")
					+ "</td><td>";

			String addCommentHtml = "<button id='addComment' onclick='getElementById(\"addComment\").style.display=\"none\";"
					+ "getElementById(\"commentForm\").style.display=\"block\" ' >Add Comment</button><div id='commentForm' style='display: none'>"
					+ "<form id='commentAdd' action='/aws-quiz/" + id + "/comment' method='POST'>"
					+ "<table><tr><td>Author:</td><td><input name='author' /></td></tr>"
					+ "<tr><td>Comment:</td><td><textarea name='comment' form='commentAdd'  rows='5' cols='200'></textarea></table>"
					+ "<input type='submit'/></form></div>";

			if (comments.isEmpty()) {
				htmlString += addCommentHtml;
			} else {
				QuizComment comment = comments.get(0);
				htmlString += "<button id='btnShowComment' onClick='getElementById(\"commentBox\").style.display = \"block\"; "
						+ "getElementById(\"btnShowComment\").style.display=\"none\" ' >Show Comment</button>"
						+ "<div id='commentBox' style='display:none'>" + addCommentHtml + comment.getHtml();

				for (int i = 1, size = comments.size(); i < size; i++) {
					comment = comments.get(i);
					htmlString += "<hr />" + comment.getHtml();
				}

				htmlString += "</div>";
			}

			htmlString += "</td></tr></table><p>";
		} else {
			htmlString += "Question Not found<p>";
		}

		if (id > 1) {
			htmlString += "<a href='/aws-quiz/" + (id - 1) + "'>Previous Quiz</a>";
		}
		if (id < maxNum) {
			htmlString += "&nbsp;&nbsp;&nbsp;&nbsp;<a href='/aws-quiz/" + (id + 1) + "'>Next Quiz</a>";
		}

		htmlString += "</body><html>";
		return htmlString;
	}

}
