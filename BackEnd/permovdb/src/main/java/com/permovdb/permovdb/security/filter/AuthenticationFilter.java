// package com.permovdb.permovdb.security.filter;

// import java.io.IOException;
// import java.util.Date;

// import org.springframework.beans.factory.annotation.Autowired;
// import
// org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.AuthenticationException;
// import
// org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// import com.auth0.jwt.JWT;
// import com.auth0.jwt.algorithms.Algorithm;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.permovdb.permovdb.security.SecurityConstants;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.AllArgsConstructor;

// @AllArgsConstructor
// public class AuthenticationFilter extends
// UsernamePasswordAuthenticationFilter {

// @Autowired
// private CustomAuthenticationManager authenticationManager;

// @Override
// public Authentication attemptAuthentication(HttpServletRequest request,
// HttpServletResponse response)
// throws AuthenticationException {
// try {
// User user = new ObjectMapper().readValue(request.getInputStream(),
// User.class);

// return authenticationManager
// .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),
// user.getPassword()));
// } catch (IOException e) {
// throw new RuntimeException();
// }

// }

// @Override
// protected void unsuccessfulAuthentication(HttpServletRequest request,
// HttpServletResponse response,
// AuthenticationException failed) throws IOException, ServletException {
// response.setStatus(401);
// response.getWriter().write(failed.getMessage());
// System.out.println("Authentication Unsuccessfull");

// }

// @Override
// protected void successfulAuthentication(HttpServletRequest request,
// HttpServletResponse response, FilterChain chain,
// Authentication authResult) throws IOException, ServletException {

// String token = JWT.create()
// .withSubject(authResult.getName())
// .withExpiresAt(new Date(System.currentTimeMillis() +
// SecurityConstants.TOKEN_EXPIRE_IN_MILLISECONDS))
// .sign(Algorithm.HMAC256(SecurityConstants.SECRET_KEY));

// response.addHeader(SecurityConstants.AUTHORIZATION, SecurityConstants.BEARER
// + token);
// response.getWriter().write("Authorization successfull");
// }

// }
