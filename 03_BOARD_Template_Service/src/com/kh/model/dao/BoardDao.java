package com.kh.model.dao;

import static com.kh.common.JDBCTemplate.close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.kh.model.vo.Board;

public class BoardDao {
	
	public ArrayList<Board> selectAll(Connection conn){
		
		ArrayList<Board> list = new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String sql = "SELECT BNO, TITLE, CREATE_DATE, USERID "
				      +"FROM BOARD, MEMBER "
				     +"WHERE WRITER= USERNO"
				       +" AND DELETE_YN = 'N'"
				     +" ORDER BY CREATE_DATE DESC";
		
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				list.add(new Board(rset.getInt("BNO"),
								   rset.getString("TITLE"),
								   rset.getDate("CREATE_DATE"),
								   rset.getString("USERID")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		return list;
	}
	
	public Board selectBoard(Connection conn, int boardNo) {
		
		Board b = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String sql = "SELECT BNO, TITLE, CONTENT, CREATE_DATE, USERID "
				    +"FROM BOARD, MEMBER "
				    +"WHERE WRITER = USERNO "
				    +"AND DELETE_YN = 'N' "
				    +"AND BNO = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				b = new Board();
				
				b.setBno(rset.getInt("BNO"));
				b.setTitle(rset.getString("TITLE"));
				b.setContent(rset.getString("CONTENT"));
				b.setCreateDate(rset.getDate("CREATE_DATE"));
				b.setWriter(rset.getString("USERID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		return b;
	}
	
	
	public int insertBoard(Connection conn, Board b) {
		
		int result = 0;
		
		PreparedStatement pstmt = null;
		
		String sql = "INSERT INTO BOARD VALUES(SEQ_BOARD.NEXTVAL, ?, ?, DEFAULT, ?, DEFAULT)";
		// INSERT INTO BOARD VALUES(SEQ_BOARD.NEXTVAL, ?, ?, DEFAULT, ?, DEFAULT);
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, b.getTitle());
			pstmt.setString(2, b.getContent());
			pstmt.setInt(3, Integer.parseInt(b.getWriter()));
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return result;
	}
	
	
	public int selectUser(Connection conn, String userNo) {
		
		int flag = 0;
		PreparedStatement pstmt = null;
		
		String sql = "SELECT * FROM MEMBER WHERE USERNO = ?";
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(userNo));
			flag = pstmt.executeQuery().next() ? 1 : 0;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return flag;
	}
	
	
	public int deleteBoard(Connection conn, int boardNo) {
		
		int result = 0;
		
		PreparedStatement pstmt = null;
		
		String sql = "UPDATE BOARD SET DELETE_YN = 'Y' WHERE BNO = ? AND DELETE_YN = 'N'";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return result;
	}
	
	

}
