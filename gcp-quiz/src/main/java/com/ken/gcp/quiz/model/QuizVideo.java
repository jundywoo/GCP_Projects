package com.ken.gcp.quiz.model;

public class QuizVideo implements Comparable<QuizVideo> {

	public static final String CATEGORY = "category";
	public static final String DESC = "desc";
	public static final String LINK = "link";

	private String category;
	private String link;
	private String desciption;

	public QuizVideo category(final String category) {
		this.category = category;
		return this;
	}

	public QuizVideo link(final String link) {
		this.link = link;
		return this;
	}

	public QuizVideo desciption(final String desc) {
		this.desciption = desc;
		return this;
	}

	public String getCategory() {
		return category;
	}

	public String getLink() {
		return link;
	}

	public String getDesciption() {
		return desciption;
	}

	@Override
	public int compareTo(QuizVideo other) {
		return this.desciption.compareTo(other.desciption);
	}

}
