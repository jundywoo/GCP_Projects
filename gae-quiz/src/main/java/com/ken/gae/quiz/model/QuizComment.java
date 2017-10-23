package com.ken.gae.quiz.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class QuizComment implements Comparable<QuizComment> {

	public static final String NUM = "num";
	public static final String AUTHOR = "author";
	public static final String DATE = "date";
	public static final String COMMENT = "comment";

	private Long num;
	private String author;
	private Date date;
	private String comment;

	public Long getNum() {
		return num;
	}

	public void setNum(Long num) {
		this.num = num;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public QuizComment num(Long num) {
		this.num = num;
		return this;
	}

	public QuizComment author(String author) {
		this.author = author;
		return this;
	}

	public QuizComment comment(String comment) {
		this.comment = comment;
		return this;
	}

	public QuizComment date(Date date) {
		this.date = date;
		return this;
	}

	public String getHtml() {
		final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("Asia/Hong_Kong"));
		return "<p><i><b>" + this.getAuthor() + "</b> comments at <b>" + format.format(this.getDate())
				+ "</b></i><p><div style='width: 60%'><pre style='white-space:pre-line;'>" + this.comment
				+ "</pre></div>";
	}

	@Override
	public int compareTo(QuizComment o) {
		return this.date.compareTo(o.date);
	}

}
