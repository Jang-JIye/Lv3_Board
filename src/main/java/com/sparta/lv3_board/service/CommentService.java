package com.sparta.lv3_board.service;


import com.sparta.lv3_board.dto.CommentRequestDto;
import com.sparta.lv3_board.dto.CommentResponseDto;
import com.sparta.lv3_board.entity.Board;
import com.sparta.lv3_board.entity.Comment;
import com.sparta.lv3_board.entity.User;
import com.sparta.lv3_board.entity.UserRoleEnum;
import com.sparta.lv3_board.repository.BoardRepository;
import com.sparta.lv3_board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;


    //create
    @Transactional
    public CommentResponseDto createComment(CommentRequestDto requestDto, User user) {
        //게시글 확인
        Board board = boardRepository.findById(requestDto.getBoardId()).orElseThrow(() ->
                new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Comment comment = new Comment(board, requestDto.getComment(), user);
        board.addCommentList(comment);
        log.info("댓글" + comment.getComment() + "등록");
        return new CommentResponseDto(commentRepository.save(comment));
    }


    //update
    @Transactional
    public CommentResponseDto updateComment(CommentRequestDto requestDto, User user) {
        //comment 확인
        Comment comment = findComment(requestDto.getCommentId);

        if (!user.getRole().equals(UserRoleEnum.ADMIN) && (!comment.getUser().equals(user))) {
            throw new SecurityException("수정 권한이 없습니다.");
        }
        comment.update(requestDto.getComment());
        return new CommentResponseDto(comment);
    }

    //delete
    @Transactional
    public void deleteComment(CommentRequestDto requestDto, User user) {
        Comment comment = findComment(requestDto.getCommentId);

        if (!user.getRole().equals(UserRoleEnum.ADMIN) && (!comment.getUser().equals(user))) {
            throw new SecurityException("삭제 권한이 없습니다.");
        }
        commentRepository.delete(comment);
    }

    @Transactional
    public Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("존재하지 않는 id 입니다."));
    }
}
