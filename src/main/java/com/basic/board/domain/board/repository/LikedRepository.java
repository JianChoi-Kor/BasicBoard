package com.basic.board.domain.board.repository;

import com.basic.board.domain.board.entity.Liked;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedRepository extends JpaRepository<Liked, Long> {

}
