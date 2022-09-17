package com.kh.run;

import com.kh.view.MemberView;

public class Run {

	public static void main(String[] args) {
		
		/*
		 * Properties 복습
		 * Properties : Map 계열 컬렉션, key + value 세트로 담는게 특징
		 * Properties는 주로 외부설정파일을 읽어오기 또는 파일형태로 출력하고자 할 때 쓴다.
		 * 
		 * Properties, xml 파일로 내보내기 => store();, storeToXML();
		 */
		/*
		Properties prop = new Properties();
		
		prop.setProperty("A","B");
		prop.setProperty("driver", "oracle.jdbc.driver.OracleDriver");
		prop.setProperty("url", "jdbc:oracle:thin:@localhost:1521:xe");
		prop.setProperty("username", "JDBC");
		prop.setProperty("password", "JDBC");
		
		
		try {
			prop.store(new FileOutputStream("resources/driver.properties"), "driver.properties");
			
			prop.storeToXML(new FileOutputStream("resources/query.xml"), "query.xml");
			
			// 별도의 경로 제시가 없으면 프로젝트 폴더 내부에 만들어진다.
			// resources : 주로 프로젝트 내부에 외부파일들을 저장하는 역할
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		/*
		Properties prop = new Properties();
		
		try {
			prop.load(new FileInputStream("resources/driver.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(prop.getProperty("driver"));
		*/
		new MemberView().mainMenu();

	}

}
