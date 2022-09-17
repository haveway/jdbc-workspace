package com.kh.controller;

import java.util.ArrayList;

import com.kh.model.dao.MemberDao;
import com.kh.model.service.MemberService;
import com.kh.model.vo.Member;
import com.kh.view.MemberView;

public class MemberController {
	
	public void insertMember(String userId, String userPwd, String userName, String gender, int age, String email,
			String phone, String address, String hobby) {
		
		// 1. 전달된 데이터들을 Member객체에 담기 => 가공처리
		Member m = new Member(userId, userPwd, userName, gender, age, email,
				              phone, address, hobby);
		
		// 2. Dao의 insertMember 메소드 호출
		int result = new MemberService().insertMember(m);
		
		// 3. 결과값에 따라서 사용자가 보게 될 화면 지정
		if(result > 0) { // 성공했을 경우
			// 성공메시지를 띄워주는 화면 호출
			new MemberView().displaySuccess("회원 추가 성공");
		}else { // 실패했을 경우
			// 실패메시지를 띄워주는 화면 호출
			new MemberView().displayFail("회원 추가 실패");
		}
	}

	public void selectAll() {
		
		// SELECT -> ResultSet -> ArrayList
		
		ArrayList<Member> list = new MemberService().selectAll(); // == list
		
		// 조회 결과가 있는지 없는지 판단 후 사용자가 보게 될 View 화면 지정
		if(list.isEmpty()) {
			new MemberView().displayNodata("전체 조회 결과가 없습니다.");
		} else {
			new MemberView().displayList(list);
		}
	}
	
	public void selectByUserName(String keyword) {
		// SELECT -> ResultSet -> ArrayList

		ArrayList<Member> list = new MemberService().selectByUserName(keyword);
		
		if(list.isEmpty()) {
			new MemberView().displayNodata("조회결과없음!");
		} else {
			new MemberView().displayList(list);
		}
	}
	
	public void deleteMember(String userId) {
		
		int result = new MemberService().deleteMember(userId);
		
		if(result > 0) { // 탈퇴 성공
			new MemberView().displaySuccess("성공 ~ ");
		} else { // 탈퇴 실패
			new MemberView().displayFail("실패 ~ ㅠㅠ");
		}
	}

	public void updateMember(String userId, String newPwd, String newEmail, String newPhone, String newAddress) {
		
		Member m = new Member();
		
		m.setUserId(userId);
		m.setUserPwd(newPwd);
		m.setEmail(newEmail);
		m.setPhone(newPhone);
		m.setAddress(newAddress);
		
		int result = new MemberService().updateMember(m);
		
		if(result > 0) {
			new MemberView().displaySuccess("수정성공 ~ !");
		} else {
			new MemberView().displayFail("수정실패 ~  ㅠㅠ");
		}
		
		
	}
	
	public void selectByUserId(String userId) {
		Member m = new MemberService().selectByUserId(userId);
		
		if(m == null) new MemberView().displayNodata("데이타없어 ~");
		else new MemberView().displayOne(m);
	}


}