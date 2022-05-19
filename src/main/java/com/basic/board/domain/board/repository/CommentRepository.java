package com.basic.board.domain.board.repository;

import com.basic.board.domain.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByIdx(Long idx);
}
