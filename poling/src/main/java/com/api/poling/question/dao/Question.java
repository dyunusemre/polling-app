package com.api.poling.question.dao;

import java.util.List;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "Question")
public class Question {
    @Id
    private String id;
    private String question;
    private String status;
    @Field("Answer")
    private List<Answer> answers;
}
