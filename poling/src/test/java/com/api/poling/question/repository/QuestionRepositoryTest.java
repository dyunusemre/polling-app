package com.api.poling.question.repository;

import com.api.poling.question.dao.Question;
import com.api.poling.question.enums.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataMongoTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository underTestQuestionRepository;

    @AfterEach
    public void tearDown() {
        underTestQuestionRepository.deleteAll();
    }

    @Test
    void shouldFindByAStatus() {
        Question question = buildQuestionWitStatus(Status.A.name());
        underTestQuestionRepository.save(question);
        List<Question> questionList = underTestQuestionRepository.findByStatus(Status.A.name());
        assertThat(questionList.get(0).getStatus()).isEqualTo(Status.A.name());
    }

    @Test
    void shouldFindByWStatus() {
        Question question = buildQuestionWitStatus(Status.W.name());
        underTestQuestionRepository.save(question);
        List<Question> questionList = underTestQuestionRepository.findByStatus(Status.W.name());
        assertThat(questionList.get(0).getStatus()).isEqualTo(Status.W.name());
    }

    @Test
    void shouldNotFindByStatusA() {
        List<Question> questionList = underTestQuestionRepository.findByStatus(Status.A.name());
        assertThat(questionList.size()).isEqualTo(0);
    }

    private Question buildQuestionWitStatus(String status) {
        return Question.builder()
                .status(status)
                .build();
    }
}