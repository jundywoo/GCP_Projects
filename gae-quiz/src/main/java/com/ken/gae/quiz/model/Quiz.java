package com.ken.gae.quiz.model;

public class Quiz {

	public static final String NUM = "num";
	public static final String TITLE = "title";
	public static final String CHOICES = "choices";
	public static final String ANSWER = "answer";

	private Long num;

	private String title;

	private String choices;

	private String answer;

	public Quiz num(Long num) {
		this.num = num;
		return this;
	}

	public Quiz title(String title) {
		this.title = title;
		return this;
	}

	public Quiz choices(String choices) {
		this.choices = choices;
		return this;
	}

	public Quiz answer(String answer) {
		this.answer = answer;
		return this;
	}

	public Long getNum() {
		return num;
	}

	public void setNum(Long num) {
		this.num = num;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getChoices() {
		return choices;
	}

	public void setChoices(String choices) {
		this.choices = choices;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
