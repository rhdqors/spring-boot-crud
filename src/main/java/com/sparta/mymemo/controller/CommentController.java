package com.sparta.mymemo.controller;

import com.sparta.mymemo.dto.CommentRequestDto;
import com.sparta.mymemo.dto.CommentResponseDto;
import com.sparta.mymemo.dto.PostRequestDto;
import com.sparta.mymemo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/comment")
    public CommentResponseDto addComment(@RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
        return commentService.addComment(commentRequestDto, request);
    }

    @PutMapping("/api/comment/{id}")
    public CommentResponseDto updateComment(@RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
        return commentService.updateComment(commentRequestDto, request);
    }
}
