package com.my.dao;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.exception.RemoveException;
import com.my.sql.MyConnection;
import com.my.vo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoardDAOOracle implements BoardDAO{
    @Override
    public Board BoardById(String board_id) throws FindException {
        Connection con =null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByIdSQL = "SELECT * FROM board b join users u on b.user_id = u.user_id WHERE board_id = ?";
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setString(1,board_id);
            rs = pstmt.executeQuery();
            if(!rs.next()){
                throw new FindException("게시판 아이디에 해당하는 값이 없습니다.");
            }
            User user = new User();
            user.setUser_nickname(rs.getString("user_nickname"));
            Board board = new Board();
            board.setUser(user);
            board.setBoard_id(rs.getString("board_id"));
            board.setBoard_subtitle(rs.getString("board_subtitle"));
            board.setBoard_title(rs.getString("board_title"));
            board.setBoard_content(rs.getString("board_wdate"));
            board.setBoard_view(rs.getInt("board_view"));
            board.setBoard_file(rs.getString("board_file"));
            return board;
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new FindException(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt,rs);
        }

    }

    @Override
    public List<Board> BoardAll(int page, int num) throws FindException {
        Connection con =null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByIdSQL = "select B.rnum, B.* , (select count(*) from qa) qa from\n" +
                "    (select rownum as rnum, A.* from (\n" +
                "        select * from board b join users u on b.user_id = u.user_id \n" +
                "        order by b.board_wdate desc ) A where rownum <=?) B where B.rnum >= ?";
        List<Board> all = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setInt(1,page*num);
            pstmt.setInt(2,(page-1)*num +1);
            rs = pstmt.executeQuery();
            while(rs.next()){
                User user = new User();
                user.setUser_nickname(rs.getString("user_nickname"));
                Board board = new Board();
                board.setUser(user);
                board.setBoard_id(rs.getString("board_id"));
                board.setBoard_subtitle(rs.getString("board_subtitle"));
                board.setBoard_title(rs.getString("board_title"));
                board.setBoard_content(rs.getString("board_wdate"));
                board.setBoard_view(rs.getInt("board_view"));
                board.setBoard_file(rs.getString("board_file"));
                board.setUser(user);
                all.add(board);
            }
            if(all.size() == 0){
                throw new FindException("정보가 하나도 없습니다.");
            }
            return all;
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new FindException(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt,rs);
        }
    }

    @Override
    public List<BoardComment> CommentAll(String board_id) throws FindException {
        return null;
    }

    @Override
    public void UpCheck(String user_id, String board_id) throws FindException {

    }

    @Override
    public void BoardInsert(Board board) throws AddException {

    }

    @Override
    public void BoardUpdate(Board board) throws ModifyException {

    }

    @Override
    public void BoardDelete(String board_id) throws RemoveException {

    }

    @Override
    public Notice NoticeById(String notice_id) throws FindException {
        return null;
    }

    @Override
    public List<Notice> NoticeAll(int page, int n) throws FindException {
        return null;
    }

    @Override
    public void NoticeInsert(Notice notice) throws AddException {

    }

    @Override
    public void NoticeUpdate(Notice notice) throws ModifyException {

    }

    @Override
    public void NoticeDelete(String notice_id) throws RemoveException {

    }
}
