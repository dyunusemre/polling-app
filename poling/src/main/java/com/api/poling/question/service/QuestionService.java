package com.api.poling.question.service;

import java.util.List;
import java.util.Optional;

import com.api.poling.question.enums.Status;
import com.api.poling.question.exception.SourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;

import com.api.poling.auth.dao.User;
import com.api.poling.auth.repository.UserRepository;
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

//TODO service needs refactoring
@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final UserAnswerRepository userAnswerRepository;

    public void saveAnswer(String questionId, String userId, Integer optionNo) {
        Optional<Question> questionOpt = questionRepository.findById(questionId);
        Optional<User> userOpt = userRepository.findById(userId);
        if (questionOpt.isPresent() && userOpt.isPresent()) {
            questionOpt.get().getAnswers().stream().filter(a -> a.getOptionNo().equals(optionNo))
                    .forEach(a -> a.setAnswerCount(a.getAnswerCount() + 1));
            questionRepository.save(questionOpt.get());
            userAnswerRepository.save(new UserAnswer(null, questionId, userId, optionNo));
        } else {
            throw new SourceNotFoundException("QUESTION_COULD_NOT_FIND");
        }
    }

    public List<Question> getAllAnswers() {
        return questionRepository.findByStatus(Status.A.name());
    }

    public RetrieveAnswerResponse retrieveAnswer(ReviewUserAnswerRequest request) {
        RetrieveAnswerResponse response = new RetrieveAnswerResponse();
        Optional<UserAnswer> userAnswer = userAnswerRepository
                .findByQuestionIdAndUserId(request.getQuestionId(), request.getUserId());
        if (userAnswer.isPresent()) {
            response.setOptionNo(userAnswer.get().getOptionNo());
        }
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

    public void approveQuestion(String qId) {
        Question question = questionRepository.findById(qId)
                .orElseThrow(() -> new SourceNotFoundException("SOURCE_NOT_FOUND"));
        question.setStatus(Status.A.name());
        questionRepository.save(question);
    }

    public void createQuestion(Question question) {
        questionRepository.save(question);
    }

    public void deleteQuestion(String questionId) {
        questionRepository.deleteById(questionId);
    }

}
