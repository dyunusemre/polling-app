package com.api.poling.question.request;

public class RequestApproveQuestion {
	private String questionId;
	
	public RequestApproveQuestion() {
	}
	
	public RequestApproveQuestion(String questionId) {
		this.questionId = questionId;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

}
