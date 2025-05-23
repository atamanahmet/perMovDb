package com.permovdb.permovdb.repository;

import com.permovdb.permovdb.domain.User;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
