package com.kh.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	 *
	 * JDBC 용 객체
	 * - Connection : DB의 연결정보를 담고 있는 객체(ip주소, port번호, 계정명, 비밀번호)
	 * - (Prepared)Statement : 해당 DB에 SQL문을 전달하고 실행한 후 결과를 받아내는 객체
	 * - ResultSet : 만일 실행한 SQL문이 SELECT문일 경우 조회된 결과들이 담겨있는 객체
	 * 
	 * ** PreparedStatement의 특징 : SQL문을 바로 실행하지 않고 잠시 보관하는 개념
	 * 							  미완성된 SQL문을 먼저 전달하고 실행하기전에 완성 형태로 만든 후 실행만 하면됨
	 * 				미완성된 SQL문 만들기 사용자가 입력한 값들이 들어갈 수 있는 공간을 ?(위치홀더)로 확보
	 * 				각 위치홀더에 맞는 값들을 셋팅
	 * 
	 * 
	 * ** Statement(부모)와 PreparedStatement(자식)관계
	 * 
	 * 차이점 
	 * 1) Statement는 완성된 SQL문, PreparedStatement는 미완성된 SQL문
	 * 
	 * 2) Statement 객체 생성 시		    stmt = conn.createStatement();
	 *    PreparedStatement 객체 생성 시     pstmt = conn.prepareStatement(sql);
	 * 
	 * 3) Statement로 SQL문 실행 시			결과 = stmt.executeXXX(sql);
	 * 	  PreparedStatement로 SQL문 실행 시
	 *    ?로 빈공간을 실제값으로 채워준 뒤 실행한다.
	 * 									pstmt.setString(?의 위치, 실제값);
	 * 									pstmt.setInt(?의 위치, 실제값);
	 * 									결과 = pstmt.executeXXX();
	 * 
	 * 
	 * 
	 *** JDBC 처리 순서
	 * 1) JDBC Driver 등록 : 해당 DBMS가 제공하는 클래스 등록
	 * 2) Connection 객체 생성 : 접속하고자 하는 DB의 정보를 입력해서 DB에 접속하면서 생성(url, 계정, 비번)
	 * 3_1) PreparedStatement 객체 생성 : Connection객체를 이용해서 생성(미완성된 SQL문을 담아서)
	 * 3_2) 현재 미완성된 SQL문을 완성형태로 채우기
	 * 			-> 미완성된 경우에만 해당 / 완성된 경우에는 생략 가능
	 * 4) SQL문 실행 : executeXXX() = > sql매개변수 없음!
	 * 				> SELECT문의 경우 : executeQuery()
	 * 				> 나머지 DML문의 경우 : executeUpdate()
	 * 5) 결과 받기 : 
	 * 				> SELECT문의 경우 : ResultSet객체(조회된 데이터들이 담겨있음)로 받기 => 6_1)
	 * 				> 나머지 DML문의 경우 : int형(처리된 행의 갯수)으로 받기 => 6_2)
	 * 6_1) ResultSet에 담겨있는 데이터들을 하나씩 뽑아서 VO객체에 담기(많으면 ArrayList로 관리)
	 * 6_2) 트랜잭션처리(성공이면 COMMIT, 실패면 ROLLBACK)
	 * 7) 다 쓴 JDBC용 객체들은 반드시 자원 반납(close()) => 생성된 순서의 역순으로!! 
	 * 8) 결과반환(Controller로)
	 * 				> SELECT문의 경우 6_1) 에서 만들어진 결과
	 * 				> 나머지 DML문의 경우 처리된 행의 갯수
	 */
	
	public int insertMember(Member m) {
		// INSERT문 => 처리된 행의 갯수 => 트랜잭션 처리
		
		// 0) 필요한 변수들 먼저 셋팅
		int result = 0; // 처리된 결과(처리된 행의 갯수)를 담아줄 변수
		Connection conn = null;// 접속된 DB의 연결정보를 담는 변수
		PreparedStatement pstmt = null;// SQL문 실행 후 결과를 받기위한 변수
		
		// + 필요한 변수 : 실행할 SQL문
		// INSERT INTO MEMBER
		// VALUES(SEQ_USERNO.NEXTVAL, 'xxx', 'xxx', 'xxx', 'xxx', x, 'xxx@xxx',
		//			'xxx', 'xxx', 'xxx', DEFAULT)
		// + "'" + 머시기 + "',"
		
		String sql = "INSERT INTO MEMBER"
					+ " VALUES(SEQ_USERNO.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, DEFAULT)";
		
		try {
			// 1) JDBC 드라이버 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) Connection 객체 생성(== DB와 연결시키겠다. --> url, 계정명, 비밀번호)
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","JDBC","JDBC");
			
			// 3_1) PreparedStatemet 객체 생성(SQL문을 미리 넘겨준다.)
			pstmt = conn.prepareStatement(sql);
			
			// 3_2) 미완성된 SQL문일 경우 완성시켜주기
			//	pstmt.setXXX(?의 위치, 실제값);
			// pstmt.setString(홀더순번, 대체할값) => '대체할값'(양옆에 홑따옴표 감싸준 상태로 알아서 들어감)
			// pstmt.setInt(홀더순번, 대체할값) = > 대체할값 (그냥 지알아서 들어감)
			pstmt.setString(1, m.getUserId());
			pstmt.setString(2, m.getUserPwd());
			pstmt.setString(3, m.getUserName());
			pstmt.setString(4, m.getGender());
			pstmt.setInt(5, m.getAge());
			pstmt.setString(6, m.getEmail());
			pstmt.setString(7, m.getPhone());
			pstmt.setString(8, m.getAddress());
			pstmt.setString(9, m.getHobby());
			
			// PreparedStatement의 단점
			// => 완성된 SQL문을 볼 수 없다.
			
			// 4, 5) DB에 완성된 SQL문을 실행 후 결과(처리된 행 갯수)받기
			result = pstmt.executeUpdate();
			
			// 6_2) 트랜잭션 처리
			/*
			if(result > 0) { // 1개 이상의 행이 INSERT 되었다면 == 성공했을 경우 => 커밋
				conn.commit();
			} else { // 실패했을 경우 => 롤백
				conn.rollback();
			}
			*/
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} /*finally {
			try {
				// 7) 다 쓴 JDBC용 객체 자원 반납 = > 생성된 순서의 역순으로 close()
				/*
				pstmt.close();
				conn.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}*/
		
		
		if(result > 0) {
			try {
				if(conn != null && !conn.isClosed()) {
					conn.commit();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				if(conn != null && !conn.isClosed()) {
					conn.rollback();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		try {
			if(pstmt != null && !pstmt.isClosed()) {
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		try {
			if(conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// 8) 컨트롤러에게 처리된 행의 갯수 반환
		return result;
	}
	
	public ArrayList<Member> selectAll(){
		ArrayList<Member> list = new ArrayList();
		// 멤버만 들어갈 수 있는 리스트를 만들고 현재 텅 빈 리스트
		
		// 처음에 실질적으로 담길 객체
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String sql = "SELECT * FROM MEMBER";
		
		try {
			// 1) JDBC Driver 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) Connection 객체 생성
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			
			// 3) PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4,5) SQL(SELECT)를 실행 후 결과(ResultSet) 받기
			rset = pstmt.executeQuery();
			
			// 6) 현재 조회결과가 담긴 ResultSEt에서 한행씩 뽑아내서 VO객체에 담기
			// rset.next() : 커서를 한줄 아래로 옮겨주고 해당 행이 존재할경우 true /아니면 false반환
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
				// 7) 다 쓴 JDBC용 객체 반납(생성된 순서의 역순으로)
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 8) 결과 반환 (올때 어레이리스트했음)
		return list;
	}
	
	public ArrayList<Member> selectByUserName(String keyword){
		
		// 0) 필요한 변수들 셋팅
		ArrayList<Member> list = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		// 실행할 SQL문 (미완성된)
		// SELECT * FROM MEMBER WHERE USERNAME LIKE '%xxx%';
		
		//String sql = "SELECT * FROM MEMBER" +
		//					" WHERE USERNAME LIKE '%?%'";
		// '%'홍길동'%' => 정상수행 안됨
		
		// 방법 1)
		String sql =
		"SELECT * FROM MEMBER WHERE USERNAME LIKE '%'||?||'%'";
		// '%keyword%'
		// 방법 2)
		// String sql =
		// "SELECT * FROM MEMBER WHERE USERNAME LIKE ?";
		
		try {
			// 1)
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2)
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			
			// 3_1)
			pstmt = conn.prepareStatement(sql);
			// 3_2)
			// 방법 1)
			// "SELECT * FROM MEMBER WHERE USERNAME LIKE '%'||?||'%'" // keyword
			pstmt.setString(1, keyword);
			// 방법 2)
			// "SELECT * FROM MEMBER WHERE USERNAME LIKE ?" // %keyword%
			// pstmt.setString(1, "%" + keyword + "%");
			
			// 4, 5)
			rset = pstmt.executeQuery();

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
			// 7) 다썼으니 반납하자
			try {
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 8) 키워드가 포함되어있는 행들을 리스트에 요소로 담아서 반환
		return list;
	}
	
	
	public int deleteMember(String userId) {
		
		// 0)
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		// 실행할 SQL문
		// DELETE FROM MEMBER WHERE USERID = userId
		String sql = "DELETE FROM MEMBER WHERE USERID = ?";
		
		
		try {
			// 1)
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2)
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","JDBC","JDBC");
			
			// 3_1)
			pstmt = conn.prepareStatement(sql);
			
			// 3_2)
			pstmt.setString(1, userId);
			
			// 4, 5)
			result = pstmt.executeUpdate();
			
			// 6)
			if(result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 8)
		return result;
	}
	
	
	public int updateMember(Member m) {
		
		int result = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		// 실행할 SQL문
		// update member
		//    set userpwd = 'xxx'
		//       ,email = 'xxx'
		//       ,phone = 'xxx'
		//       ,address = 'xxx'
		//  where userid = 'xxxx';
		
		String sql = "UPDATE MEMBER "
					+   "SET USERPWD = ?"
					+      ",EMAIL = ?"
					+      ",PHONE = ?"
					+      ",ADDRESS = ?"
					+ "WHERE USERID = ?";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","JDBC","JDBC");
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, m.getUserPwd());
			pstmt.setString(2, m.getEmail());
			pstmt.setString(3, m.getPhone());
			pstmt.setString(4, m.getAddress());
			pstmt.setString(5, m.getUserId());
			
			result = pstmt.executeUpdate();
			
			if(result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public Member selectByUserId(String userId) {
		Member m = new Member();
		//Member m = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String sql =
				"SELECT * FROM MEMBER WHERE USERID = ?";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
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
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return m;
	}
	
}
