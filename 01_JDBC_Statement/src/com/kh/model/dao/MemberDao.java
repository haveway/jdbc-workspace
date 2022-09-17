package com.kh.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.kh.model.vo.Member;

/*
 * DAO (Data Access Object)
 * 
 * 데이터베이스 관련된 작업(CRUD)을 전문적으로 담당하는 객체
 * DAO안에 모든 메소드를 데이터베이스와 관련된 작업으로 만든다.
 * 
 * CRUD - CREATE, RETRIEVE(READ), UPDATE, DELETE(DESTROY)
 * SQL DML
 *     SELECT
 * 
 * 
 * Controller를 통해서 호출된 기능을 수행
 * DB에 직접적으로 접근한 후 해당 SQL문을 실행 및 결과 받기(JDBC)
 */

public class MemberDao {
	
	/*
	 * * JDBC용 객체
	 * - Connection : DB의 연결정보를 담고 있는 객체
	 * - (Prepared)Statement : 해당 DB에 SQL문을 전달하고 실행한 후 결과를 받아내는 객체
	 * - ResultSet : 만일 실행한 SQL문이 SELECT문일 경우 조회된 결과들이 담겨있는 객체
	 * 
	 **** Statement 특징 : 완성된 SQL문을 실행할 수 있는 객체
	 *
	 ** JDBC 처리 순서
	 *
	 * 1) JDBC Driver 등록 : 해당 DBMS가 제공하는 클래스 등록
	 * 2) Connection 생성 : 접속하고자하는 DB정보를 입려갷서 DB에 접속하면서 생성
	 * 3) Statement 생성 : Connection 객체를 이용해서 생성
	 * 4) SQL문을 전달하면서 실행 : Statement객체를 이용해서 SQL문 실행
	 * 	> SELECT문일 경우 - executeQuery() 호출
	 * 	> DML문일 경우 - executeUpdate() 호출
	 * 5) 결과받기
	 * 	> SELECT문일 경우 - ResultSet객체(조회된 데이터들이 담겨있음)로 받기 => 6_1
	 * 	> DML문일 경우 - int(처리된 행 수)로 받기 => 6_2
	 * 6_1) ResultSet에 담겨있는 데이터들을 하나하나씩 뽑아서 VO객체에 담기
	 * 6_2) 트랜잭션 처리(성공이면 COMMIT, 실패면 ROLLBACK)
	 * 7) 다 쓴 JDBC용 객체들을 반드시 자원반납(close()) => 생성된 역순으로
	 * 8) 결과반환(Controller)
	 * 	> SELECT문일 경우 - 6_1) 만들어진 결과
	 *  > DML문일 경우 - int(처리된 행 수)
	 */
	
