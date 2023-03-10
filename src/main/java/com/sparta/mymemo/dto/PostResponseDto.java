package com.sparta.mymemo.dto;

import com.sparta.mymemo.entity.Comment;
import com.sparta.mymemo.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDto { // 클라로 어떤값을 내보낼지

    private Long id;
    private String title;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentList = new ArrayList<>();

    public PostResponseDto(Post post/*, CommentResponseDto commentResponseDto*/) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getUsername();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();

//        this.commentList = commentResponseDto..;
    }

    public PostResponseDto(Post post, List<CommentResponseDto> comResponseList) {
        this(post);
//        this.id = post.getId();
//        this.title = post.getTitle();
//        this.username = post.getUsername();
//        this.content = post.getContent();
//        this.createdAt = post.getCreatedAt();
//        this.modifiedAt = post.getModifiedAt();
        this.commentList = comResponseList;
    }
}
