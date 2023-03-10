package com.sparta.mymemo.entity;

import com.sparta.mymemo.dto.CommentResponseDto;
import com.sparta.mymemo.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post extends TimeStamped{

    @Id // pk설정
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String username;

    @ManyToOne // N쪽이 주인이된다 - 외래키를 갖는다.
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();
//    @ManyToMany
//    private List<Comment> postList = new ArrayList<>();

    public Post(PostRequestDto requestDto, User user) {
        this.content = requestDto.getContent();
        this.title = requestDto.getTitle();
        this.username = user.getUsername();
        this.user = user;
    }


    public void updateMemo(PostRequestDto requestDto) {
        this.content = requestDto.getContent();
        this.title = requestDto.getTitle();
    }

}
