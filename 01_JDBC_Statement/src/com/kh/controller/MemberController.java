package com.kh.controller;

import java.util.ArrayList;

import com.kh.model.dao.MemberDao;
import com.kh.model.vo.Member;
import com.kh.view.MemberView;

// Controller : View를 통해서 요청 기능을 처리하는 담당
//				해당 메소드로 전달된 데이터를 가공처리 한 후 Dao메소드를 호출
//				Dao로부터 반환받은 결과에 따라서 사용자가 보게될 View(응답화면)을 결정(View 메소드 호출)
public class MemberController {
	
	/**
	 * 사용자의 회원 추가 요청을 처리해주는 메소드
	 * @param userId
	 * @param userPwd
	 * @param userName
	 * @param gender
	 * @param age
	 * @param email
	 * @param phone
	 * @param address
	 * @param hobby
	 * => 사용자가 요청 시 입력했던 값들
	 */
	public void insertMember(String userId, String userPwd, String userName,
							 String gender, int age, String email, 
							 String phone, String address, String hobby) {
		
		// 전달된 데이터들을 Member객체에 담기
		Member m = new Member(userId, userPwd, userName
		     	        	 ,gender, age, email
			    	         ,phone, address, hobby);
		
		int result = new MemberDao().insertMember(m);
		
		if(result > 0) { // 성공했을 경우
			new MemberView().displaySuccess("회원 가입 성공");
		} else { // 실패했을 경우
			new MemberView().displayFail("회원 가입 실패");
		}
	}
	
	
	/**
	 * 사용자의 회원 전체 조회 요청을 처리해주는 메소드
	 */
	public void selectAll() {
		// 결과를 담을 변수
		// SELECT -> ResultSet -> ArrayList<Member>
		
		ArrayList<Member> list = new MemberDao().selectAll();
		
		// 조회결과가 있는지 없는지 판단 후 사용자가 보게 될 View 화면 지정
		if(list.isEmpty()) { // 텅 빈 리스트 => 조회 결과가 없다
			new MemberView().displayNodata("조회결과없어용~");
		} else { // 요소가 존재함 => 조회 결과가 있음
			new MemberView().displayList(list);
		}
		
	}
	
	/**
	 * 사용자의 아이디로 검색요청을 처리해주는 메소드
	 * @param userId : 사용자가 입력한 검색하고자 하는 아이디
	 */
	public void selectByUserId(String userId) {
		// SELECT => ResultSet(1행) => Member
		Member m = new MemberDao().selectByUserId(userId);
		
		// 조회 결과가 있는지 없는지 판단 후 사용자가 보게 될 View 지정
		if(m == null) { // 조회 결과가 없을경우
			new MemberView().displayFail(userId + "는 존재하지 않습니다.");
		} else { // 조회 결과가 있을경우
			new MemberView().displayOne(m);
		}
	}
	
	
	public void selectByUserName(String keyword) {
		// 결과값을 담을 변수
		// SELECT -> ResultSet -> ArrayList<Member>
		ArrayList<Member> list = new MemberDao().selectByUserName(keyword);
		
		// 조회결과가 있을지 없을지 모름
		if(list.isEmpty()) { // 검색결과가 없을경우
			new MemberView().displayFail(keyword + "에 대한 검색결과가 없습니다.");
		} else { // 리스트에 요소가 있을 경우
			new MemberView().displayList(list);
		}
	}
	
	
	/**
	 * 사용자의 회원정보 수정 요청시 처리해주는 메소드
	 */
	public void updateMember(String userId, String newPwd, String newEmail,
							String newPhone, String newAddress) {
		
		// VO 객체에 입력받은 값들 담기(가공처리)
		Member m = new Member();
		
		m.setUserId(userId);
		m.setUserPwd(newPwd);
		m.setEmail(newEmail);
		m.setPhone(newPhone);
		m.setAddress(newAddress);
		
		int result = new MemberDao().updateMember(m);
		
		// 결과에 따른 화면 지정
		if(result > 0) { // 정보수정 성공
			new MemberView().displaySuccess("회원 정보 변경 성공");
		} else { // 정보수정 실패
			new MemberView().displayFail("회원 정보 변경 실패");
		}
		
	}
	
	public void deleteMember(String userId) {
		
		int result = new MemberDao().deleteMember(userId);
		
		if(result > 0) {
			// 성공
			new MemberView().displaySuccess("회원 탈퇴 성공");
		} else {
			// 실패
			new MemberView().displayFail("회원 탈퇴 실패");
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
}
