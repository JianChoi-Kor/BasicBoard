package com.basic.board.domain.board.repository;

import com.basic.board.domain.board.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

}
