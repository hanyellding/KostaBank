package com.my.service;

import com.my.dao.BoardDAO;
import com.my.dao.BoardDAOOracle;
import com.my.exception.FindException;
import com.my.vo.Board;
import com.my.vo.Qa;

import java.util.List;

public class BoardService {
    private BoardDAO dao = new BoardDAOOracle();
    public Board boardById(String board_id) throws FindException {
        return dao.BoardById(board_id);
    }
    public List<Board> boardAll(int page, int num) throws FindException {
        return dao.BoardAll(page,num);
    }
}
