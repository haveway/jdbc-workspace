package com.kh.run;

public class MVC {
	
	/*
	 * 1. MVC 패턴
	 * Model : 데이터와 관련된 역할(데이터를 담는다거나, DB에 접근해서 데이터 입출력)
	 * View : 사용자가 보게될 시각적인 요소 / 화면(입력, 출력)
	 * Controller : 사용자의 요청을 받아서 처리 후 응답화면을 지정하는 역할
	 * 
	 * * View단에서만 출력문(System.out.println)을 사용한다.
	 * * Model의 DAO(Data Access Object)에서만 DB에 직접적으로 접근한 후 해당 SQL문 실행 및 결과를 받는다.
	 * 
	 * 2. ojdbc6.jar
	 * 
	 * 프로젝트선택 후 마우스 오른쪽 클릭 -> Properties -> JavaBuildPath -> Libraries ->
	 * Add External JARs -> c:\dev\ojdbc6.jar 선택 -> Apply -> Apply and Close
	 * 
	 * References Libraries에 ojdbc6 추가 확인
	 * 
	 * 얘 안해놓으면 ClassNotFoundException 발생함
	 * 
	 * 
	 * 3. Statement란?
	 * 
	 * Statement = 해당 DB에 SQL문을 전달하고 실행한 후 결과를 받아주는 객체
	 * 				Connection 클래스의 createStatement()호출하여 생성
	 * 				executeXXX 호출시 SQL문을 매개변수로 전달하여 질의를 수행
	 * 
	 * 
	 */
}
