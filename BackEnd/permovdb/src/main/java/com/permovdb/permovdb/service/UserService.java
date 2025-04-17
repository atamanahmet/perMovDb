package com.permovdb.permovdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.permovdb.permovdb.domain.User;
import com.permovdb.permovdb.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCrypt;
    // private

    public User saveUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            user.setPassword(bCrypt.encode(user.getPassword()));
            return userRepository.save(user);
        }
        return null;
    }
}
