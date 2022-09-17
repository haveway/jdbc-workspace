package com.kh.controller;

import java.util.ArrayList;

import com.kh.model.dao.MemberDao;
import com.kh.model.vo.Member;
import com.kh.view.MemberView;

// Controller : View를 통해서 요청 기능을 처리하는 담당
//				해당 메소드로 전달된 데이터를 가공처리 한 후 Dao메소드를 호출
//				Dao로부터 반환받은 결과에 따라서 사용자가 보게될 View(응답화면)을 결정(View 메소드 호출)
public class MemberController {
	
	public void insertMember(String userId, String userPwd, String userName,
							 String gender, int age, String email, 
							 String phone, String address, String hobby) {
		// 1. 전달된 데이터들을 Member객체에 담기 => 가공처리
		Member m = new Member(userId, userPwd, userName, gender, age, email, phone, address, hobby);
		
		// 2. Dao의 insertMember(멤버객체) 호출
		int result = new MemberDao().insertMember(m);
		
		// 3. 결과값에 따라서 사용자가 보게 될 화면 지정
		if(result > 0) { // 성공했을 경우
			new MemberView().displaySuccess("회원 추가 성공");
		} else {
			new MemberView().displayFail("회원 추가 실패");
		}
	}
	
	public void selectAll() {
		// SELECT -> ResultSet(Member) -> ArrayList
		ArrayList<Member> list = new MemberDao().selectAll();  // == list
		
		// 조회결과가 있는지 없는지 판단 후 사용자가 보게 될 View화면 지정
		if(list.isEmpty()) {
			new MemberView().displayNodata("전체 조회 결과가 없습니다.");
		} else {
			new MemberView().displayList(list);
		}
		
	}
	
	public void selectByUserId(String userId) {
		Member m = new MemberDao().selectByUserId(userId);
		
		if(m.getUserId() == null) {
			new MemberView().displayNodata(userId + "에 대한 조회 결과 없음");
		} else {
			new MemberView().displayOne(m);
		}
		
	}
	
	public void selectByUserName(String keyword) {
		// SELECT  -> ResultSet -> ArrayList
		
		ArrayList<Member> list = new MemberDao().selectByUserName(keyword);
		
		if(list.isEmpty()) {
			new MemberView().displayNodata("조회결과가없습니다.");
		} else {
			new MemberView().displayList(list);
		}
	}
	
	public void updateMember(String userId, String newPwd, String newEmail,
							String newPhone, String newAddress) {
		
		Member m = new Member();
		
		m.setUserId(userId);
		m.setUserPwd(newPwd);
		m.setEmail(newEmail);
		m.setPhone(newPhone);
		m.setAddress(newAddress);
		
		int result = new MemberDao().updateMember(m);
		
		if(result > 0) {
			new MemberView().displaySuccess("수정성공");
			selectByUserId(userId);
		} else {
			new MemberView().displayFail("수정실패");
		}
		
	}
	
	public void deleteMember(String userId) {
		
		int result = new MemberDao().deleteMember(userId);
		
		if(result > 0) {
			new MemberView().displaySuccess("탈퇴 성공");
		} else {
			new MemberView().displayFail("탈퇴 실패");
		}
	}
	
}
