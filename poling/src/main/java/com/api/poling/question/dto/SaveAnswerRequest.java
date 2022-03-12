package com.api.poling.question.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveAnswerRequest {
    private String questionId;
    private String userId;
    private Integer optionNo;
}
