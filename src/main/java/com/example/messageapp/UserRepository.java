package com.example.messageapp;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>{
    User findByUsername(String username);
    User findById(long id);
    User findByEmail(String email);
    Long countByEmail(String email);
    Long countByUsername(String username);
}



