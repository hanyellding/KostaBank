package com.my.dao;

import com.my.exception.FindException;
import com.my.vo.Question;

import java.util.List;

public interface QuestionDAO {
    /**
     * 문제 아이디로 문제 찾기
     * @param question_id 문제 아이디
     * @return question 객체
     * @throws FindException 문제 아이디에 해당하는 문제가 없을때
     */
    Question selectById(String question_id) throws FindException;

    /**
     * 내노트에 해당하는 문제 정보 // mynote와 question 조인
     * @param user_id 세션에 있는 유저 아이디
     * @param rownum 페이징 숫자
     * @return 문제 객체
     * @throws FindException 페이징 숫자에 해당하는 문제가 없을경우
     */
    Question selectMNById(String user_id, int rownum) throws FindException;

    /**
     * 내가 푼 문제 가져오기 // solved와 questrion 조인 200개
     * @param user_id 세션에 있는 유저 아이디
     * @param n 몇개뽑을지 기본은 200
     * @return 문제객체 리스트
     * @throws FindException 존재하지 않거나 데이터가 없을때
     */
    List<Question> selectSQById(String user_id, int n) throws FindException;

    /**
     * 문제 종류 code(문제 id 앞 2자리)에 대한 문제들
     * @return 문제 객체 리스트
     * @throws FindException 존재하지 않거나 데이터가 없을때
     */
    List<Question> selectAll() throws FindException;
}
