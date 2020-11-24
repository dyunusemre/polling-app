package com.assigment.poling.question.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("UserAnswer")
public class UserAnswer {
	@Id
	private String id;
	private String questionId;
	private String userId;
	private int optionNo;
	
	public UserAnswer() {
	}
	
	public UserAnswer(String id, String questionId, String userId, int optionNo) {
		super();
		this.id = id;
		this.questionId = questionId;
		this.userId = userId;
		this.optionNo = optionNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getOptionNo() {
		return optionNo;
	}

	public void setOptionNo(int optionNo) {
		this.optionNo = optionNo;
	}

}
