package com.practice.app1.dao;

import com.practice.app1.domain.BoardDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class BoardDaoImplTest {
    @Autowired
    private BoardDao boardDao;

    @Test
    public void insertTestData() throws Exception{
        boardDao.deleteAll();
        for (int i=1; i<=220; i++){
            BoardDto boardDto = new BoardDto("title"+i, "no content", "asdfasdf");
            boardDao.insert(boardDto);
        }
    }
}