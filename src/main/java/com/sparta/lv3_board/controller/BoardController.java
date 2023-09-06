package com.sparta.lv3_board.controller;

import com.sparta.lv3_board.dto.BoardRequestDto;
import com.sparta.lv3_board.dto.BoardResponseDto;
import com.sparta.lv3_board.entity.Board;
import com.sparta.lv3_board.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {


    private final BoardService boardService;


    //create
    @PostMapping("/boards")
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto requestDto, HttpServletRequest httpServletRequest) {
        BoardResponseDto responseDto = boardService.createBoard(requestDto, httpServletRequest);
        if (responseDto != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }    }

    //readAll
    @GetMapping("/boards")
    public List<BoardResponseDto> getAllBoard() {
        return boardService.getBoards();
    }


    //read
    @GetMapping("/boards/{id}")
    public Board getBoard(@PathVariable Long id) {

        return boardService.getBoard(id);
    }

    //update
    @PutMapping("/boards/{id}")
    public ResponseEntity<String> updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto, HttpServletRequest httpServletRequest) {
        ResponseEntity<String> response = boardService.updateBoard(id, requestDto, httpServletRequest);
        if (response != null) {
            return response;
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();// 인증 실패
        }    }

    //delete
    @DeleteMapping("/boards/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long id,  HttpServletRequest httpServletRequest) {
        ResponseEntity<String> response = boardService.deleteBoard(id, httpServletRequest);
        if (response != null) {
            return response;
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }    }
}

