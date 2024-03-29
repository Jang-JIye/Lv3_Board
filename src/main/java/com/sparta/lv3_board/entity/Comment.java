package com.sparta.lv3_board.entity;

import ch.qos.logback.core.net.SMTPAppenderBase;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.lv3_board.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private String username;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    @JsonIgnore
    private Board board;

    public Comment(String username, CommentRequestDto commentRequestDto, Board board) {
        this.board = board;
        this.username = username;
        this.comment = commentRequestDto.getComment();
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.comment = commentRequestDto.getComment();
    }

}