package com.api.poling.question.dao;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("AnswerFilter")
public class Answer {
    private Integer optionNo;
    private String option;
    private int answerCount;
}
