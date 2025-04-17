package com.permovdb.permovdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.permovdb.permovdb.domain.User;
import com.permovdb.permovdb.repository.UserRepository;
import com.permovdb.permovdb.security.JwtUtil;

@Service
public class UserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCrypt;
    // private

    public User saveUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {

            user.setPassword(bCrypt.encode(user.getPassword()));

            String token = jwtUtil.generateToken(user);

            user.setJWToken(token);

            return userRepository.save(user);
        }
        return null; // User already exist
    }

    public User authUser(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            if (bCrypt.matches(user.getPassword(), existingUser.getPassword())) {
                String token = jwtUtil.generateToken(existingUser);

                existingUser.setJWToken(token);
                userRepository.save(existingUser);

                return existingUser;
            }
        }
        return null;
    }
}
