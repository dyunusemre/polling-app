package com.api.poling.question.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.api.poling.question.dao.Question;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {
	List<Question> findByStatus(String status);
	
}
