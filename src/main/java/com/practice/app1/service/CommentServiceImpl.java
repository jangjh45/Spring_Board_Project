package com.practice.app1.service;

import com.practice.app1.dao.BoardDao;
import com.practice.app1.dao.CommentDao;
import com.practice.app1.domain.CommentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
//    @Autowired
    BoardDao boardDao;
//    @Autowired
    CommentDao commentDao;

    // 생성자 주입
//    @Autowired
    public CommentServiceImpl(CommentDao commentDao, BoardDao boardDao) {
        this.commentDao = commentDao;
        this.boardDao = boardDao;
    }

    @Override
    public int getCount(Integer bno) throws Exception {
        return commentDao.count(bno);
    }

    // 댓글 삭제
    // 1. 댓글 개수를 -1 해줌 updateCommentCnt
    // 2. 댓글 개수와 댓글 삭제를 모두 성공해야하기 때문에 @Transactional 써줌
    @Override
    @Transactional(rollbackFor = Exception.class) // 예외 발생하면 롤백
    public int remove(Integer cno, Integer bno, String commenter) throws Exception {
        int rowCnt = boardDao.updateCommentCnt(bno, -1);
        System.out.println("updateCommentCnt - rowCnt = " + rowCnt);
//        throw new Exception("test");
        rowCnt = commentDao.delete(cno, commenter);
        System.out.println("rowCnt = " + rowCnt);
        return rowCnt;
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // 예외 발생하면 롤백
    public int write(CommentDto commentDto) throws Exception {
        boardDao.updateCommentCnt(commentDto.getBno(), 1);
//                throw new Exception("test");
        return commentDao.insert(commentDto);
    }

    @Override
    public List<CommentDto> getList(Integer bno) throws Exception {
//        throw new Exception("test");
        return commentDao.selectAll(bno);
    }

    @Override
    public CommentDto read(Integer cno) throws Exception {
        return commentDao.select(cno);
    }

    @Override
    public int modify(CommentDto commentDto) throws Exception {
        return commentDao.update(commentDto);
    }
}
