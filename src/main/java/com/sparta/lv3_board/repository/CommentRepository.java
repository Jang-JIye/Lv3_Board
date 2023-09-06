package com.sparta.lv3_board.repository;

import com.sparta.lv3_board.entity.Board;
import com.sparta.lv3_board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void delete(Comment comment);
}
