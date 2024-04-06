package com.example.demo.service;

import com.example.demo.models.User;
import com.example.demo.models.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final int USERNAME_MIN_LENGTH = 4;
    private final int PASSWORD_MIN_LENGTH = 8;

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public boolean isValidPassword(String password) {
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());

        boolean hasSymbol = password.matches(".*\\W.*");
        boolean isLongEnough = password.length() >= PASSWORD_MIN_LENGTH;

        return hasUppercase && hasLowercase && hasSymbol && isLongEnough;
    }

    public Error validatePassword(String newPassword) {
        Error toRet = new Error();

        return toRet;
    }

    public Error validateUsername(String newUsername) {
        if (newUsername.isEmpty()) {
            return new Error(400, "Username not provided.");
        } else if (newUsername.length() < USERNAME_MIN_LENGTH) {
            return new Error(400, "Username must be at least " + USERNAME_MIN_LENGTH + " characters");
        } else if (userRepository.existsByUsername(newUsername)) {
            return new Error(409, "Username already exists");
        }
        
        //default constructor creates a non-error (valid)
        return new Error();
    }
}
