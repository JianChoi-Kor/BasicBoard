package com.basic.board.domain.board.repository;

import com.basic.board.domain.board.entity.Board;
import com.basic.board.domain.board.repository.custom.BoardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

}
