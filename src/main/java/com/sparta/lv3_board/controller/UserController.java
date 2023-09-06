package com.sparta.lv3_board.controller;


import com.sparta.lv3_board.dto.LoginRequestDto;
import com.sparta.lv3_board.dto.SignupRequestDto;
import com.sparta.lv3_board.dto.UserResponseDto;
import com.sparta.lv3_board.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

        //회원 가입
    @PostMapping("/user/signup")
    public UserResponseDto signup(@RequestBody SignupRequestDto requestDto,HttpServletResponse httpServletResponse) {
        return userService.signup(requestDto);
    }


    //로그인
    @PostMapping("/user/login")
    public UserResponseDto login(@RequestBody LoginRequestDto requestDto, HttpServletResponse httpServletResponse) throws IOException {
        return userService.login(requestDto, httpServletResponse);
    }
}
