package com.practice.app1.service;

import com.practice.app1.dao.BoardDao;
import com.practice.app1.dao.CommentDao;
import com.practice.app1.domain.BoardDto;
import com.practice.app1.domain.CommentDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class CommentServiceImplTest {
    @Autowired
    CommentService commentService;
    @Autowired
    CommentDao commentDao;
    @Autowired
    BoardDao boardDao;

    @Test
    public void remove() throws Exception {
        // 게시물 다 지우고
        boardDao.deleteAll();
        // 새로운 게시물 넣음
        BoardDto boardDto = new BoardDto("hello", "hello", "asdf");
        // 잘 들어갔는지 확인
        assertTrue(boardDao.insert(boardDto) == 1);
        // 게시물 번호를 얻어와서 확인
        Integer bno = boardDao.selectAll().get(0).getBno();
//        String bno = boardDao.selectAll().get(0).getWriter();
        System.out.println("bno = " + bno);

        commentDao.deleteAll(bno); // 위에서 해당 게시글 번호를 얻어와서 댓글 다지움 그리고 넣음
        CommentDto commentDto = new CommentDto(bno,0,"hi","qwer");

        // 댓글 없는지 확인
        assertTrue(boardDao.select(bno).getComment_cnt() == 0);
        // 댓글 쓰기
        assertTrue(commentService.write(commentDto)==1);
        // board 에 있는 댓글 개수가 1개 있어야 한다.
        assertTrue(boardDao.select(bno).getComment_cnt() == 1);

        Integer cno = commentDao.selectAll(bno).get(0).getCno();

        // CommentServiceImpl에서 remove할때 예회 발생하면 롤백되는지 확인
        // 댓글 하나있는데 삭제 오류가 나면 board에 댓글 카운터 -1되어있으면 안됨
        // 일부러 예외를 발생시키고 Tx가 취소되는지 확인해야.
        int rowCnt = commentService.remove(cno, bno, commentDto.getCommenter());
        assertTrue(rowCnt==1);
        assertTrue(boardDao.select(bno).getComment_cnt() == 0);
    }

    // CommentServiceImpl에서 write할때 예회 발생하면 롤백되는지 확인
    @Test
    public void write() throws  Exception {
        boardDao.deleteAll();

        BoardDto boardDto = new BoardDto("hello", "hello", "asdf");
        assertTrue(boardDao.insert(boardDto) == 1);
        Integer bno = boardDao.selectAll().get(0).getBno();
        System.out.println("bno = " + bno);

        commentDao.deleteAll(bno);
        CommentDto commentDto = new CommentDto(bno,0,"hi","qwer");

        assertTrue(boardDao.select(bno).getComment_cnt() == 0);
        assertTrue(commentService.write(commentDto)==1);

        Integer cno = commentDao.selectAll(bno).get(0).getCno();
        assertTrue(boardDao.select(bno).getComment_cnt() == 1);
    }
}
