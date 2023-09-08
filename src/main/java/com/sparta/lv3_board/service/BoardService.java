package com.sparta.lv3_board.service;


import com.sparta.lv3_board.dto.BoardRequestDto;
import com.sparta.lv3_board.dto.BoardResponseDto;
import com.sparta.lv3_board.entity.Board;
import com.sparta.lv3_board.entity.User;
import com.sparta.lv3_board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    // create
    public BoardResponseDto createBoard(BoardRequestDto requestDto, User user) {
        Board board = new Board(requestDto, user.getUsername());
        Board saveBoard = boardRepository.save(board);
        return new BoardResponseDto(saveBoard);
    }

    // readAll
    public List<BoardResponseDto> getAllBoards() {
        return boardRepository.findAllByOrderByModifiedAtDesc().stream().map(BoardResponseDto::new).collect(Collectors.toList());
    }

    // read
    public BoardResponseDto getBoard(Long id) {
        return new BoardResponseDto(findBoard(id));
    }

    // update
    @Transactional
    public ResponseEntity<String> updateBoard(Long id, BoardRequestDto requestDto, User user) {
        Board board = findBoard(id);

        if (!board.getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("작성자가 다릅니다.");
        }
        board.update(requestDto);

        return ResponseEntity.ok("수정 성공!");
    }

    // delete
    public ResponseEntity<String> deleteBoard(Long id, User user) {
        Board board = findBoard(id);

        if (!board.getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("작성자가 다릅니다.");
        }
        boardRepository.delete(board);

        return ResponseEntity.ok("삭제 성공!");
    }

    private Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("선택한 게시물은 존재하지 않습니다.")
        );
    }
}
