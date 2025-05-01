package com.permovdb.permovdb.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Value("${file.upload-dir}")
    private String uploadDir;

    // public AuthResponse saveUser(String username, String password, MultipartFile
    // file) {
    // if (userRepository.findByUsername(username) == null) {

    // String fileName = null;
    // if (file != null && !file.isEmpty()) {
    // fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
    // Path filePath = Paths.get(uploadDir, fileName);
    // try {
    // Files.createDirectories(filePath.getParent());
    // Files.copy(file.getInputStream(), filePath,
    // StandardCopyOption.REPLACE_EXISTING);

    // } catch (IOException e) {
    // System.out.println("file cant created, try again");
    // e.printStackTrace();
    // }
    // }
    // User newUser = new User();

    // if (fileName != null) {
    // newUser = new User(username, bCrypt.encode(password), fileName);

    // } else {
    // newUser = new User(username, bCrypt.encode(password));
    // }

    // String token = jwtUtil.generateToken(newUser);

    // newUser.setJWToken(token);

    // userRepository.save(newUser);

    // return new AuthResponse(newUser.getUsername(), token);
    // }
    // return null; // user already exist
    // }

    public AuthResponse saveUser(String username, String password) {
        if (userRepository.findByUsername(username) == null) {

            User newUser = new User(username, bCrypt.encode(password));
            String token = jwtUtil.generateToken(newUser);

            newUser.setJWToken(token);

            userRepository.save(newUser);

            return new AuthResponse(newUser.getUsername(), token);
        }
        return null; // user already exist
    }

    public AuthResponse saveUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) == null) {

            User newUser = new User(user);

            String token = jwtUtil.generateToken(newUser);

            newUser.setJWToken(token);

            userRepository.save(newUser);

            return new AuthResponse(newUser.getUsername(), token);
        }
        return null; // user already exist
    }

    public AuthResponse updateUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {

            String token = jwtUtil.generateToken(user);

            user.setJWToken(token);

            userRepository.save(user);

            return new AuthResponse(user.getUsername(), token);
        }
        return null; // user already exist
    }

    public AuthResponse authUser(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        System.out.println("existing username" + existingUser.getUsername());

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
}
