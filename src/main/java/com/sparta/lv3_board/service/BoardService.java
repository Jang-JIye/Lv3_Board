package com.sparta.lv3_board.service;



import com.sparta.lv3_board.dto.BoardRequestDto;
import com.sparta.lv3_board.dto.BoardResponseDto;
import com.sparta.lv3_board.entity.Board;
import com.sparta.lv3_board.repository.BoardRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    //create
    public BoardResponseDto createBoard(BoardRequestDto requestDto, HttpServletRequest httpServletRequest) {
        Board board = new Board(requestDto);
        Board saveBoard = boardRepository.save(board);
        return new BoardResponseDto(saveBoard);
    }


    // readAll
    public List<BoardResponseDto> getBoards() {
        return boardRepository.findAllByOrderByModifiedAtDesc().stream().map(BoardResponseDto::new).toList();

    }


    //read
    public Board getBoard(Long id) {
        Board board = findBoard(id);
        return board;
    }


    //update
    @Transactional
    public ResponseEntity<String> updateBoard(Long id, BoardRequestDto requestDto, HttpServletRequest httpServletRequest) {
        Board board = findBoard(id);
        board.update(requestDto);
        return ResponseEntity.ok("수정 성공!");
    }

    //delete
    public ResponseEntity<String> deleteBoard(Long id, HttpServletRequest httpServletRequest) {
        Board board = findBoard(id);
        boardRepository.delete(board);
        return ResponseEntity.ok("삭제 성공!");
    }


    private Board findBoard(Long id) {

        return boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("선택한 게시물은 존재하지 않습니다.")
        );
    }



}
