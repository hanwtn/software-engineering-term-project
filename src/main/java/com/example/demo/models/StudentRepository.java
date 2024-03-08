package com.example.demo.models;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<User,Integer>{
    
    List<User> findByUsername(String username);
    List<User> findAllByOrderByUid();
    boolean existsByUsername(String username);
}
