package com.basic.board.domain.board.entity;

import com.basic.board.domain.BaseDateEntity;
import com.basic.board.domain.BooleanToYNConverter;
import com.basic.board.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Board extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    //제목
    @Column(name = "title", nullable = false)
    private String title;

    //내용
    @Column(name = "contents", nullable = false)
    private String contents;

    //조회수
    @Column(name = "views", nullable = false)
    private Long views = 0L;

    //작성자
    @ManyToOne(targetEntity = Member.class, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "idx")
    private Member writer;

    //삭제 여부
    @Column(name = "delete_yn", nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean deleteYn = false;
}
