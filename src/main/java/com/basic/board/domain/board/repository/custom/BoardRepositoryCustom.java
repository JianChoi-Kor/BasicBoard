package com.basic.board.domain.board.repository.custom;

import com.basic.board.domain.PageRequest;
import com.basic.board.domain.board.BoardReqDto;
import com.basic.board.domain.board.BoardResDto;
import org.springframework.data.domain.PageImpl;

public interface BoardRepositoryCustom {
    PageImpl<BoardResDto.BoardForList> getBoardList(BoardReqDto.SearchBoard searchBoard, PageRequest pageRequest);
    BoardResDto.BoardDetail getBoardDetail(Long boardIdx, Long memberIdx);
    void updateBoardViews(Long boardIdx);
}
