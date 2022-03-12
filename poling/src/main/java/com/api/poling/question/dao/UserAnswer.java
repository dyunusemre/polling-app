package com.api.poling.question.dao;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("UserAnswer")
public class UserAnswer {
    @Id
    private String id;
    private String questionId;
    private String userId;
    private int optionNo;
}
