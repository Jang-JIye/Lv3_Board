package com.sparta.lv3_board.dto;


import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long BoardId;
    private Long commentId;
    private String comment;
}
