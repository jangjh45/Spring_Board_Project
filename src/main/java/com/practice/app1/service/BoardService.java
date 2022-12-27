package com.practice.app1.service;

import com.practice.app1.domain.BoardDto;
import com.practice.app1.domain.SearchCondition;

import java.util.List;
import java.util.Map;

public interface BoardService {
    int getCount() throws Exception;

    int remove(Integer bno, String writer) throws Exception;

    int write(BoardDto boardDto) throws Exception;

    List<BoardDto> getList() throws Exception;

    BoardDto read(Integer bno) throws Exception;

    List<BoardDto> getPage(Map map) throws Exception;

    int modify(BoardDto boardDto) throws Exception;

    List<BoardDto> getSearchResultPage(SearchCondition sc) throws Exception;

    int getSearchResultCnt(SearchCondition sc) throws Exception;
}
