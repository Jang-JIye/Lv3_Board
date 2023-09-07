package com.sparta.lv3_board.controller;

import com.sparta.lv3_board.dto.BoardRequestDto;
import com.sparta.lv3_board.dto.BoardResponseDto;
import com.sparta.lv3_board.entity.Board;
import com.sparta.lv3_board.jwt.JwtUtil;
import com.sparta.lv3_board.service.BoardService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@AllArgsConstructor
public class BoardController {
//
//
//    private final BoardService boardService;
//
//
//    //create
//    @PostMapping("/boards")
//    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto requestDto, HttpServletRequest httpServletRequest) {
//        BoardResponseDto responseDto = boardService.createBoard(requestDto, httpServletRequest);
//        if (responseDto != null) {
//            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }    }
//
//    //readAll
//    @GetMapping("/boards")
//    public List<BoardResponseDto> getAllBoard() {
//        return boardService.getBoards();
//    }
//
//
//    //read
//    @GetMapping("/boards/{id}")
//    public Board getBoard(@PathVariable Long id) {
//
//        return boardService.getBoard(id);
//    }
//
//    //update
//    @PutMapping("/boards/{id}")
//    public ResponseEntity<String> updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto, HttpServletRequest httpServletRequest) {
//        ResponseEntity<String> response = boardService.updateBoard(id, requestDto, httpServletRequest);
//        if (response != null) {
//            return response;
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();// 인증 실패
//        }    }
//
//    //delete
//    @DeleteMapping("/boards/{id}")
//    public ResponseEntity<String> deleteBoard(@PathVariable Long id,  HttpServletRequest httpServletRequest) {
//        ResponseEntity<String> response = boardService.deleteBoard(id, httpServletRequest);
//        if (response != null) {
//            return response;
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }


    private final BoardService boardService;
    private final JwtUtil jwtUtil;


    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(
            @RequestBody BoardRequestDto requestDto, HttpServletRequest request) {
        // JWT 토큰을 검증하고 게시물 생성
        String token = jwtUtil.resolveToken(request);
        if (token != null && jwtUtil.validateToken(token)) {
            Claims claims = jwtUtil.getUserInfoFromToken(token);
            String username = claims.getSubject();

            BoardResponseDto responseDto = boardService.createBoard(requestDto, username);
            return ResponseEntity.ok(responseDto);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<Board> getBoard(
            @PathVariable Long boardId, HttpServletRequest request) {
        // JWT 토큰을 검증하고 게시물 가져오기
        String token = jwtUtil.resolveToken(request);
        if (token != null && jwtUtil.validateToken(token)) {
            Claims claims = jwtUtil.getUserInfoFromToken(token);
            String username = claims.getSubject();

            Board responseDto = boardService.getBoard(boardId, username);
            return ResponseEntity.ok(responseDto);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<String> updateBoard(
            @PathVariable Long boardId, @RequestBody BoardRequestDto requestDto, HttpServletRequest request) {
        // JWT 토큰을 검증하고 게시물 수정
        String token = jwtUtil.resolveToken(request);
        if (token != null && jwtUtil.validateToken(token)) {
            Claims claims = jwtUtil.getUserInfoFromToken(token);
            String username = claims.getSubject();

            ResponseEntity<String> response = boardService.updateBoard(boardId, requestDto, username);
            return response;
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(
            @PathVariable Long boardId, HttpServletRequest request) {
        // JWT 토큰을 검증하고 게시물 삭제
        String token = jwtUtil.resolveToken(request);
        if (token != null && jwtUtil.validateToken(token)) {
            Claims claims = jwtUtil.getUserInfoFromToken(token);
            String username = claims.getSubject();

            ResponseEntity<String> response = boardService.deleteBoard(boardId, username);
            return response;
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
    }
}

