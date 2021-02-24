package com.my.dao;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.exception.RemoveException;
import com.my.vo.Feedback;
import com.my.vo.Notice;
import com.my.vo.Qa;
import com.my.vo.Report;

import java.util.List;

//feedback qa report
public interface FeedbackDAO {
    /**
     * 오른쪽 위에 있을 알림에 대한 내용물
     * new 가 1 인것만 정렬 시간순 7개? 읽으면 new -->0
     * @param user_id 세션에 있는 아이디
     * @return 피드백 객체
     * @throws FindException 아이디에 해당하는 유저가 없으면 반환
     */
    Feedback selectFeedbackById(String user_id) throws FindException;

    /**
     * 1:1문의에 대한 내용
     * @param user_id 세션에 있는 아이디
     * @param rownum 페이지 수
     * @return QA객체
     * @throws FindException 아이디에 해당하는 유저가 없으면 반환
     */
    Qa UserQaById(String user_id,int rownum) throws FindException;

    /**
     * 문의아이디로 찾는 문의 내용
     * @param qa_id 문의 아이디
     * @return 문의 상세 내용
     * @throws FindException 문의 아이디에 대한 내용이 없을 때
     */
    Qa QaByQaId(String qa_id) throws FindException;


    /**
     * 7개씩 끊어서 전체 문의사항 조회
     * status, 날짜로 정렬
     * @return 페이징 숫자에 대한 n개의 문의사항 정보
     * @throws FindException page페이징 숫자에 대한 정보가 하나도 없을때 발생
     */
    List<Qa> QaAll(int page, int n) throws FindException;

    /**
     * 새로 만든 문의사항 인서트
     * @param qa 문의사항 객체
     * @throws AddException 저장소 꽉찰경우
     */
    void QaInsert(Qa qa) throws AddException;

    /**
     * 문의사항 정보 업데이트
     * @param qa 문의사항 객체
     * @throws ModifyException 수정 실패시 예외발생
     */
    void QaUpdate(Qa qa) throws ModifyException;

    /**
     * 문의사항 삭제
     * @param qa_id 문의사항 id
     * @throws RemoveException 삭제 실패시 예외발생
     */
    void QaDelete(String qa_id) throws RemoveException;

    /**
     * 신고 아이디로 신고 상세정보 조회
     * @param report_id 신고 아이디
     * @return 신고 객체
     * @throws FindException 신고 아이디에 대한 신고 없을때
     */
    Report ReportById(String report_id) throws FindException;

    /**
     * 7개씩 끊어서 전체 신고 조회
     * status, 날짜로 정렬
     * @return 페이징 숫자에 대한 n개의 신고 정보
     * @throws FindException page페이징 숫자에 대한 정보가 하나도 없을때 발생
     */
    List<Report> ReportAll(int page, int n) throws FindException;

    /**
     * 새로 만든 신고 인서트
     * @param report 신고 객체
     * @throws AddException 저장소 꽉찰경우
     */
    void ReportInsert(Report report) throws AddException;

    /**
     * 신고 정보 업데이트
     * @param report 신고 객체
     * @throws ModifyException 수정 실패시 예외발생
     */
    void ReportUpdate(Report report) throws ModifyException;

    /**
     * 신고 삭제
     * @param report_id 신고 id
     * @throws RemoveException 삭제 실패시 예외발생
     */
    void ReportDelete(String report_id) throws RemoveException;
}