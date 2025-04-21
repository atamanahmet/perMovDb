package com.permovdb.permovdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.permovdb.permovdb.domain.User;
import com.permovdb.permovdb.repository.UserRepository;
import com.permovdb.permovdb.security.AuthResponse;
import com.permovdb.permovdb.security.JwtUtil;

@Service
public class UserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCrypt;

    public AuthResponse saveUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) == null) {

            user.setPassword(bCrypt.encode(user.getPassword()));

            String token = jwtUtil.generateToken(user);

            user.setJWToken(token);

            userRepository.save(user);

            return new AuthResponse(user.getUsername(), token);
        }
        return null; // user already exist
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
            return null;
        }
        return null;

    }

    public User loadByUserName(String username) {
        return userRepository.findByUsername(username);
    }
}
