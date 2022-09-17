package com.kh.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCTemplate {
	
	// JDBC과정 중 반복적으로 쓰이는 구문들을 각각의 메소드로 정의해둘 곳
	// "재사용할 목적"으로 공통 템플릿 작업 진행
	
	// 이 클래스에서의 모든 메소드들은 다 static 메소드로 만들것!
	// 싱글톤패턴 : 메모리영역에 단 한번만 올라간 것을 재사용한 개념!
	
	// 공통적인 부분 뽑아내기
	// 1. DB와 접속된 Connection 객체를 생성해서 반환시켜주는 메소드
	public static Connection getConnection() {
		
		// Connection 객체를 담을 그릇 생성
		Connection conn = null;
		
		// 연결시키기 => 1), 2)
		
		
		/*
		 * 기존의 방식 : JDBC Driver 구문, 내가 접속할 DB의 url정보, 계정명, 비밀번호 등을
		 * 			  자바 소스코드 내부에 명시적으로 작성함 => 정적 코딩방식(하드코딩)
		 * 
		 * - 문제점 : DBMS가 변경 되었을 경우 / 접속할 url, 계정명, 비밀번호가 변경되었을경우
		 * 			=> 자바소스코드를 수정해야함
		 * 		       수정한 코드 내용을 반영시키고자 한다면 프로그램을 재구동 해야함
		 * 		    (사용자 입장에서 프로그램 사용 중 비정상적으로 종료되었다가 다시 구동될 수 있음)
		 * 			* 유지보수가 불편함
		 * 
		 * - 해결 방식 : DB관련된 정보들을 별도로 관리하는 외부파일(.properties)로 만들어서 관리
		 * 		      외부파일로 key에 대한 value를 읽어들여서 반영시킬 것 => 동적코딩방식
		 * 
		 */
		// 동적 코딩방식을 적용하기 위해 Properties객체를 생성
		Properties prop = new Properties();
		try {
			// prop객체로부터 load메소드를 이용해서 각 키에 해당하는 벨류를 가져오겠다!
			prop.load(new FileInputStream("resources/driver.properties"));
			
			// prop로 부터 getProperty메소드를 이용해서 각 키에 해당하는 벨류를 뽑아서 배치
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			Class.forName(prop.getProperty("driver"));
			
			//conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","JDBC","JDBC");
			conn = DriverManager.getConnection(prop.getProperty("url"),
											   prop.getProperty("username"),
											   prop.getProperty("password"));
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	// 2. 전달받은 JDBC 용 객체를 반납시켜주는 메소드(각 객체별로)
	// 2_1)) Connection객체를 전달받아서 반납시켜주는 메소드
	public static void close(Connection conn) {
		try {
			// 주의사항!
			// 만약에 conn 이 null이라면 NullpointerException 발생할 수 있음
			if(conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 2_2) Statement 객체를 전달받아서 반납시켜주는 메소드(오버로딩 적용)
	// => 다형성으로 인해 PreparedStatement객체 또한 매개변수로 전달이 가능하다!
	public static void close(Statement stmt) {
		try {
			if(stmt != null && !stmt.isClosed()) {
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 2_3) ResultSet 객체를 전달받아서 반납시켜주는 메소드(오버로딩 적용)
	public static void close(ResultSet rset) {
		try {
			if(rset != null && !rset.isClosed()) {
				rset.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 3. 전달받은 Connection 객체를 가지고 트랜잭션 처리를 해주는 메소드
	// 3_1) 전달받은 Connection 객체를 가지고 COMMIT 시켜주는 메소드
	public static void commit(Connection conn) {
		try {
			if(conn != null && !conn.isClosed()) {
				conn.commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 3_2) 전달받은 Connection 객체를 가지고 ROLLBACK 시켜주는 메소드
	public static void rollback(Connection conn) {
		try {
			if(conn != null && !conn.isClosed()) {
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
