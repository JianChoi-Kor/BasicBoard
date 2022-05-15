package com.basic.board.domain.board.repository.impl;

import com.basic.board.domain.PageRequest;
import com.basic.board.domain.board.BoardResDto;
import com.basic.board.domain.board.repository.custom.BoardRepositoryCustom;
import org.springframework.data.domain.PageImpl;

public class BoardRepositoryImpl implements BoardRepositoryCustom {
    @Override
    public PageImpl<BoardResDto.BoardForList> getBoardList(PageRequest pageRequest) {
        return null;
    }
}
