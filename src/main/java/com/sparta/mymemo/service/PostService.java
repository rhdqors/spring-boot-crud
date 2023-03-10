package com.sparta.mymemo.service;

import com.sparta.mymemo.dto.CommentResponseDto;
import com.sparta.mymemo.dto.PostRequestDto;
import com.sparta.mymemo.dto.PostResponseDto;
import com.sparta.mymemo.entity.Comment;
import com.sparta.mymemo.entity.Post;
import com.sparta.mymemo.entity.User;
import com.sparta.mymemo.jwt.JwtUtil;
import com.sparta.mymemo.repository.CommentRepository;
import com.sparta.mymemo.repository.PostRepository;
import com.sparta.mymemo.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor // 추적
public class PostService {

    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

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

    // 글 생성
    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, HttpServletRequest request) {
        User user = tokenMatchUser(request);

        // 요청받은 DTO 로 DB에 저장할 객체 만들기
        Post post = postRepository.saveAndFlush(new Post(requestDto, user));

        return new PostResponseDto(post);
    }

    // 전체 글 조회 - user 확인하는 토큰을 가져올 필요 없음
    @Transactional(readOnly = true)
    public List<PostResponseDto> getPosts() {
        List<Post> postlist = postRepository.findAllByOrderByCreatedAtDesc(); // 전체 게시글 리스트에 넣기
        //controller, service타입을 response로 바꾸고 바로 리턴해도 나오나 ?

        // 최종 리턴값 담을 리스트
        List<PostResponseDto> responselist = new ArrayList<>();

        for (Post post : postlist) { // 전체 게시물 리스트에서 각각의 게시물을 찾는다
            List<Comment> comments = post.getCommentList(); // 각 게시물의 댓글을 전체 저장하는 리스트
            List<CommentResponseDto> comResponseList = new ArrayList<>(); // 클라이언트 내보낼 형식의 리스트
            for (Comment comment : comments) { // 전체 댓글 리스트에서 각각의 댓글을 찾는다
                comResponseList.add(new CommentResponseDto(comment)); // 댓글을 클라이언트로 내보낼 형식으로 리스트 저장
            }

            responselist.add(new PostResponseDto(post,comResponseList));
            // 각 게시물에 댓글을 새로 넣어줘야 함
        }
        return responselist;
    }

    // 선택 글 조회 - user 확인하는 토큰을 가져올 필요 없음
    @Transactional
    public PostResponseDto getPost(Long postId) {

        // 게시글, 아이디일치 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        List<CommentResponseDto> commentList = new ArrayList<>();
        for (Comment comment : post.getCommentList()) {
            commentList.add(new CommentResponseDto(comment));
        }
        return new PostResponseDto(post, commentList);

    }

    // 글 수정
    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto requestDto, HttpServletRequest request) {
        User user = tokenMatchUser(request);
        Post post = postMatchUser(postId, user);

        post.updateMemo(requestDto);
        return new PostResponseDto(post);
    }

    // 글 삭제
    @Transactional
    public void deletePost(Long postId, HttpServletRequest request) {
        User user = tokenMatchUser(request);
        Post post = postMatchUser(postId, user);

        postRepository.deleteById(post.getId());
    }


    private Post postMatchUser(Long postId, User user) {
        Post post = postRepository.findByIdAndUsername(postId, user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("유저와 게시글이 일치하지 않습니다.")); // 로그인 유저가 작성한 글인지 확인
        return post;
    }
}


