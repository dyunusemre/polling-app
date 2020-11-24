package com.assigment.poling.auth.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.assigment.poling.auth.model.User;

public interface UserRepository extends MongoRepository<User, String> {
	Optional<User> findByUsernameAndPassword(String username, String password);

	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);
}
