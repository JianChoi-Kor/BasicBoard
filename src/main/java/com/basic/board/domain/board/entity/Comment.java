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
public class Comment extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    //게시글 고유 번호
    @Column(name = "board_idx")
    private Long boardIdx;

    //상위 댓글 고유 번호
    @Column(name = "parent_comment_idx")
    private Long parentCommentIdx;

    //댓글 내용
    @Column(name = "contents", nullable = false)
    private String contents;

    //댓글 작성자
    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "idx")
    private Member writer;

    //삭제 여부
    @Column(name = "delete_yn", nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean deleteYn = false;
}
