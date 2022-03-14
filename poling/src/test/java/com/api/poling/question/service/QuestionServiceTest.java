package com.api.poling.question.service;

import com.api.poling.auth.service.UserService;
import com.api.poling.question.dao.Question;
import com.api.poling.question.dto.ApproveQuestionRequest;
import com.api.poling.question.enums.Status;
import com.api.poling.question.exception.SourceNotFoundException;
import com.api.poling.question.repository.QuestionRepository;
import com.api.poling.question.repository.UserAnswerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    UserService userService;

    @Mock
    QuestionRepository questionRepository;

    @Mock
    UserAnswerRepository userAnswerRepository;

    @InjectMocks
    QuestionService questionService;

    @Test
    void saveAnswer() {
    }

    @Test
    void getAllAnswers() {
    }

    @Test
    void retrieveAnswer() {
    }

    @Test
    void getAllQuestions() {
    }

    @Test
    void approveQuestion() {
        when(questionRepository.findById(anyString())).thenReturn(Optional.of(Question.builder()
                .status(Status.A.name())
                .build()));

        ApproveQuestionRequest request = ApproveQuestionRequest.builder()
                .questionId("qid12")
                .build();
        questionService.approveQuestion(request);

        ArgumentCaptor<Question> questionArgumentCaptor = ArgumentCaptor.forClass(Question.class);
        verify(questionRepository).save(questionArgumentCaptor.capture());
        Question captured = questionArgumentCaptor.getValue();

        assertThat(captured.getStatus()).isEqualTo(Status.A.name());
    }

    @Test
    void shouldThrownExceptionWhenQuestionNotFound() {
        ApproveQuestionRequest request = ApproveQuestionRequest.builder()
                .questionId("qid12")
                .build();
        when(questionRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> questionService.approveQuestion(request))
                .isInstanceOf(SourceNotFoundException.class)
                .hasMessageContaining("Question not found");

        verify(questionRepository, never()).save(any());
    }

    @Test
    void createQuestion() {
        Question question = Question.builder()
                .id("b12")
                .build();
        questionService.createQuestion(question);

        ArgumentCaptor<Question> questionArgumentCaptor = ArgumentCaptor.forClass(Question.class);
        verify(questionRepository).save(questionArgumentCaptor.capture());
        Question capturedQuestion = questionArgumentCaptor.getValue();
        assertThat(question.getId()).isEqualTo(capturedQuestion.getId());

    }

    @Test
    void deleteQuestion() {
    }
}