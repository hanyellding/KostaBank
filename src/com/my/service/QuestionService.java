package com.my.service;

import com.my.dao.QuestionDAO;
import com.my.dao.QuestionDAOOracle;
import com.my.dao.UserDAO;
import com.my.dao.UserDAOOracle;
import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.RemoveException;
import com.my.vo.Question;

import java.util.List;

public class QuestionService {
    private QuestionDAO dao =new QuestionDAOOracle();
    public Question findById(String question_id) throws FindException{
        return dao.selectById(question_id);
    }

    public Question mynoteById(String user_id, int row) throws FindException{
        return dao.selectMNById(user_id, row);
    }
    public void removeMnById(String user_id, String question_id) throws RemoveException{
        dao.deleteMNById(user_id, question_id);
    }

    public List<Question> solvedById(String user_id,int n) throws FindException{
        return dao.selectSQById(user_id,n);
    }
    public void addMnById(String user_id, String[] question_id_list) throws AddException{
        dao.insertMNById(user_id, question_id_list);
    }
}
