package com.ken.gcp.quiz.service;

import org.springframework.stereotype.Service;

@Service
public class QuizValidationService {

	private static final String EOL = System.getProperty("line.separator");

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

}
