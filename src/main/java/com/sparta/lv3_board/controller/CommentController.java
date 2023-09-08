package com.sparta.lv3_board.controller;

import com.sparta.lv3_board.dto.CommentRequestDto;
import com.sparta.lv3_board.dto.CommentResponseDto;
import com.sparta.lv3_board.dto.UserResponseDto;
import com.sparta.lv3_board.jwt.JwtUtil;
import com.sparta.lv3_board.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;
//    private final JwtUtil jwtUtil;

    // create-Comment
    @PostMapping("/comments/{boardId}")
    public CommentResponseDto createComment(@PathVariable Long boardId, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {
        return commentService.createComment(boardId, commentRequestDto, httpServletRequest);
    }

    // update-Comment
    @PutMapping("/comments/{commentId}")
    public CommentResponseDto updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {
        return commentService.updateComment(commentId, commentRequestDto, httpServletRequest);
    }

    // delete-Comment
    @DeleteMapping("/comments/{commentId}")
    public UserResponseDto deleteComment(@PathVariable Long commentId, HttpServletRequest httpServletRequest) {
        return commentService.deleteComment(commentId, httpServletRequest);
    }
}
