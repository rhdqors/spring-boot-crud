package com.sparta.mymemo.entity;

import com.sparta.mymemo.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends TimeStamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "comment_id")
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 댓글 내용

    //    @JoinColumn(name = "user_id")
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "POST_ID", nullable = false)
    private Post post; // 어떤 게시물에 댓글 달지

    public Comment(CommentRequestDto commentRequestDto, User user, Post post) {
        this.content = commentRequestDto.getContent();
        this.user = user;
        this.post = post;
    }

    public void updateComment(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }
}
