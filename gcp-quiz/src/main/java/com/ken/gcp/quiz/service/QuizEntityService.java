package com.ken.gcp.quiz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ken.gcp.quiz.dao.QuizControlDao;
import com.ken.gcp.quiz.dao.QuizEntityDao;
import com.ken.gcp.quiz.model.QuizEntity;

@Service
public class QuizEntityService {

	private static final String EOL = System.getProperty("line.separator");

	@Autowired
	private QuizEntityDao quizEntityDao;

	@Autowired
	private QuizControlDao quizControlDao;

	public String formatAnswer(String choices, String answer) {
		StringBuilder builder = new StringBuilder();
		boolean opened = false;
		String[] lines = choices.split(EOL);
		for (String line : lines) {
			if (line.length() > 1) {
				char first = line.charAt(0);
				char second = line.charAt(1);
				if (second == '.') {
					builder.append("<p>");
					if (answer.indexOf(first) >= 0) {
						opened = true;
						builder.append("<font color=\"red\"><b>");
					}
				}

				builder.append(line);
			} else {
				if (opened) {
					opened = false;
					builder.append("</b></font>");
				}

				builder.append("</p>");
			}
		}

		return builder.toString();
	}

	public String validate(String choices) {
		StringBuilder builder = new StringBuilder();
		String[] lines = choices.split(EOL);
		char last = 'A';
		boolean opened = false;
		for (String line : lines) {
			if (line.length() > 1) {
				char first = line.charAt(0);
				char second = line.charAt(1);

				if (opened) {
					builder.append(line);
				} else {
					if (second == '.') {
						opened = true;
						last = first;
						builder.append(line);
					} else {
						builder.append(last++).append(". ").append(line);
					}
				}
			} else {
				opened = false;
			}
			builder.append(EOL);
		}
		return builder.toString();
	}

	public Long addQuizEntity(String category, String title, String desc, String choices, String answer) {
		final Long nextNum = quizControlDao.getMyCheck(category) + 1;
		final QuizEntity quiz = new QuizEntity() //
				.category(category) //
				.num(nextNum) //
				.answer(answer) //
				.choices(validate(choices)) //
				.title(title) //
				.desc(desc);

		quizEntityDao.addQuiz(quiz);
		quizControlDao.updateNextNum(category, nextNum);

		return nextNum;
	}

}
