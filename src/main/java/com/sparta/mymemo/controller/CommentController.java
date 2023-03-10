package com.sparta.mymemo.controller;

import com.sparta.mymemo.dto.CommentRequestDto;
import com.sparta.mymemo.dto.CommentResponseDto;
import com.sparta.mymemo.dto.ResponseCodeDto;
import com.sparta.mymemo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
        return commentService.updateComment(id, commentRequestDto, request);
    }

    @DeleteMapping("/api/comment/{id}")
    public ResponseEntity<ResponseCodeDto> deleteComment(@PathVariable Long id, HttpServletRequest request) {
        commentService.deleteComment(id, request);
        return new ResponseEntity<>(new ResponseCodeDto("댓글 삭제 완료.", HttpStatus.OK.value()), HttpStatus.OK) ;
    }

}
