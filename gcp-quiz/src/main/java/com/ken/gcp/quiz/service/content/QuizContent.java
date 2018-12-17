package com.ken.gcp.quiz.service.content;

import static com.ken.gcp.quiz.service.content.ContentConstants.HTML_BODY_END;
import static com.ken.gcp.quiz.service.content.ContentConstants.HTML_HEADER;
import static com.ken.gcp.quiz.service.content.ContentConstants.HTML_HEADER2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ken.gcp.quiz.dao.QuizCommentDao;
import com.ken.gcp.quiz.dao.QuizControlDao;
import com.ken.gcp.quiz.dao.QuizEntityDao;
import com.ken.gcp.quiz.model.QuizComment;
import com.ken.gcp.quiz.model.QuizEntity;
import com.ken.gcp.quiz.service.QuizEntityService;

@Component
public class QuizContent {

	@Autowired
	private QuizEntityDao quizEntityDao;

	@Autowired
	private QuizCommentDao quizCommentDao;

	@Autowired
	private QuizControlDao quizControlDao;

	@Autowired
	private QuizEntityService quizEntityService;

	public String getQuizList(String category) {
		final Long maxNum = quizControlDao.getMaxNum(category);
		String htmlString = HTML_HEADER + "Quiz - " + category + HTML_HEADER2 + "<a href='/'>Home</a><p>Quiz List: <P>";

		if (maxNum != null && maxNum > 0) {
			htmlString += "<a href='/" + category + "/1'>Quiz 1</a>";
			for (int i = 2; i <= maxNum; i++) {
				if (i % 10 == 1) {
					htmlString += "<p>";
				} else {
					htmlString += "&nbsp;|&nbsp;";
				}
				htmlString += "<a href='/" + category + "/" + i + "'>Quiz " + i + "</a>";
			}
		} else {
			htmlString += "<p>No Question in the list";
		}

		htmlString += HTML_BODY_END;
		return htmlString;
	}

	public String getAddQuizEntity(String category, String message, String num) {
		String htmlString = HTML_HEADER + "Create Quiz - " + category + HTML_HEADER2;

		if ("success".equals(message) && num != null) {
			htmlString += "<font color='green'><b>Success added record Quiz " + num + "</b></font><p>";
		} else if (message != null && !"".equals(message.trim())) {
			htmlString += "<font color='red'><b>Failed - " + message + "</b></font><p>";
		}

		htmlString += "<a href='/" + category + "'>Back to List</a><p>Adding Quiz - " + category
				+ ": <p><form id='quizAdd' action='/" + category + "/add' method='POST'>" //
				+ "<table><tr><td>Description: </td><td><textarea name='Desc' form='quizAdd' rows=\"10\" cols=\"200\"></textarea></td></tr>" //
				+ "<tr><td>Question: </td><td><textarea name='Title' form='quizAdd' rows=\"5\" cols=\"200\"></textarea></td></tr>" //
				+ "<tr><td>Choices: </td><td><textarea name='Choices' form='quizAdd' rows=\"15\" cols=\"200\"></textarea></td></tr>" //
				+ "<tr><td>Answer: </td><td><input type='text' name='Answer'></td></tr></table>" //
				+ "<input type='submit'/>" //
				+ "</form>" + HTML_BODY_END;
		return htmlString;
	}

	public String getQuizEntityPage(String category, Long id) {
		final Long maxNum = quizControlDao.getMaxNum(category);
		final QuizEntity quiz = quizEntityDao.readQuiz(category, id);
		String htmlString = HTML_HEADER + category + " Quiz " + id + HTML_HEADER2 + "<a href='/" + category
				+ "'>Quiz List</a><p>";
		if (id > 1) {
			htmlString += "<a href='/" + category + "/" + (id - 1) + "'>Previous Quiz</a>";
		}
		if (id < maxNum) {
			htmlString += "&nbsp;&nbsp;&nbsp;&nbsp;<a href='/" + category + "/" + (id + 1) + "'>Next Quiz</a>";
		}
		htmlString += "<p>Quiz " + id + "  <p>";

		if (quiz != null) {
			htmlString += "<table border='1'>";

			final String desc = quiz.getDesc();
			if (desc != null && !"".equals(desc.trim())) {
				htmlString += "<tr><td><b>Description</b></td><td><h1><pre>" + desc + "</pre></h1></td></tr>";
			}

			final List<QuizComment> comments = quizCommentDao.readCommenByQuiz(category, id);
			htmlString += "<tr><td><b>Question</b></td><td><h1><pre>" + quiz.getTitle() + "</pre></h1></td></tr>" //
					+ "<tr><td><b>Choices<b></td><td><h3><div id='cleanAnswer'>"
					+ quizEntityService.formatAnswer(quiz.getChoices(), "") //
					+ "</div><div id='goodAnswer' style='display: none'>"
					+ quizEntityService.formatAnswer(quiz.getChoices(), quiz.getAnswer()) + "</div></h3></td></tr>" //
					+ "<tr><td><b>Answer</b></td><td><h3><font id='answerBox' color='red'>"
					+ "<div id='divAnswer' style='display: none'>" + quiz.getAnswer()
					+ "</div></h3></font><button id='btnAnswer' onClick='" //
					+ "getElementById(\"divAnswer\").style.display=\"block\"; "
					+ "getElementById(\"goodAnswer\").style.display=\"block\"; "
					+ "getElementById(\"cleanAnswer\").style.display=\"none\"; "
					+ "getElementById(\"btnAnswer\").style.display=\"none\";' >Show Answer</button></td></tr>"
					+ "<tr><td><b>" + (comments.isEmpty() ? "No Comment" : "Comment (" + comments.size() + ")")
					+ "</td><td>";

			final String addCommentHtml = "<button id='addComment' onclick='getElementById(\"addComment\").style.display=\"none\";"
					+ "getElementById(\"commentForm\").style.display=\"block\" ' >Add Comment</button><div id='commentForm' style='display: none'>"
					+ "<form id='commentAdd' action='/" + category + "/" + id + "/comment' method='POST'>"
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
			htmlString += "<a href='/" + category + "/" + (id - 1) + "'>Previous Quiz</a>";
		}
		if (id < maxNum) {
			htmlString += "&nbsp;&nbsp;&nbsp;&nbsp;<a href='/" + category + "/" + (id + 1) + "'>Next Quiz</a>";
		}

		htmlString += HTML_BODY_END;
		return htmlString;
	}

}
