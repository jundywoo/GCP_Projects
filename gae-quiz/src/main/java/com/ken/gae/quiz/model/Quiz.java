package com.ken.gae.quiz.model;

public class Quiz {

	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String CHOICES = "choices";
	public static final String ANSWER = "answer";

	private Long id;

	private String title;

	private String choices;

	private String answer;

	public Quiz id(Long id) {
		this.id = id;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
