package com.thesis.recommenderapp.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.thesis.recommenderapp.domain.User;

public interface UserDao extends JpaRepository<User, Long> {

    Page<User> findAllByUserNameContainingIgnoreCase(String substring, Pageable pageable);

    User findByUserName(String userName);

}
