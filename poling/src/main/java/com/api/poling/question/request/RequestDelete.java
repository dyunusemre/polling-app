package com.api.poling.question.request;

public class RequestDelete {
	private String questionId;

	public RequestDelete() {
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getQuestionId() {
		return questionId;
	}
}
