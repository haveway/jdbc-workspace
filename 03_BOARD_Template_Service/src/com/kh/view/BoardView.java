package com.kh.view;

import java.util.ArrayList;
import java.util.Scanner;

import com.kh.controller.BoardController;
import com.kh.model.vo.Board;

public class BoardView {
	
	private Scanner sc = new Scanner(System.in);
	private BoardController bc = new BoardController();
	
	//  메인화면
	public void mainMenu() {
		while(true) {
			System.out.println("************** 자유게시판 **************");
			selectAll();
			
			System.out.println("\n---------------------------------------");
			System.out.println("1. 게시글 조회하기");
			System.out.println("2. 게시글 작성하기");
			System.out.println("3. 게시글 삭제하기");
			System.out.println("9. 자유게시판 종료");
			System.out.print("메뉴 입력 > ");
			int menu = sc.nextInt();
			sc.nextLine();
			
			switch(menu) {
			case 1 : selectBoard(); break;
			case 2 : insertBoard(); break;
			case 3 : deleteBoard(); break;
			case 9 : System.out.println("자유게시판을 종료합니다."); return;
			default : System.out.println("없는메뉴입니다. 다시입력해주세요.");
			}
		}
	}
	
	// ---------- 요청 화면 ----------
	
	//게시글 전체 조회 화면
	private void selectAll() {
		
		ArrayList<Board> list = bc.selectAll();
		
		if(list.isEmpty()) {
			System.out.println("게시글이 없습니다.");
		} else {
			for(Board b : list) {
				System.out.println(b);
			}
		}
	}
	
	// 단일 게시글 조회 화면
	private void selectBoard() {
		
		System.out.println("---- 게시글 조회하기 ----");
		System.out.print("조회할 게시글 번호 > ");
		int boardNo = sc.nextInt();
		sc.nextLine();
		
		Board b = bc.selectBoard(boardNo);

		if(b == null) {
			System.out.println("게시글이 존재하지 않습니다.");
		} else {
			System.out.println("\n\n 게시글 제목 : " + b.getTitle());
			System.out.println("\n게시글 작성자 : " + b.getWriter() +"\t작성일 : " + b.getCreateDate());
			System.out.println("\n게시글 내용 : " + b.getContent() + "\n\n");
		}
		
		while(true) {
			System.out.print("글목록으로 돌아가시려면 돌아가기를 입력해주세요 > ");
			String keyword = sc.nextLine();
			if(keyword.equals("돌아가기")) return;
		}
	}
	
	// 게시글 작성 화면
	private void insertBoard() {
		
		System.out.println("---- 게시글 작성하기 ----");
		System.out.print("작성자 번호 : ");
		String userNo = sc.nextLine();
		sc.nextLine();
		System.out.print("게시글 제목 : ");
		String title = sc.nextLine();
		System.out.print("게시글 내용 : ");
		String content = sc.nextLine();
		
		bc.insertBoard(userNo, title, content);
	}
	
	// 게시글 삭제 화면
	private void deleteBoard() {
		
		System.out.println("게시글 삭제하기!!!!");
		System.out.print("삭제한 게시글 번호 > ");
		int boardNo = sc.nextInt();
		sc.nextLine();
		
		bc.deleteBoard(boardNo);
	}
	
	
	// --------------------- 응답화면 ---------------------
	
	public void displaySuccess(String message) {
		System.out.println("서비스 요청 성공 : " + message);
	}
	
	public void displayFail(String message) {
		System.out.println("서비스 요청 실패 : " + message);
	}

}
