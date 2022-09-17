package com.kh.model.vo;

import java.sql.Date;

/*
 * VO(Value Object)
 * => DB테이블의 한 행에 대한 데이터를 기록할 수 있는 저장용 객체
 * 
 * > 유사
 * DTO(Data Transfer Object)
 * DO(Domain Object)
 * Entity
 * bean
 * 
 * VO 조건
 * 
 * 1. 반드시 캡슐화를 적용
 * 2. 모든 필드에 대한 setter / getter메소드를 작성할 것
 * 3. 기본생성자 및 매개변수생성자를 작성할 것
 */

/*
 * MEMBER 테이블의 컬럼들과 유사하게 필드를 구성
 * SEQUENCE와 DEFAULT등의 조건으로 값이 들어가는 경우는
 * 해당 필드를 뺀 생성자를 추가해준다.
 */
public class Member {
	
	// 필드는 DB 컬럼정보와 유사하게 작업!
	private int userNo;//USERNO NUMBER
    private String userId;//USERID VARCHAR2(15)
    private String userPwd;//USERPWD VARCHAR2(20)
    private String userName;//USERNAME VARCHAR2(20)
    private String gender;//GENDER CHAR(1)
    private int age;//AGE NUMBER,
    private String email;//EMAIL VARCHAR2(30),
    private String phone;//PHONE CHAR(11),
    private String address;//ADDRESS VARCHAR2(100),
    private String hobby;//HOBBY VARCHAR2(50),
    private Date enrollDate;//ENROLLDATE DATE
    
	public Member() {
		super();
	}
	public Member(int userNo, String userId, String userPwd, String userName, String gender, int age, String email,
			String phone, String address, String hobby, Date enrollDate) {
		super();
		this.userNo = userNo;
		this.userId = userId;
		this.userPwd = userPwd;
		this.userName = userName;
		this.gender = gender;
		this.age = age;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.hobby = hobby;
		this.enrollDate = enrollDate;
	}
	// 회원 추가용 생성자
	public Member(String userId, String userPwd, String userName, String gender, int age, String email, String phone,
			String address, String hobby) {
		super();
		this.userId = userId;
		this.userPwd = userPwd;
		this.userName = userName;
		this.gender = gender;
		this.age = age;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.hobby = hobby;
	}
	
	public int getUserNo() {
		return userNo;
	}
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getHobby() {
		return hobby;
	}
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	public Date getEnrollDate() {
		return enrollDate;
	}
	public void setEnrollDate(Date enrollDate) {
		this.enrollDate = enrollDate;
	}
	
	@Override
	public String toString() {
		return "Member [userNo=" + userNo + ", userId=" + userId + ", userPwd=" + userPwd + ", userName=" + userName
				+ ", gender=" + gender + ", age=" + age + ", email=" + email + ", phone=" + phone + ", address="
				+ address + ", hobby=" + hobby + ", enrollDate=" + enrollDate + "]";
	}

}
