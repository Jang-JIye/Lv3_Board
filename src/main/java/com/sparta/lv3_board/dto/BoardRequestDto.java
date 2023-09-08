package com.sparta.lv3_board.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BoardRequestDto {
    @NotBlank
    private String title;//제목
    @NotBlank
    private String contents;//작성 내용
}
