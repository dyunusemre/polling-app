package com.api.poling.question.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteQuestionRequest {
    private String questionId;
}
