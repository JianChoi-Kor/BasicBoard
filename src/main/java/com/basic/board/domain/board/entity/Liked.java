package com.basic.board.domain.board.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Liked {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    //게시글 고유 번호
    @Column(name = "comment_idx", nullable = false)
    private Long commentIdx;

    //작성자 고유 번호
    @Column(name = "member_idx", nullable = false)
    private Long memberIdx;
}
