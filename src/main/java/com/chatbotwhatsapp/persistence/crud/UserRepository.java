package com.chatbotwhatsapp.persistence.crud;

import com.chatbotwhatsapp.persistence.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByPhoneNumber(String phoneNumber);
}
