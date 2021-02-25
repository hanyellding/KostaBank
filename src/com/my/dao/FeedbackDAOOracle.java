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

public class FeedbackDAOOracle implements FeedbackDAO {
    @Override
    public List<Feedback> selectFeedbackById(String user_id) throws Exception {
        Connection con =null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

        String selectByIdSQL = "select * from ( select report_id, report_id id, report_title, report_sol_wdate, report_new from report where report_user = ? and report_new =1 \n" +
                "union all select qa_id,-1, qa_title, qa_sol_wdate, qa_new from qa where user_id= ? and qa_new =1 order by report_sol_wdate desc) where rownum<=10";
        List<Feedback> all = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setString(1,user_id);
            pstmt.setString(2,user_id);
            rs = pstmt.executeQuery();
            int code;
            while(rs.next()){
                if(rs.getString("id").equals("-1")){
                    code = 0;
                }else{code=1;}
                Feedback f = new Feedback(code,rs.getString("report_id"),rs.getString("report_title"),rs.getString("report_sol_wdate"));

                all.add(f);
            }
            if(all.size() == 0){
                throw new Exception("정보가 하나도 없습니다.");
            }
            return all;
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new Exception(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt,rs);
        }
    }

    @Override
    public Qa UserQaById(String user_id, int rownum) throws FindException {
        Connection con =null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByIdSQL = "select B.rnum, B.* , (select count(*) from qa where user_id= ?) qa from\n" +
                "    (select rownum as rnum, A.* from (\n" +
                "        select * from qa q join users u on q.user_id = u.user_id where q.user_id = ? \n" +
                "        order by q.qa_wdate desc ) A where rownum <=?) B where B.rnum >= ?";
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setString(1,user_id);
            pstmt.setString(2,user_id);
            pstmt.setInt(3,rownum);
            pstmt.setInt(4,rownum);
            rs = pstmt.executeQuery();
            if(!rs.next()){
                throw new FindException("정보가 하나도 없습니다.");
            }

            Qa qa = new Qa(rs.getString("qa_id"),rs.getString("qa_title"), rs.getString("qa_content"), rs.getString("qa_wdate")
                    ,rs.getString("qa_file"), rs.getInt("qa_new"),rs.getInt("qa_status"),rs.getString("qa_sol_wdate"),rs.getString("qa_sol_content"),rs.getInt("qa"));
            User user = new User();
            user.setUser_nickname(rs.getString("user_nickname"));
            qa.setUser(user);
            return qa;
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new FindException(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt,rs);
        }
    }

    @Override
    public Qa QaByQaId(String qa_id) throws FindException {
        Connection con =null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByIdSQL = "SELECT * FROM qa q join users u on q.user_id = u.user_id WHERE qa_id = ?";
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setString(1,qa_id);
            rs = pstmt.executeQuery();
            if(!rs.next()){
                throw new FindException("문의 아이디에 해당하는 값이 없습니다.");
            }
            Qa qa = new Qa(rs.getString("qa_id"),rs.getString("qa_title"), rs.getString("qa_content"), rs.getString("qa_wdate")
                    ,rs.getString("qa_file"), rs.getInt("qa_new"),rs.getInt("qa_status"),rs.getString("qa_sol_wdate"),rs.getString("qa_sol_content"),1);
            User user = new User();
            user.setUser_nickname(rs.getString("user_nickname"));
            qa.setUser(user);
            return qa;
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new FindException(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt,rs);
        }
    }


    @Override
    public List<Qa> QaAll(int page, int num) throws FindException {
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
                "        select * from qa q join users u on q.user_id = u.user_id \n" +
                "        order by q.qa_wdate desc ) A where rownum <=?) B where B.rnum >= ?";
        List<Qa> all = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setInt(1,page*num);
            pstmt.setInt(2,(page-1)*num +1);
            rs = pstmt.executeQuery();
            while(rs.next()){
                Qa qa = new Qa(rs.getString("qa_id"),rs.getString("qa_title"), rs.getString("qa_content"), rs.getString("qa_wdate")
                        ,rs.getString("qa_file"), rs.getInt("qa_new"),rs.getInt("qa_status"),rs.getString("qa_sol_wdate"),rs.getString("qa_sol_content"),rs.getInt("qa"));
                User user = new User();
                user.setUser_nickname(rs.getString("user_nickname"));
                qa.setUser(user);
                all.add(qa);
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
    public void QaInsert(Qa qa) throws AddException {

    }

    @Override
    public void QaUpdate(Qa qa) throws ModifyException {

    }

    @Override
    public void QaDelete(String qa_id) throws RemoveException {

    }

    @Override
    public Report ReportById(String report_id) throws FindException {
        Connection con =null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByIdSQL = "SELECT * FROM report r join users u on r.report_user = u.user_id join question q on r.report_question_id = q.question_id WHERE report_id = ?";
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setString(1,report_id);
            rs = pstmt.executeQuery();
            if(!rs.next()){
                throw new FindException("신고 아이디에 해당하는 값이 없습니다.");
            }
            User user = new User();
            user.setUser_nickname(rs.getString("user_nickname"));
            Question question = new Question();
            question.setContent(rs.getString("content"));
            question.setCorrect_answer(rs.getString("correct_answer"));
            question.setExplanation(rs.getString("explanation"));
            Report report = new Report(rs.getString("report_id"),user,question,rs.getString("report_title"),rs.getString("report_content"), rs.getString("report_wdate"),
                                    rs.getInt("report_new"),rs.getInt("report_status"), rs.getString("report_sol_wdate"),rs.getString("report_sol_content"));
            return report;
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new FindException(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt,rs);
        }
    }

    @Override
    public List<Report> ReportAll(int page, int num) throws FindException {
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
                "        select * from report r join users u on r.report_user = u.user_id join question q on r.report_question_id = q.question_id \n" +
                "        order by r.report_wdate desc ) A where rownum <=?) B where B.rnum >= ?";
        List<Report> all = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setInt(1,page*num);
            pstmt.setInt(2,(page-1)*num +1);
            rs = pstmt.executeQuery();
            while(rs.next()){
                User user = new User();
                user.setUser_nickname(rs.getString("user_nickname"));
                Question question = new Question();
                question.setContent(rs.getString("content"));
                question.setCorrect_answer(rs.getString("correct_answer"));
                question.setExplanation(rs.getString("explanation"));
                Report report = new Report(rs.getString("report_id"),user,question,rs.getString("report_title"),rs.getString("report_content"), rs.getString("report_wdate"),
                        rs.getInt("report_new"),rs.getInt("report_status"), rs.getString("report_sol_wdate"),rs.getString("report_sol_content"));

                all.add(report);
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
    public void ReportInsert(Report report) throws AddException {

    }

    @Override
    public void ReportUpdate(Report report) throws ModifyException {

    }

    @Override
    public void ReportDelete(String report_id) throws RemoveException {

    }
}
