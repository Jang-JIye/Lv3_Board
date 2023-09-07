package com.sparta.lv3_board.service;


import com.sparta.lv3_board.dto.BoardRequestDto;
import com.sparta.lv3_board.dto.BoardResponseDto;
import com.sparta.lv3_board.entity.Board;
import com.sparta.lv3_board.jwt.JwtUtil;
import com.sparta.lv3_board.repository.BoardRepository;
import com.sparta.lv3_board.repository.UserRepository;
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
//
//    private final BoardRepository boardRepository;
//    private final UserRepository userRepository;
//    private final JwtUtil jwtUtil;
//
//    //create
//    public BoardResponseDto createBoard(BoardRequestDto requestDto, HttpServletRequest httpServletRequest) {
//        // 현재 사용자 정보 가지고 오기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//
//        String token = jwtUtil.resolveToken(httpServletRequest);
//        Claims claims;
//        // 토큰이 있는 경우에만 글 작성 가능
//        if (token != null) {
//            if (jwtUtil.validateToken(token)) {
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                throw new IllegalArgumentException("토큰 오류");
//            }
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                    () -> new IllegalArgumentException("로그인 하세요."));
//
//            Board board = new Board(requestDto, user.getUsername());
//            Board saveBoard = boardRepository.save(board);
//
//            return new BoardResponseDto(saveBoard);
//        }
//        return null;
//    }
//
//
//    // readAll
//    public List<BoardResponseDto> getBoards() {
//        return boardRepository.findAllByOrderByModifiedAtDesc().stream().map(BoardResponseDto::new).toList();
//
//    }
//
//
//    //read
//    public Board getBoard(Long id) {
//        Board board = findBoard(id);
//
//        return board;
//    }
//
//
//    //update
//    @Transactional
//    public ResponseEntity<String> updateBoard(Long id, BoardRequestDto requestDto, HttpServletRequest httpServletRequest) {
//        Board board = findBoard(id);
//
//        String token = jwtUtil.resolveToken(httpServletRequest);
//        Claims claims;
//        // 토큰이 있는 경우에만 글 작성 가능
//        if (token != null) {
//            if (jwtUtil.validateToken(token)) {
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                throw new IllegalArgumentException("토큰 오류");
//            }
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                    () -> new IllegalArgumentException("로그인 하세요."));
//
//
//            if (user.getUsername().equals(board.getUsername())) {
//                board.update(requestDto);
//                return ResponseEntity.ok("수정 성공!");
//            } else {
//                throw new IllegalArgumentException("작성자가 다릅니다.");
//            }
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
//    }
//
//
//    //delete
//    public ResponseEntity<String> deleteBoard(Long id, HttpServletRequest httpServletRequest) {
//        Board board = findBoard(id);
//
//        String token = jwtUtil.resolveToken(httpServletRequest);
//        Claims claims;
//
//        if (token != null) {
//            if (jwtUtil.validateToken(token)) {
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                throw new IllegalArgumentException("토큰 오류");
//            }
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                    () -> new IllegalArgumentException("로그인 하세요."));
//
//            if (user.getUsername().equals(board.getUsername())) {
//                boardRepository.delete(board);
//                return ResponseEntity.ok("삭제 성공!");
//            } else {
//                throw new IllegalArgumentException("작성자가 다릅니다.");
//            }
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
//    }
//
//
//    private Board findBoard(Long id) {
//
//        return boardRepository.findById(id).orElseThrow(
//                () -> new IllegalArgumentException("선택한 게시물은 존재하지 않습니다.")
//        );
//    }

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // create
    public BoardResponseDto createBoard(BoardRequestDto requestDto, String username) {
        Board board = new Board(requestDto, username);
        Board saveBoard = boardRepository.save(board);
        return new BoardResponseDto(saveBoard);
    }

    // readAll
    public List<BoardResponseDto> getAllBoards() {
        return boardRepository.findAllByOrderByModifiedAtDesc().stream().map(BoardResponseDto::new).collect(Collectors.toList());
    }

    // read
    public BoardResponseDto getBoard(Long id, String username) {
        return new BoardResponseDto(findBoard(id));
    }

    // update
    @Transactional
    public ResponseEntity<String> updateBoard(Long id, BoardRequestDto requestDto, String username) {
        Board board = findBoard(id);

        if (username.equals(board.getUsername())) {
            board.update(requestDto);
            return ResponseEntity.ok("수정 성공!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("작성자가 다릅니다.");
        }
    }

    // delete
    public ResponseEntity<String> deleteBoard(Long id, String username) {
        Board board = findBoard(id);

        if (username.equals(board.getUsername())) {
            boardRepository.delete(board);
            return ResponseEntity.ok("삭제 성공!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("작성자가 다릅니다.");
        }
    }

    private Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("선택한 게시물은 존재하지 않습니다.")
        );
    }
}
