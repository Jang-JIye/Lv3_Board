package com.sparta.lv3_board.controller;

import com.sparta.lv3_board.dto.BoardRequestDto;
import com.sparta.lv3_board.dto.BoardResponseDto;
import com.sparta.lv3_board.entity.Board;
import com.sparta.lv3_board.jwt.JwtUtil;
import com.sparta.lv3_board.security.UserDetailsImpl;
import com.sparta.lv3_board.service.BoardService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final JwtUtil jwtUtil;

    //create
    @PostMapping("/boards")
    public ResponseEntity<BoardResponseDto> createBoard(
            @RequestBody BoardRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        BoardResponseDto responseDto = boardService.createBoard(requestDto, userDetails.getUser());
        return ResponseEntity.ok(responseDto);
    }


    //readAll
    @GetMapping("/boards")
    public ResponseEntity<List<BoardResponseDto>> getAllBoards(HttpServletRequest request) {
        List<BoardResponseDto> responseDtoList = boardService.getAllBoards();
        return ResponseEntity.ok(responseDtoList);
    }


    //read
    @GetMapping("/boards/{boardId}")
    public ResponseEntity<BoardResponseDto> getBoard(@PathVariable Long boardId) {
        BoardResponseDto responseDto = boardService.getBoard(boardId);
        return ResponseEntity.ok(responseDto);
    }


    //update
    @PutMapping("/boards/{boardId}")
    public ResponseEntity<?> updateBoard(
            @PathVariable Long boardId, @RequestBody BoardRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        // JWT 토큰을 검증하고 게시물 수정
        return boardService.updateBoard(boardId, requestDto, userDetails.getUser());
    }


    //delete
    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<String> deleteBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return boardService.deleteBoard(boardId, userDetails.getUser());
    }
}