	public int insertMember(Member m) {
		// INSERT => 처리된 행 수 => 트랜잭션 처리
		
		// 필요한 변수들 먼저 셋팅
		int result = 0;// 처리된 결과(행 수)를 담아줄 변수
		Connection conn = null;// 접속된 DB의 연결정보를 담는 변수
		Statement stmt = null;// SQL문 실행 후 결과를 받기 위한 변수
		
		// 실행할 SQL문(완성된 형태로 만들어 주어야 함)
		// INSERT INTO MEMBER
		// VALUES(SEQ_USERNO.NEXTVAL, 'xxx', 'xxxx', 'xxxx',
		//		  'xxxx', 'xx', 'x', 'xxxx', 'xxxx', 'xx', SYSDATE);
		String sql = 
		"INSERT INTO MEMBER VALUES(SEQ_USERNO.NEXTVAL, " 
								+ "'" + m.getUserId() + "',"
								+ "'" + m.getUserPwd() + "'," 
								+ "'" + m.getUserName() + "',"
								+ "'" + m.getGender() + "'," 
								      + m.getAge() + "," 
								+ "'" + m.getEmail() + "'," 
								+ "'" + m.getPhone() + "'," 
								+ "'" + m.getAddress() + "'," 
								+ "'" + m.getHobby() + "', SYSDATE)";
		
		//System.out.println(sql);
		
		
		try {
			// 1) JDBC Driver 등록!
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// ojdbc6.jar가 누락되었거나, 잘 추가되었지만 오타가 있을경우
			// => ClassNotFoundException 발생!
			
			// 2) Connection 객체 생성(DB와 연결 -> url, 계정명, 비번)
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe"
				        					   , "JDBC"
						        			   , "JDBC");
			
			// 3) Statement객체 생성
			stmt = conn.createStatement();
			
			// 4, 5) DB에 완성된 SQL문을 전달하면서 실행 후 결과
			result = stmt.executeUpdate(sql);
			
			// 6_2) 트랜잭션 처리
			if(result > 0) { // 성공했을 경우
				conn.commit();
			} else { // 실패했을 경우 
				conn.rollback();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			try {
				// 7) 다쓴 JDBC용 객체 자원 반납 => 생성된 역순으로 close()
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		// 8) 결과반환
		return result; // 처리된 행의 수
	}
	
	public ArrayList<Member> selectAll() {
		// 0) 필요한 변수들 셋팅
		// 조회된 결과를 뽑아서 담아줄 변수 => ArrayList 생성(여러 회원의 정보, 여러 행)
		ArrayList<Member> list = new ArrayList<>(); // 텅빈리스트
		
		// Connection, Statement, ResultSet
		Connection conn = null; // 접속된 DB 연결정보를 담는 변수
		Statement stmt = null; // SQL문 실행 후 결과를 받기위한 변수
		ResultSet rset = null; // SELECT문이 실행된 조회 결과값들이 처음에 실질적으로 담길 객체
		
		// 실행할 SQL문(완성된 형태로) 
		String sql = "SELECT * FROM MEMBER";
		
		try {
			// 1) JDBC Driver 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) Connection 객체 생성
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe"
												,"JDBC","JDBC");
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4, 5) SQL문(SELECT)을 전달해서 실행 후 결과(ResultSet) 받기
			rset = stmt.executeQuery(sql);
			
			// 6_1) 현재 조회 결과가 담긴 ResultSet에서 한행씩 뽑아서 VO객체에 담기
			// rset.next()
			// 커서를 한 줄 아래로 옮겨주고 해당 행이 존재할경우 true / 아니면 false 반환
			while(rset.next()) {
				
				// 현재 rset의 커서가 가리키고 있는 해당 행의 데이터를
				// 하나씩 뽑아서 담아줄 Member객체를 만든다.
				Member m = new Member();
				
				// rset으로부터 어떤 컬럼에 해당하는 값을 뽑을건지 제시
				// rset.getInt(컬럼명 또는 컬럼순번) : int형 값 뽑아낼 때
				// rset.getString(컬럼명 또는 컬럼순번) : String형 값 뽑아낼 때
				// rset.getDate(컬럼명 또는 컬럼순번) : Date형 값 뽑아낼 때
				// => 컬럼명(대소문자를 가리지 않음), 컬럼순번
				// => 권장사항 : 컬럼명으로 적고, 대문자로 쓰는거 권장
				
				m.setUserNo(rset.getInt("USERNO"));
				m.setUserId(rset.getString("USERID"));
				m.setUserPwd(rset.getString("USERPWD"));
				m.setUserName(rset.getString("USERNAME"));
				m.setGender(rset.getString("GENDER"));
				m.setAge(rset.getInt("AGE"));
				m.setEmail(rset.getString("EMAIL"));
				m.setPhone(rset.getString("PHONE"));
				m.setAddress(rset.getString("ADDRESS"));
				m.setHobby(rset.getString("Hobby"));
				m.setEnrollDate(rset.getDate("ENROLLDATE"));
				// 한 행에 대한 모든 컬럼의 데이터값들을
				// 각각의 필드에 담아 하나의 MEMBER객체에 옮겨담기 끝!
				
				// 리스트에 해당 Member객체를 담아주면 끝!
				list.add(m);
				
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				// 7) 다 쓴 JDBC용 객체 반납(생성된 순서의 역순으로)
				rset.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		// 8) 결과 반환 ( 조회 결과들이 뽑혀서 모두 담겨있는 list )
		return list;
	}
	
	public Member selectByUserId(String userId) {
		
		// 0) 필요한 변수들 셋팅
		// 조회된 한 회원에 대한 정보를 담을 변수
		Member m = null;
		
		// JDBC관련 객체들 선언
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		
		// 실행할 SQL문(완성된 형태)
		String sql = "SELECT * FROM MEMBER WHERE USERID = '" + userId  + "'"; 
		
		try {
			// 1) JDBC Driver 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) Connection 객체 생성
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe",
												"JDBC", "JDBC");
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4, 5) SQL문(SELECT)을 전달해서 실행 후 결과 받기
			rset = stmt.executeQuery(sql);
			
			// 6_1 ) 현재 조회 결과가 담긴 ResultSet에서 
			// VO객체에 옮겨담기 => id검색이기 때문에 한행만 조회가 될것이다!
			if(rset.next()) { // 커서를 한 행 움직여보고 조회결과가 있으면 true / 없다면 false
				
				// 조회된 한 행에 대한 모든 열에 데이터값들을 뽑아서 하나의 Member객체에 담기
				m = new Member(rset.getInt("USERNO")
						      ,rset.getString("USERID")
						      ,rset.getString("USERPWD")
						      ,rset.getString("USERNAME")
						      ,rset.getString("GENDER")
						      ,rset.getInt("AGE")
						      ,rset.getString("EMAIL")
						      ,rset.getString("PHONE")
						      ,rset.getString("ADDRESS")
						      ,rset.getString("HOBBY")
						      ,rset.getDate("ENROLLDATE"));
			} 
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				// 7) 다 쓴 JDBC용 객체를 반납(생성된 순서의 역순으로)
				rset.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		// 8 ) 결과값 반환
		return m;
	}
	
	public ArrayList<Member> selectByUserName(String keyword){
		
		// 0) 필요한 변수들 셋팅
		ArrayList<Member> list = new ArrayList();
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		
		// 실행할 SQL문(완성된 형태)
		// SELECT * FROM MEMBER WHERE USERNAME LIKE '%keyword%' 
		
		String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE '%" + keyword + "%'";
		
		//System.out.println(sql);
		
		
		try {
			// 1)
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2)
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe"
												, "JDBC", "JDBC");
			
			// 3)
			stmt = conn.createStatement();
			
			// 4, 5)
			rset = stmt.executeQuery(sql);
			
			// 6_1)
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
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				// 7)
				rset.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 8)
		return list;
	}
	
	public int updateMember(Member m) {
		// UPDATE문 => 처리된 행의 갯수(int) => 트랜잭션 처리
		
		// 0)
		int result = 0;
		
		Connection conn = null;
		Statement stmt = null;
		
		// 실행할 SQL문(완성된 형태로)
		// UPDATE MEMBER
		//    SET USERPWD = '사용자가 View에서 입력한 비밀번호',
		//        EMAIL = '사용자가 View에서 입력한 이메일',
		//        PHONE = '사용자가 View에서 입력한 핸드폰번호',
		//        ADDRESS = '사용자가 View에서 입력한 주소'
		//  WHERE USERID = '사용자가 View에서 입력한 아이디';
		
		String sql = 
					 "UPDATE MEMBER "
					 +  "SET USERPWD = '" + m.getUserPwd() + "',"
					 +      "EMAIL = '" + m.getEmail() + "',"
					 +      "PHONE = '" + m.getPhone() + "',"
					 +      "ADDRESS = '" + m.getAddress() + "'"
					 +"WHERE USERID = '" + m.getUserId() + "'";
		System.out.println(sql);
		
		
		try {
			// 1)
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2)
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","JDBC","JDBC");
			
			// 3)
			stmt = conn.createStatement();
			
			// 4, 5)
			result = stmt.executeUpdate(sql);
			
			// 6_2) 트랜잭션처리
			if(result > 0) { // DML처리가 된 row가 1개 이상 있어
				conn.commit();
			} else { // 없어 그런거
				conn.rollback();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				// 7)
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 8)
		return result;
	}
	
	
	
	
	// 회원 탈퇴 요청시 실행할 SQL문
	// DELETE FROM MEMBER
	//  WHERE USERID = 'xxxx';
	
	public int deleteMember(String userId) {
		// delete문 - 처리된 행 수 - 트랜잭션 처리
		
		// 0)
		int result = 0;
		
		Connection conn = null;
		Statement stmt = null;
		
		// 실행할 SQL문
		// DELETE FROM MEMBER WHERE USERID = 'userId';
		String sql = "DELETE FROM MEMBER WHERE USERID = '" + userId + "'";
		
		
		
		try {
			// 1 ~ 7 )
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC","JDBC");
			stmt = conn.createStatement();
			result = stmt.executeUpdate(sql);
			
			if(result > 0) conn.commit();
			else conn.rollback();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 8)
		return result;
	}
	

	
	
	
	
	
	
	
	
	
	
	
}
