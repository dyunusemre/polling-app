package com.api.poling.question.service;

import com.api.poling.auth.service.UserService;
import com.api.poling.question.dao.Answer;
import com.api.poling.question.dao.Question;
import com.api.poling.question.dao.UserAnswer;
import com.api.poling.question.dto.ApproveQuestionRequest;
import com.api.poling.question.dto.RetrieveAnswerResponse;
import com.api.poling.question.dto.ReviewUserAnswerRequest;
import com.api.poling.question.dto.SaveAnswerRequest;
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

import java.util.*;

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
        SaveAnswerRequest request = SaveAnswerRequest.builder()
                .optionNo(1)
                .userId("u11")
                .questionId("b12")
                .build();
        Question question = buildQuestion();
        when(userService.findUserById(request.getUserId()))
                .thenReturn(Optional.empty());
        when(questionRepository.findById(request.getQuestionId()))
                .thenReturn(Optional.of(question));

        verify(questionRepository, times(1)).save(question);
        assertThat(question.getAnswers().get(0).getAnswerCount()).isNotEqualTo(0);
    }

    @Test
    void getAllAnswers() {
    }

    @Test
    void retrieveAnswer() {
        ReviewUserAnswerRequest request = ReviewUserAnswerRequest.builder()
                .questionId("b12")
                .userId("u1")
                .build();
        when(userAnswerRepository.findByQuestionIdAndUserId(anyString(), anyString())).thenReturn(Optional.of(UserAnswer.builder()
                .questionId("b12")
                .userId("u1")
                .optionNo(1)
                .build()));

        RetrieveAnswerResponse retrieveAnswerResponse = questionService.retrieveAnswer(request);
        assertThat(retrieveAnswerResponse.getOptionNo()).isNotNull();
        assertThat(retrieveAnswerResponse.getOptionNo()).isEqualTo(1);
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
        String questionId = "b12";
        questionService.deleteQuestion("b12");
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(questionRepository).deleteById(stringArgumentCaptor.capture());
        String capturedQuestionId = stringArgumentCaptor.getValue();
        assertThat(questionId).isEqualTo(capturedQuestionId);
    }

    Question buildQuestion() {
        return Question.builder()
                .answers(buildAnswerList())
                .build();
    }

    List<Answer> buildAnswerList() {
        List<Answer> answerList = new ArrayList<>();
        answerList.add(Answer.builder().answerCount(0).optionNo(1).option("Option1").build());
        answerList.add(Answer.builder().answerCount(0).optionNo(2).option("Option2").build());
        answerList.add(Answer.builder().answerCount(0).optionNo(3).option("Option3").build());
        answerList.add(Answer.builder().answerCount(0).optionNo(4).option("Option4").build());
        return answerList;
    }
}