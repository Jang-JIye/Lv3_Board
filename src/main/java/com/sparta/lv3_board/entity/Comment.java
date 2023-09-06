package com.sparta.lv3_board.entity;

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
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnore
    private Board board;

//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    @JsonManagedReference
//    private User user;

    public Comment(User user, CommentRequestDto commentRequestDto, Board board) {
        this.board = board;
        this.username = board.getUsername();
        this.comment = commentRequestDto.getComment();
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.comment = commentRequestDto.getComment();
    }

}