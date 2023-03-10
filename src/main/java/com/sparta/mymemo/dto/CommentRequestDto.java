package com.sparta.mymemo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private Long id;
    private String content;
}
