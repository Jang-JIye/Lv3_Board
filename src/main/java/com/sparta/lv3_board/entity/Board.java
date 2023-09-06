package com.sparta.lv3_board.entity;

import com.sparta.lv3_board.dto.BoardRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Board(BoardRequestDto requestDto, String username) {
        this.username = username;
        this.username = username;
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public void update(BoardRequestDto requestDto) {
//        this.username =username;
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }
}