package com.kh.model.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

import static com.kh.common.JDBCTemplate.*;
import com.kh.model.dao.MemberDao;
import com.kh.model.vo.Member;

/* Service : 기존의 DAO의 역할을 분담
 * 			컨트롤에서 서비스 호출(Connection 객체 생성) 후 서비스를 거쳐서
 * 			DAO로 넘어갈것
 * 			DAO 호출시 커넥션 객체와 기존에 넘기고자 했던 매개변수를 같이 넘겨줌
 * 			DAO 처리가 끝나면 서비스단에서 결과에따른 트랜잭션 처리도 같이 해줌
 * 			=> 서비스 단을 추가함으로써 DAO에는 순수하게 SQL문을 처리하는 부분만 남음
 */

public class MemberService {
	
	public int insertMember(Member m) {
		
		// Connection 객체 생성
		Connection conn = getConnection();
		
		// DAO 호출 시 Connection 객체와 기존에 넘기고자 했던 매개변수를 같이 넘김
		int result = new MemberDao().insertMember(conn, m);
		
		// 결과에 따른 트랜잭션 처리
		if(result > 0) {
			commit(conn);
		}else {
			rollback(conn);
		}
		
		// Connection 객체 반납
		close(conn);
		
		return result;
	}
	
	
	public ArrayList<Member> selectAll() {
		
		// 1) Connection 객체 생성
		Connection conn = /*JDBCTemplate.*/getConnection();

		// 2) 결과값을 받을 변수를 만들고, DAO 호출해서 리턴받기
		// 만약 Controller에서 넘겨준 값이 있다면 conn객체와함께 넘겨줘야함
		ArrayList<Member> list = new MemberDao().selectAll(conn);

		// 3) Connection 객체 close
		close(conn);
		
		// 4) 결과값 리턴
		return list;
	}
	
	public ArrayList<Member> selectByUserName(String keyword){
		// 1) 커넥션 객체 만들기
		Connection conn = getConnection();
		// 2) 매개변수 전달받은 값과 커넥션객체를 같이 Dao로 전달하기
		ArrayList<Member> list = new MemberDao().selectByUserName(conn, keyword);
		// 3) 커넥션객체 반납해주기
		close(conn);
		// 4) 컨트롤로 결과값 반환
		return list;
	}
	
	public int deleteMember(String userId) {
		// 1) Connection객체 생성
		Connection conn = getConnection();
		// 2) 매개변수 전달값하고 커넥션하고 Dao로 전달
		int result = new MemberDao().deleteMember(conn, userId);
		// 3) 트랜잭션처리
		if(result > 0) commit(conn);
		else rollback(conn);
		// 4) 커넥션 객체 반납
		close(conn);
		// 5) 결과값 반환(처리된 행의 갯수)
		return result;
	}
	
	
	public int updateMember(Member m) {
		
		// 1) Connection 객체 생성
		Connection conn = getConnection();
		
		int result = new MemberDao().updateMember(conn, m);
		
		if(result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);
		
		return result;
	}
	
	public Member selectByUserId(String userId) {
		// 1) conn 만들기
		Connection conn = getConnection();
		// 2) dao에 전달
		Member m = new MemberDao().selectByUserId(conn, userId);
		// 3) conn반납
		close(conn);
		// 4) 결과반환
		return m;
	}
	
}
