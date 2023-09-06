package com.sparta.lv3_board.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {

    private String message;       //메세지
    private Integer statusCode; //상태코드


    public UserResponseDto(String message, Integer statusCode) {
        this.message = message;
        this.statusCode = statusCode;

    }
}
