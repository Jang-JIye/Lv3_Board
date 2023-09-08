package com.sparta.lv3_board.entity;

import com.sparta.lv3_board.dto.BoardRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "board")
@NoArgsConstructor
public class Board extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String contents;

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER)
    private List<Comment> comment = new ArrayList<>();



    public Board(BoardRequestDto requestDto, String username) {
//        this.username = username;
        this.username = username;
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public void update(BoardRequestDto requestDto) {
//        this.username =username;
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public void addCommentList(Comment comment) {
        this.comment.add(comment);
    }
}