package com.api.poling.question.service;

import java.util.List;

import com.api.poling.auth.exception.UserNotFoundException;
import com.api.poling.auth.service.UserService;
import com.api.poling.question.dto.ApproveQuestionRequest;
import com.api.poling.question.dto.SaveAnswerRequest;
import com.api.poling.question.enums.Status;
import com.api.poling.question.exception.SourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.json.MappingJacksonValue;

import com.api.poling.question.dao.Question;
import com.api.poling.question.dao.UserAnswer;
import com.api.poling.question.repository.QuestionRepository;
import com.api.poling.question.repository.UserAnswerRepository;
import com.api.poling.question.dto.ReviewUserAnswerRequest;
import com.api.poling.question.dto.RetrieveAnswerResponse;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final UserService userService;
    private final QuestionRepository questionRepository;
    private final UserAnswerRepository userAnswerRepository;

    public void saveAnswer(SaveAnswerRequest request) {
        Question question = validateUserAnswerAndGetQuestion(request);
        question.getAnswers().stream()
                .filter(a -> a.getOptionNo().equals(request.getOptionNo()))
                .forEach(a -> a.setAnswerCount(a.getAnswerCount() + 1));
        questionRepository.save(question);
        userAnswerRepository.save(buildUserAnswer(request));
    }

    public List<Question> getAllAnswers() {
        return questionRepository.findByStatus(Status.A.name());
    }

    public RetrieveAnswerResponse retrieveAnswer(ReviewUserAnswerRequest request) {
        RetrieveAnswerResponse response = RetrieveAnswerResponse.builder().build();
        userAnswerRepository.findByQuestionIdAndUserId(request.getQuestionId(), request.getUserId()).ifPresent(answer -> {
            response.setOptionNo(answer.getOptionNo());
        });
        return response;
    }

    public MappingJacksonValue getAllQuestions(Status status) {
        List<Question> questionList = questionRepository.findByStatus(status.name());
        MappingJacksonValue mapping = new MappingJacksonValue(questionList);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("option", "optionNo");
        FilterProvider filters = new SimpleFilterProvider().addFilter("AnswerFilter", filter);
        mapping.setFilters(filters);
        return mapping;
    }

    public void approveQuestion(ApproveQuestionRequest request) {
        Question question = questionRepository.findById(request.getQuestionId()).orElseThrow(SourceNotFoundException::new);
        question.setStatus(Status.A.name());
        questionRepository.save(question);
    }

    public void createQuestion(Question question) {
        questionRepository.save(question);
    }

    public void deleteQuestion(String questionId) {
        questionRepository.deleteById(questionId);
    }

    private Question validateUserAnswerAndGetQuestion(SaveAnswerRequest request) {
        userService.findUserById(request.getUserId()).orElseThrow(UserNotFoundException::new);
        return questionRepository.findById(request.getQuestionId()).orElseThrow(SourceNotFoundException::new);
    }

    private UserAnswer buildUserAnswer(SaveAnswerRequest request) {
        return UserAnswer.builder()
                .questionId(request.getQuestionId())
                .userId(request.getUserId())
                .optionNo(request.getOptionNo())
                .build();
    }

}
