package com.sparta.lv3_board.service;


import com.sparta.lv3_board.dto.CommentRequestDto;
import com.sparta.lv3_board.dto.CommentResponseDto;
import com.sparta.lv3_board.dto.UserResponseDto;
import com.sparta.lv3_board.entity.Board;
import com.sparta.lv3_board.entity.Comment;
import com.sparta.lv3_board.entity.User;
import com.sparta.lv3_board.entity.UserRoleEnum;
import com.sparta.lv3_board.jwt.JwtUtil;
import com.sparta.lv3_board.repository.BoardRepository;
import com.sparta.lv3_board.repository.CommentRepository;
import com.sparta.lv3_board.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // Comment 작성
    @Transactional
    public CommentResponseDto createComment(Long boardId, CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {
        User user = checkToken(httpServletRequest);

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        Comment comment = new Comment(user, commentRequestDto, board);
        Comment saveComment = commentRepository.save(comment);

        return new CommentResponseDto(saveComment);
    }

    // Comment 수정
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest)  {
        User user = checkToken(httpServletRequest);

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("수정할 댓글이 존재하지 않습니다."));

        if (comment.getUsername().equals(user.getUsername()) || user.getRole().equals(UserRoleEnum.ADMIN)) {
            comment.update(commentRequestDto);
            return new CommentResponseDto(comment);
        } else {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }
    }

    /// Comment 삭제
    public UserResponseDto deleteComment(Long commentId, HttpServletRequest httpServletRequest) {
        User user = checkToken(httpServletRequest);

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );

        if (comment.getUsername().equals(user.getUsername()) || user.getRole().equals(UserRoleEnum.ADMIN)) {
            commentRepository.delete(comment);
            return new UserResponseDto("삭제 성공", 200);
        } else {
            return new UserResponseDto("작성자만 삭제할 수 있습니다.", 400);
        }

    }

    // Token 체크
    public User checkToken(HttpServletRequest request){

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            return user;

        }
        return null;
    }
}