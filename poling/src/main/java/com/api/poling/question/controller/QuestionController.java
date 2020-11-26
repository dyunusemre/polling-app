package com.api.poling.question.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.api.poling.question.component.QuestionComponent;
import com.api.poling.question.component.Status;
import com.api.poling.question.model.Question;
import com.api.poling.question.request.RequestApproveQuestion;
import com.api.poling.question.request.RequestRetriewUserAnswer;
import com.api.poling.question.request.RequestSaveAnswer;
import com.api.poling.question.request.ResponseRetrieveAnswer;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/question")
public class QuestionController {
	
	@Autowired
	QuestionComponent questionComponent;
	
	@GetMapping("/poll-questions")
	@ApiOperation("Get all Questions with aswers")
	public ResponseEntity<MappingJacksonValue> getPollQuestions() {
		return new ResponseEntity<MappingJacksonValue>(questionComponent.getAllQuestions(Status.A), HttpStatus.OK);
	}
	
	@GetMapping("/poll-waiting-questions")
	@ApiOperation("Get all Questions that waits to approve by Admin")
	public ResponseEntity<MappingJacksonValue> getPollWaitingQuestions() {
		return new ResponseEntity<MappingJacksonValue>(questionComponent.getAllQuestions(Status.W), HttpStatus.OK);
	}
	
	@PostMapping("/approve-question")
	@ApiOperation("Approve the questions on waiting")
	public void approveWaitingQuestions(@RequestBody RequestApproveQuestion request) {
		questionComponent.approveQuestion(request.getQuestionId());
	}
	
	@PostMapping("/retrive-answer")
	@ApiOperation("Retrieves the answer of user for specific question")
	public ResponseRetrieveAnswer retrieveUserAnswer(@RequestBody RequestRetriewUserAnswer request) {
		return questionComponent.retrieveAnswer(request);
	}
	
	@PutMapping("/send-answer")
	@ApiOperation("Save the answer for specified question")
	public void sendAnswer(@RequestBody RequestSaveAnswer request) {
		questionComponent.saveAnswer(request.getQuestionId(), request.getUserId(), request.getOptionNo());
	}
	
	@PostMapping("/create-question")
	@ApiOperation("Creates a new question")
	public void createQuestion(@RequestBody Question question) {
		questionComponent.createQuestion(question);
	}
	
	@DeleteMapping("/delete-question/{id}")
	@ApiOperation("Deletes the specified question")
	public void deleteQuestion(@PathVariable String id) {
		questionComponent.deleteQuestion(id);
	}
	
	@PatchMapping("/modify-question")
	@ApiOperation("Editing the existing question")
	public void modifyQuestion(@RequestBody Question question) {
		questionComponent.createQuestion(question);
	}
	
	@GetMapping("/poll-answer")
	@ApiOperation("Get all Questions with aswers and response count")
	public List<Question> getPollAnswers() {
		return questionComponent.getAllAnswers();
	}
}
