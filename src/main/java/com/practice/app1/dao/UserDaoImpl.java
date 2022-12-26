package com.practice.app1.dao;

import com.practice.app1.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    DataSource ds;

    final int FAIL = 0;

    @Override
    public int insertUser(User user) {
        int rowCnt = FAIL;

        String sql = "insert into user values (?,?,?,?,?,now())";

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);)
        {
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getPsw());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, String.valueOf(new java.sql.Date(user.getBirth().getTime())));

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return FAIL;
        }
    }
}