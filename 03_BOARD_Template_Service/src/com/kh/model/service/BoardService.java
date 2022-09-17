package com.kh.model.service;

import static com.kh.common.JDBCTemplate.getConnection;

import java.sql.Connection;
import java.util.ArrayList;

import com.kh.model.dao.BoardDao;
import com.kh.model.vo.Board;
import static com.kh.common.JDBCTemplate.*;

public class BoardService {
	
	public ArrayList<Board> selectAll() {
		
		Connection conn = getConnection();
		
		ArrayList<Board> list = new BoardDao().selectAll(conn);
		
		close(conn);
		
		return list;
	}
	
	public Board selectBoard(int boardNo) {
		
		Connection conn = getConnection();
		
		Board b = new BoardDao().selectBoard(conn, boardNo);
		
		close(conn);
		
		return b;
	}
	
	public int insertBoard(Board b) {
		
		Connection conn = getConnection();
		
		int result = new BoardDao().insertBoard(conn, b);
		
		if(result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		
		close(conn);
		
		return result;
	}
	
	public int selectUser(String userNo) {
		
		Connection conn = getConnection();
		int flag = new BoardDao().selectUser(conn, userNo);
		close(conn);
		return flag;
		
	}
	
	public int deleteBoard(int boardNo) {
		
		Connection conn = getConnection();
		int result = new BoardDao().deleteBoard(conn, boardNo);
		
		if(result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);
		return result;
	}
	

}
