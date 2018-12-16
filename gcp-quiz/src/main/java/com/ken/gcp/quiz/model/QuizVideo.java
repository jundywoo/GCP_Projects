package com.ken.gcp.quiz.model;

public class QuizVideo implements Comparable<QuizVideo> {

	public static final String CATEGORY = "category";
	public static final String DESCRIPTION = "description";
	public static final String LINK = "link";

	private String category;
	private String link;
	private String description;

	public QuizVideo category(final String category) {
		this.category = category;
		return this;
	}

	public QuizVideo link(final String link) {
		this.link = link;
		return this;
	}

	public QuizVideo description(final String desc) {
		this.description = desc;
		return this;
	}

	public String getCategory() {
		return category;
	}

	public String getLink() {
		return link;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public int compareTo(QuizVideo other) {
		return this.description.compareTo(other.description);
	}

}
