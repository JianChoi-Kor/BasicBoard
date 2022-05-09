package com.basic.board.domain.board.entity;

import com.basic.board.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    //게시글 고유 번호
    @Column(name = "board_idx", nullable = false)
    private Long boardIdx;

    //작성자 고유 번호
    @Column(name = "writer_idx", nullable = false)
    private Long writerIdx;
}
