package com.permovdb.permovdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.permovdb.permovdb.domain.User;
import com.permovdb.permovdb.repository.UserRepository;
import com.permovdb.permovdb.security.AuthResponse;
import com.permovdb.permovdb.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCrypt;

    public AuthResponse saveUser(String username, String password) {
        if (userRepository.findByUsername(username) == null) {

            User newUser = new User(username, bCrypt.encode(password));

            String token = jwtUtil.generateToken(newUser);

            newUser.setJWToken(token);

            userRepository.save(newUser);

            return new AuthResponse(newUser.getUsername(), token);
        }
        return null;
    }

    public AuthResponse updateUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {

            userRepository.save(user);

            return new AuthResponse(user.getUsername(), user.getJWToken());
        }
        return null;
    }

    public AuthResponse authUser(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser != null) {

            if (bCrypt.matches(user.getPassword(), existingUser.getPassword())) {

                String token = jwtUtil.generateToken(existingUser);

                existingUser.setJWToken(token);

                userRepository.save(existingUser);

                return new AuthResponse(user.getUsername(), token);
            }
        }
        return null;

    }

    public User loadByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserFromRequest(HttpServletRequest request) {

        String username = jwtUtil.extractUsernameFromRequest(request);

        User user = loadByUserName(username);

        if (user != null) {
            return user;
        }
        return null;
    }
}
