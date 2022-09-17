package com.kh.model.dao;

import static com.kh.common.JDBCTemplate.close;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import com.kh.model.vo.Member;

public class MemberDao {
	
	private Properties prop = new Properties();
	
	// new MemberDao().xxx()를 할때마다 MemberDao객체가 생성되면서 .xml파일로부터 Properties객체로 읽여들여오는 행위를 만들것
	public MemberDao() {
		try {
			prop.loadFromXML(new FileInputStream("resources/query.xml"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public int insertMember(Connection conn, Member m) {
		// INSERT문 => 처리된 행의 갯수 => 트랜잭션처리
		// 0) 필요한 변수들 먼저 셋팅
		int result = 0; // 처리된 결과(행의 갯수)를 담아줄 변수
		
		PreparedStatement pstmt = null; // SQL문 실행 후 결과를 받기 위한 변수
		/*
		Properties prop = new Properties();
		
		try {
			prop.loadFromXML(new FileInputStream("resources/query.xml"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		*/
		// 실행할 SQL문(미완성된 형태로)
		/*
		String sql = "INSERT INTO MEMBER" + 
				" VALUES(SEQ_USERNO.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";
		*/
		String sql = prop.getProperty("insertMember");
		
		try {
			// 3_1) PreparedStatement 객체 생성(SQL문을 미리 넘겨준다)
			pstmt = conn.prepareStatement(sql);
			
			// 3_2) 미완성된 SQL문일 경우 완성시켜주기
			// pstmt.setXXX(?의 위치, 실제값)
			pstmt.setString(1, m.getUserId());
			pstmt.setString(2, m.getUserPwd());
			pstmt.setString(3, m.getUserName());
			pstmt.setString(4, m.getGender());
			pstmt.setInt(5, m.getAge());
			pstmt.setString(6, m.getEmail());
			pstmt.setString(7, m.getPhone());
			pstmt.setString(8, m.getAddress());
			pstmt.setString(9, m.getHobby());
			
			// 4, 5) DB에 완성된 SQL문을 실행 후 결과(*처리된 행의 갯수) 받기
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return result;
	}
	
	public ArrayList<Member> selectAll(Connection conn) { 
		// SELECT => ResultSet 형태 => ArrayList 반환
		
		// 0) 필요한 변수들 셋팅
		// 조회된 결과를 뽑아서 담아줄 변수 => ArrayList 생성(여러회원의 정보, 여러 행)
		ArrayList<Member> list = new ArrayList<>(); // 텅빈 리스트 생성
		
		// PreparedStatement, ResultSet
		PreparedStatement pstmt = null; // SQL문 실행 / 결과받을 변수
		ResultSet rset = null; // SELECT문이 실행된 조회결과값들이 처음 담길 객체
		
		// 실행할 SQL문
		//String sql = "SELECT * FROM MEMBER ORDER BY USERNAME DESC";
		String sql = prop.getProperty("selectAll");
		
		try {
			// 3_1 ) PreparedStatement 객체 생성(SQL문을 미리 같이 넘겨줌)
			pstmt = conn.prepareStatement(sql);
			
			// 3_2 ) 미완성된 SQL문이라면 완성시켜주기 => 스킵!
			
			// 4, 5 ) SQL문 (SELECT)을 실행 후 결과(ResultSet) 받기
			rset = pstmt.executeQuery();
			
			// 6_1 ) 현재 조회결과가 담긴 ResultSet에서 한행씩 뽑아서 VO 객체에 담기
			// rset.next() : 커서를 내린다 행이 있으면 true / 없으면 false
			while(rset.next()) {

				Member m = new Member();
				
				m.setUserId(rset.getString("USERID"));
				m.setUserPwd(rset.getString("USERPWD"));
				m.setUserName(rset.getString("USERNAME"));
				m.setGender(rset.getString("GENDER"));
				m.setAge(rset.getInt("AGE"));
				m.setEmail(rset.getString("EMAIL"));
				m.setPhone(rset.getString("PHONE"));
				m.setAddress(rset.getString("ADDRESS"));
				m.setHobby(rset.getString("HOBBY"));
				m.setUserNo(rset.getInt("USERNO"));
				m.setEnrollDate(rset.getDate("ENROLLDATE"));
				
				list.add(m);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 7) 다 쓴 JDBC용 객체 반납 (생성된 순서의 역순으로)
			close(rset);
			close(pstmt);			
		}
		
		// 8) 결과 반환(조회결과들이 모두 담겨있는 list)
		return list;
	}
	
	public ArrayList<Member> selectByUserName(Connection conn, String keyword){
		
		// 0) 필요한 변수 셋팅
		ArrayList<Member> list = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		// 실행할 SQL문(미완성)
		// SELECT * FROM MEMBER WHERE USERNAME LIKE '%?%';
		//String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE '%'||?||'%'";
		String sql = prop.getProperty("selectByUserName");
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + keyword + "%");
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				list.add(new Member(rset.getInt("USERNO")
								   ,rset.getString("USERID")
								   ,rset.getString("USERPWD")
								   ,rset.getString("USERNAME")
								   ,rset.getString("GENDER")
								   ,rset.getInt("AGE")
								   ,rset.getString("EMAIL")
								   ,rset.getString("PHONE")
								   ,rset.getString("ADDRESS")
								   ,rset.getString("HOBBY")
								   ,rset.getDate("ENROLLDATE")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		return list;
	}
							// 1, 2번은 벌써했어
	public int deleteMember(Connection conn, String userId) {
		
		// 0)
		int result = 0;
		PreparedStatement pstmt = null;
		
		// 실행할 SQL문
		// DELETE FROM MEMBER WHERE USERID = 'userId';
		String sql = prop.getProperty("deleteMember");
		//String sql = "DELETE FROM MEMBER WHERE USERID = ?";
		
		
		try {
			// 3_1
			pstmt = conn.prepareStatement(sql);
			// 3_2
			pstmt.setString(1, userId);
			// 4, 5
			result = pstmt.executeUpdate();
			
			// 6번은 서비스가서 할꺼야 ~ 
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 7번은 반만할거야 ~
			close(pstmt);
		}
		// 8
		return result;
	}
	
	public int updateMember(Connection conn, Member m) {
		
		int result = 0;
		
		PreparedStatement pstmt = null;
		
		
		/*
		String sql = "UPDATE MEMBER "
					+"SET USERPWD = ?,"
					+"EMAIL = ?,"
					+"PHONE = ?,"
					+"ADDRESS = ?"
					+"WHERE USERID = ?";
					*/
		String sql = prop.getProperty("updateMember");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, m.getUserPwd());
			pstmt.setString(2, m.getEmail());
			pstmt.setString(3, m.getPhone());
			pstmt.setString(4, m.getAddress());
			pstmt.setString(5, m.getUserId());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return result;
	}
	
	
	public Member selectByUserId(Connection conn, String userId) {
		
		Member m = null;
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		//String sql = "SELECT * FROM MEMBER WHERE USERID = ?";
		String sql = prop.getProperty("selectByUserId");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) { 
				
				m = new Member(rset.getInt("USERNO")
							 , rset.getString("USERID")
							 , rset.getString("USERPWD")
							 , rset.getString("USERNAME")
							 , rset.getString("GENDER")
							 , rset.getInt("AGE")
							 , rset.getString("EMAIL")
							 , rset.getString("PHONE")
							 , rset.getString("ADDRESS")
							 , rset.getString("HOBBY")
							 , rset.getDate("ENROLLDATE"));
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return m;
		
	}
	
	
}
