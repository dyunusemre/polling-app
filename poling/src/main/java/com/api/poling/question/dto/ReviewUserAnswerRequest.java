package com.api.poling.question.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUserAnswerRequest {
    private String questionId;
    private String userId;
}
