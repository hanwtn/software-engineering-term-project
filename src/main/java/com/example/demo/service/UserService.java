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
        if (newPassword.isEmpty()) {
            return new Error(400, "Password must be provided");
        } else if (newPassword.length() < PASSWORD_MIN_LENGTH) {
            return new Error(400, "Password must be at least " + PASSWORD_MIN_LENGTH + " characters");
        } else if (!isValidPassword(newPassword)) {
            return new Error(400,
                    "Password must include at least one uppercase letter, one lowercase letter, and one symbol");
        }
        return toRet;
    }

    public Error validateUsername(String newUsername) {
        if (newUsername.isEmpty()) {
            return new Error(400, "Username must be provided");
        } else if (newUsername.length() < USERNAME_MIN_LENGTH) {
            return new Error(400, "Username must be at least " + USERNAME_MIN_LENGTH + " characters");
        } else if (userRepository.existsByUsername(newUsername)) {
            return new Error(409, "Username already exists");
        }

        // default constructor creates a non-error (valid)
        return new Error();
    }

    // login version, bypass the error of username already exists
    public Error validateUsernameLogin(String newUsername) {
        Error error = validateUsername(newUsername);
        if (error.message.equals("Username already exists")) {
            return new Error();
        } else {
            return error;
        }
    }

    public Error validateLogin(String newUsername, String newPassword) {
        Error[] validations = { validateUsernameLogin(newUsername), validatePassword(newPassword) };
        for (Error validation : validations) {
            if (validation.isError) {
                return validation;
            }
        }

        User user = userRepository.findByUsername(newUsername);
        boolean userNotFound = (user == null);
        boolean incorrectPassword = true;
        if (user != null) {
            incorrectPassword = !user.getPassword().equals(newPassword);
        }
        if (userNotFound || incorrectPassword) {
            return new Error(401, "Incorrect username or password.");
        }
        return new Error();
    }

    public Error validateRegistration(String newUsername, String newPassword) {
        Error[] validations = { validateUsername(newUsername), validatePassword(newPassword) };
        for (Error validation : validations) {
            if (validation.isError) {
                return validation;
            }
        }
        return new Error();
    }
}
