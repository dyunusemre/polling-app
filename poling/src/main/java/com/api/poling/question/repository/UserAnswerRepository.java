package com.api.poling.question.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.api.poling.question.dao.UserAnswer;

public interface UserAnswerRepository extends MongoRepository<UserAnswer, String>{
	Optional<UserAnswer> findByQuestionIdAndUserId(String questionId, String userId);
}
