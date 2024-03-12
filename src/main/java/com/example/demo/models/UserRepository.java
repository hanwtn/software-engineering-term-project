package com.example.demo.models;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer>{
    
    User findByUsername(String username);
    User findById(int uid);
    List<User> findAllByOrderByUid();
    boolean existsByUsername(String username);
    User findByUid(int uid);
}
