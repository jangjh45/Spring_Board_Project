package com.practice.app1.dao;

import com.practice.app1.domain.BoardDto;
import com.practice.app1.domain.SearchCondition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class BoardDaoImplTest {
    @Autowired
    private BoardDao boardDao;

    @Test
    public void searchResultCntTest() throws Exception{
        boardDao.deleteAll();
        for(int i=1; i<=20; i++){
            BoardDto boardDto = new BoardDto("title"+i, "aweowafuihaksdjfh", "asdfasdf"+i);
            boardDao.insert(boardDto);
        }
        SearchCondition sc = new SearchCondition(1, 10, "title2", "T"); // title1%
        int cnt = boardDao.searchResultCnt(sc);
        System.out.println("cnt = " + cnt);
        assertTrue(cnt==2); // 1~20, title2, title20

        sc = new SearchCondition(1, 10, "asdfasdf2", "W"); // title1%
        cnt = boardDao.searchResultCnt(sc);
        System.out.println("cnt = " + cnt);
        assertTrue(cnt==2); // 1~20, title2, title20
    }

    @Test
    public void searchSelectPageTest() throws Exception{
        boardDao.deleteAll();
        for(int i=1; i<=20; i++){
            BoardDto boardDto = new BoardDto("title"+i, "aweowafuihaksdjfh", "asdfasdf"+i);
            boardDao.insert(boardDto);
        }
        SearchCondition sc = new SearchCondition(1, 10, "title2", "T"); // title1%
        List<BoardDto> list = boardDao.searchSelectPage(sc);
        System.out.println("list = " + list);
        assertTrue(list.size()==2); // 1~20, title2, title20

        sc = new SearchCondition(1, 10, "asdfasdf2", "W"); // title1%
        list = boardDao.searchSelectPage(sc);
        System.out.println("list = " + list);
        assertTrue(list.size()==2); // 1~20, title2, title20
    }

    @Test
    public void insertTestData() throws Exception{
        boardDao.deleteAll();
        for (int i=1; i<=220; i++){
            BoardDto boardDto = new BoardDto("title"+i, "no content", "asdfasdf");
            boardDao.insert(boardDto);
        }
    }

    @Test
    public void deleteTest() throws Exception {
        boardDao.deleteAll();
        assertTrue(boardDao.count()==0);

        BoardDto boardDto = new BoardDto("no title", "no content", "asdf");
        assertTrue(boardDao.insert(boardDto)==1);
        Integer bno = boardDao.selectAll().get(0).getBno();
        assertTrue(boardDao.delete(bno, boardDto.getWriter())==1);
        assertTrue(boardDao.count()==0);

        assertTrue(boardDao.insert(boardDto)==1);
        bno = boardDao.selectAll().get(0).getBno();
        assertTrue(boardDao.delete(bno, boardDto.getWriter()+"4521")==0);
        assertTrue(boardDao.count()==1);

        assertTrue(boardDao.delete(bno, boardDto.getWriter())==1);
        assertTrue(boardDao.count()==0);

        assertTrue(boardDao.insert(boardDto)==1);
        bno = boardDao.selectAll().get(0).getBno();
        assertTrue(boardDao.delete(bno+1, boardDto.getWriter())==0);
        assertTrue(boardDao.count()==1);
    }
}