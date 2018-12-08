package com.ken.gcp.quiz.model;

public class QuizControl {

	public static final String TABLE_NAME_QUIZ_CONTROL = "quiz_control";

	public static final String CONTROL_FIELD = "control_field";
	public static final String MAX_NUM = "max_num";
	public static final String MY_CHECK = "my_check";
	public static final String ALLOW_COMMENT = "allow_comment";

	private String controlField;
	private Long maxNum;
	private Long myCheck;
	private boolean allowComment;

	public QuizControl controlField(final String controlField) {
		this.controlField = controlField;
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

	public String getControlField() {
		return controlField;
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

}
