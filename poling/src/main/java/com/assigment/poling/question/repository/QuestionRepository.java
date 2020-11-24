package com.assigment.poling.question.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.assigment.poling.question.model.Question;

public interface QuestionRepository extends MongoRepository<Question, String> {
	List<Question> findByStatus(String status);
	
}
