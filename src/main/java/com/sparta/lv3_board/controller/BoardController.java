package com.sparta.lv3_board.controller;

import com.sparta.lv3_board.dto.BoardRequestDto;
import com.sparta.lv3_board.dto.BoardResponseDto;
import com.sparta.lv3_board.entity.Board;
import com.sparta.lv3_board.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto, HttpServletRequest httpServletRequest) {
        return boardService.createBoard(requestDto, httpServletRequest);
    }

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
        return boardService.updateBoard(id, requestDto, httpServletRequest);
        //수정 시 비밀번호 확인
    }

    //delete
    @DeleteMapping("/boards/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        return boardService.deleteBoard(id, httpServletRequest);
    }
}

