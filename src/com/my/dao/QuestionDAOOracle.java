package com.my.dao;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.RemoveException;
import com.my.sql.MyConnection;
import com.my.vo.Question;
import com.my.vo.User;
import com.sun.deploy.uitoolkit.impl.awt.ui.SwingConsoleWindow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAOOracle implements QuestionDAO {
    @Override
    public Question selectById(String question_id) throws FindException {
        Connection con =null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByIdSQL = "SELECT * FROM question WHERE question_id = ?";
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setString(1,question_id);
            rs = pstmt.executeQuery();
            if(!rs.next()){
                throw new FindException("문제 아이디에 해당하는 값이 없습니다.");
            }
            Question question = new Question();
            question.setQuestion_id(rs.getString("question_id"));
            question.setContent(rs.getString("content"));
            question.setCorrect_answer(rs.getString("correct_answer"));
            question.setExplanation(rs.getString("explanation"));
            question.setTotal_answer_count(Integer.parseInt(rs.getString("total_answer_count")));
            question.setCorrect_answer_count(Integer.parseInt(rs.getString("correct_answer_count")));
            return question;
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new FindException(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt,rs);
        }
    }

    @Override
    public Question selectMNById(String user_id, int rownum) throws FindException {
        Connection con =null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByIdSQL = "select B.rnum, B.* , (select count(*) from my_note where user_id= ?) mn from\n" +
                "    (select rownum as rnum, A.* from (\n" +
                "        select q.* from question q join my_note mn on q.QUESTION_ID = mn.QUESTION_ID where mn.user_id = ? \n" +
                "        order by mn.mn_date desc ) A where rownum <=?) B where B.rnum >= ?";
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setString(1,user_id);
            pstmt.setString(2,user_id);
            pstmt.setInt(3,rownum);
            pstmt.setInt(4,rownum);
            rs = pstmt.executeQuery();
            if(!rs.next()){
                throw new FindException("아이디에 해당하는 값이 없습니다.");
            }
            Question question = new Question();
            question.setQuestion_id(rs.getString("question_id"));
            question.setContent(rs.getString("content"));
            question.setCorrect_answer(rs.getString("correct_answer"));
            question.setExplanation(rs.getString("explanation"));
            question.setTotal_answer_count(Integer.parseInt(rs.getString("total_answer_count")));
            question.setCorrect_answer_count(Integer.parseInt(rs.getString("correct_answer_count")));
            question.setMn_total(Integer.parseInt(rs.getString("mn")));
            return question;
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new FindException(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt,rs);
        }
    }

    @Override
    public void insertMNById(String user_id, String[] question_id_list) throws AddException {
        Connection con = null;
        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AddException("노트 추가 실패: 이유= "+ e.getMessage());
        }
        PreparedStatement pstmt = null;
        String insertSQL = "INSERT INTO my_note (user_id,question_id) VALUES (?,?)";
        try {
            for(int i =0; i<question_id_list.length;i++) {
                pstmt = con.prepareStatement(insertSQL);
                pstmt.setString(1, user_id);
                pstmt.setString(2, question_id_list[i]);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            if(e.getErrorCode() == 1){
                throw new AddException("이미 존재하는 정보 입니다.");
            }else {
                e.printStackTrace();
            }
        } finally {
            MyConnection.close(con,pstmt);
        }
    }

    @Override
    public void deleteMNById(String user_id, String question_id) throws RemoveException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            throw new RemoveException(e.getMessage());
        }
        String deleteSQL = "delete from my_note where user_id = ? and question_id = ?";
        try {
            pstmt = con.prepareStatement(deleteSQL);
            pstmt.setString(1,user_id);
            pstmt.setString(2,question_id);
            int rowcnt = pstmt.executeUpdate();
            if(rowcnt !=1){
                throw new RemoveException("삭제실패: 해당 문제가 없습니다");
            }
        } catch (SQLException e) {
            throw new RemoveException(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt);
        }
    }

    @Override
    public List<Question> selectSQById(String user_id, int n) throws FindException {
        Connection con =null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByIdSQL = "select * from (select * from question_solved qs join question q on qs.question_id=q.question_id left join my_note mn on qs.question_id = mn.question_id and mn.user_id=? where qs.user_id=? order by qs.solved_date desc) where rownum<=?";
        List<Question> all = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setString(1,user_id);
            pstmt.setString(2,user_id);
            pstmt.setInt(3,n);
            rs = pstmt.executeQuery();
            while (rs.next()){
                Question question = new Question();
                question.setQuestion_id(rs.getString("question_id"));
                question.setContent(rs.getString("content"));
                question.setCorrect_answer(rs.getString("correct_answer"));
                question.setExplanation(rs.getString("explanation"));
                question.setTotal_answer_count(Integer.parseInt(rs.getString("total_answer_count")));
                question.setCorrect_answer_count(Integer.parseInt(rs.getString("correct_answer_count")));
                question.setQuestion_ox(Integer.parseInt(rs.getString("question_ox")));
                if(rs.getString("mn_date") == null){
                    question.setMn_id("");
                }else{
                    question.setMn_id("V");
                }
                all.add(question);
            }
            if(all.size() == 0){
                throw new FindException("문제가 하나도 없습니다.");
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
    public List<Question> selectAll() throws FindException {
        Connection con =null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByIdSQL = "select * from question";
        List<Question> all = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            rs = pstmt.executeQuery();
            while (rs.next()){
                Question question = new Question();
                question.setQuestion_id(rs.getString("question_id"));
                question.setContent(rs.getString("content"));
                question.setCorrect_answer(rs.getString("correct_answer"));
                question.setExplanation(rs.getString("explanation"));
                question.setTotal_answer_count(Integer.parseInt(rs.getString("total_answer_count")));
                question.setCorrect_answer_count(Integer.parseInt(rs.getString("correct_answer_count")));
                all.add(question);
            }
            if(all.size() == 0){
                throw new FindException("문제가 하나도 없습니다.");
            }

            return all;
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new FindException(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt,rs);
        }
    }
}
