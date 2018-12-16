package com.ken.gcp.quiz.model;

public class QuizControl {

	public static final String TABLE_NAME_QUIZ_CONTROL = "quiz_control";

	public static final String CATEGORY = "category";
	public static final String MAX_NUM = "max_num";
	public static final String MY_CHECK = "my_check";
	public static final String ALLOW_COMMENT = "allow_comment";
	public static final String DESC = "desc";
	public static final String AVAILABLE = "available";

	private String category;
	private Long maxNum;
	private Long myCheck;
	private boolean allowComment;
	private boolean isAvailable;
	private String desciption;

	public QuizControl category(final String category) {
		this.category = category;
		return this;
	}

	public QuizControl maxNum(final long maxNum) {
		this.maxNum = maxNum;
		return this;
	}

	public QuizControl myCheck(final long myCheck) {
		this.myCheck = myCheck;
		return this;
	}

	public QuizControl allowComment(final boolean allowComment) {
		this.allowComment = allowComment;
		return this;
	}

	public QuizControl available(final boolean isAvailable) {
		this.isAvailable = isAvailable;
		return this;
	}

	public QuizControl desciption(final String desc) {
		this.desciption = desc;
		return this;
	}

	public String getCategory() {
		return category;
	}

	public Long getMaxNum() {
		return maxNum;
	}

	public Long getMyCheck() {
		return myCheck;
	}

	public boolean isAllowComment() {
		return allowComment;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public String getDesciption() {
		return desciption;
	}

}
