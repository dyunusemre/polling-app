package com.api.poling.question.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.poling.question.service.QuestionService;
import com.api.poling.question.enums.Status;
import com.api.poling.question.dao.Question;
import com.api.poling.question.dto.ApproveQuestionRequest;
import com.api.poling.question.dto.ReviewUserAnswerRequest;
import com.api.poling.question.dto.SaveAnswerRequest;
import com.api.poling.question.dto.RetrieveAnswerResponse;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/poll-questions")
    @ApiOperation("Get all Questions with answers")
    public ResponseEntity<MappingJacksonValue> getPollQuestions() {
        return new ResponseEntity<MappingJacksonValue>(questionService.getAllQuestions(Status.A), HttpStatus.OK);
    }

    @GetMapping("/poll-waiting-questions")
    @ApiOperation("Get all Questions that waits to approve by Admin")
    public ResponseEntity<MappingJacksonValue> getPollWaitingQuestions() {
        return new ResponseEntity<MappingJacksonValue>(questionService.getAllQuestions(Status.W), HttpStatus.OK);
    }

    @PostMapping("/approve-question")
    @ApiOperation("Approve the questions on waiting")
    public void approveWaitingQuestions(@RequestBody ApproveQuestionRequest request) {
        questionService.approveQuestion(request.getQuestionId());
    }

    @PostMapping("/retrive-answer")
    @ApiOperation("Retrieves the answer of user for specific question")
    public RetrieveAnswerResponse retrieveUserAnswer(@RequestBody ReviewUserAnswerRequest request) {
        return questionService.retrieveAnswer(request);
    }

    @PutMapping("/send-answer")
    @ApiOperation("Save the answer for specified question")
    public void sendAnswer(@RequestBody SaveAnswerRequest request) {
        questionService.saveAnswer(request.getQuestionId(), request.getUserId(), request.getOptionNo());
    }

    @PostMapping("/create-question")
    @ApiOperation("Creates a new question")
    public void createQuestion(@RequestBody Question question) {
        questionService.createQuestion(question);
    }

    @DeleteMapping("/delete-question/{id}")
    @ApiOperation("Deletes the specified question")
    public void deleteQuestion(@PathVariable String id) {
        questionService.deleteQuestion(id);
    }

    @PatchMapping("/modify-question")
    @ApiOperation("Editing the existing question")
    public void modifyQuestion(@RequestBody Question question) {
        questionService.createQuestion(question);
    }

    @GetMapping("/poll-answer")
    @ApiOperation("Get all Questions with answers and response count")
    public List<Question> getPollAnswers() {
        return questionService.getAllAnswers();
    }
}
