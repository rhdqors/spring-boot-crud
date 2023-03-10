package com.sparta.mymemo.service;

import com.sparta.mymemo.dto.CommentRequestDto;
import com.sparta.mymemo.dto.CommentResponseDto;
import com.sparta.mymemo.dto.PostRequestDto;
import com.sparta.mymemo.entity.Comment;
import com.sparta.mymemo.entity.Post;
import com.sparta.mymemo.entity.User;
import com.sparta.mymemo.jwt.JwtUtil;
import com.sparta.mymemo.repository.CommentRepository;
import com.sparta.mymemo.repository.PostRepository;
import com.sparta.mymemo.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor

public class CommentService {

    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    private User tokenMatchUser(HttpServletRequest request) {
        // 토큰 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 가능
        User user;
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("토큰이 없습니다.");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            user = userRepository.findByUsername(claims.getSubject()).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        } else {
            throw new IllegalArgumentException("Token missing");
        }
        return user;
    }

    public CommentResponseDto addComment(CommentRequestDto commentRequestDto, HttpServletRequest request) {

        User user = tokenMatchUser(request);
//        List<String> comments = commentRequestDto.get;

        Post post = postRepository.findById(commentRequestDto.getId()).orElseThrow(
                () -> new IllegalArgumentException("게시글 없음")
        ); // 댓글달아야할 게시글 찾아옴

        Comment comment = commentRepository.saveAndFlush(new Comment(commentRequestDto, user, post));
        return new CommentResponseDto(comment);
    }

    public CommentResponseDto updateComment(CommentRequestDto commentRequestDto, HttpServletRequest request) {
    }
}
