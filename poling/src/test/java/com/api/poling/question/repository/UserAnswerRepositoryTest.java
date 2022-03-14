package com.api.poling.question.repository;

import com.api.poling.auth.dao.User;
import com.api.poling.question.dao.Question;
import com.api.poling.question.dao.UserAnswer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataMongoTest
class UserAnswerRepositoryTest {

    @Autowired
    private UserAnswerRepository underTestUserAnswerRepository;

    @AfterEach
    public void tearDown() {
        underTestUserAnswerRepository.deleteAll();
    }

    @Test
    void shouldFindByQuestionIdAndUserId() {
        UserAnswer userAnswer = UserAnswer.builder()
                .userId("ab2")
                .questionId("1")
                .build();
        underTestUserAnswerRepository.save(userAnswer);
        Optional<UserAnswer> actual = underTestUserAnswerRepository.findByQuestionIdAndUserId("1", "ab2");
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getUserId()).isEqualTo(userAnswer.getUserId());
        assertThat(actual.get().getQuestionId()).isEqualTo(userAnswer.getQuestionId());
    }
}