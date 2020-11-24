package com.assigment.poling.question.model;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("AnswerFilter")
public class Answer {
	private Integer optionNo;
	private String option;
	private int answerCount;

	public Answer() {
	}

	public Answer(Integer optionNo, String option, Integer answerCount) {
		super();
		this.optionNo = optionNo;
		this.option = option;
		this.answerCount = answerCount;
	}

	public Integer getOptionNo() {
		return optionNo;
	}

	public void setOptionNo(Integer optionNo) {
		this.optionNo = optionNo;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public Integer getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(Integer answerCount) {
		this.answerCount = answerCount;
	}

}
