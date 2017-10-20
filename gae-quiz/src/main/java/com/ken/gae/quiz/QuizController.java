package com.ken.gae.quiz;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ken.gae.quiz.model.Quiz;

@RestController()
public class QuizController {

	@Autowired
	QuizDataService dataService;

	@RequestMapping("/aws-quiz")
	public String list() {
		Long maxNum = dataService.maxNum();
		String htmlString = "<!DOCTYPE html><html><body><p>List: ";

		if (maxNum > 0) {
			for (int i = 1; i <= maxNum; i++) {
				htmlString += "<p><a href='/aws-quiz/" + i + "'>Question" + i + "</a>";
			}
		} else {
			htmlString += "<p>No Question in the list";
		}

		htmlString += "</body><html>";
		return htmlString;
	}

	@RequestMapping(path = "/aws-quiz/add", method = RequestMethod.GET)
	public String addQuizPage() {
		String htmlString = "<!DOCTYPE html><html><body>Quiz: <p>" //
				+ "<form id='quizAdd' action='/aws-quiz/add' method='POST'>" //
				+ "<table><tr><td>Title: </td><td><textarea name='Title'form='quizAdd'></textarea></td></tr>" //
				+ "<tr><td>Choices: </td><td><textarea name='Choices' form='quizAdd'></textarea></td></tr>" //
				+ "<tr><td>Answer: </td><td><input type='text' name='Answer'></td></tr></table>" //
				+ "<input type='submit'/>" //
				+ "</form></body><html>";
		return htmlString;
	}

	@RequestMapping(path = "/aws-quiz/add", method = RequestMethod.POST)
	public void addQuiz(@RequestParam("Title") String title, //
			@RequestParam("Choices") String choices, //
			@RequestParam("Answer") String answer, //
			HttpServletResponse response) throws IOException {
		Long maxNum = dataService.maxNum();
		Quiz quiz = new Quiz().num(maxNum + 1).answer(answer).choices(choices).title(title);
		dataService.addQuiz(quiz);

		response.sendRedirect("/aws-quiz");
	}

	@RequestMapping("/aws-quiz/{id}")
	public String getQuiz(@PathVariable("id") Long id) {
		Long maxNum = dataService.maxNum();
		Quiz quiz = dataService.readQuiz(id);
		String htmlString = "<!DOCTYPE html><html><body>Quiz " + id + "  <p>";

		if (quiz != null) {
			htmlString += "<table><tr><td>Title: </td><td><pre>" + quiz.getTitle() + "</pre></td></tr>" //
					+ "<tr><td>Choices: </td><td><pre>" + quiz.getChoices() + "</pre></td></tr>" //
					+ "<tr><td>Answer: </td><td><font color='write'><pre>" + quiz.getAnswer()
					+ "</pre></font></td></tr></table><p>";
		} else {
			htmlString += "Question Not found<p>";
		}

		if (id > 1) {
			htmlString += "<a href='/aws-quiz/" + (id - 1) + "'>Previous Quiz</a>";
		}
		if (id < maxNum) {
			htmlString += "&nbsp;&nbsp;&nbsp;&nbsp;<a href='/aws-quiz/" + (id + 1) + "'>Next Quiz</a>";
		}

		htmlString += "<p><a href='/aws-quiz'>Quiz List</a></body><html>";
		return htmlString;
	}

	@RequestMapping("/aws-quiz/{id}/delete")
	public String deleteQuiz(@PathVariable("id") String id) {
		return "Delete: Quiz ID=" + id;
	}

}
