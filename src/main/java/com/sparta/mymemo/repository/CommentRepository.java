package com.sparta.mymemo.repository;

import com.sparta.mymemo.entity.Comment;
import com.sparta.mymemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndUser(Long id, User user);
}
