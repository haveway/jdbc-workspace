package com.kh.controller;

import java.util.ArrayList;

import com.kh.model.service.BoardService;
import com.kh.model.vo.Board;
import com.kh.view.BoardView;

public class BoardController {
	
	public ArrayList<Board> selectAll(){
		
		ArrayList<Board> list = new BoardService().selectAll();
		
		return list;
	}
	
	public Board selectBoard(int boardNo) {
		
		Board b = new BoardService().selectBoard(boardNo);
		
		return b;
	}
	
	public void insertBoard(String userNo, String title, String content) {
		
		int flag = new BoardService().selectUser(userNo);
		if(flag == 0) {
			new BoardView().displayFail("사용자가 존재하지 않습니다.");
			return;
		}
		
		Board b = new Board();
		b.setWriter(userNo);
		b.setTitle(title);
		b.setContent(content);
		
		int result = new BoardService().insertBoard(b);
		
		if(result > 0) {
			new BoardView().displaySuccess("게시글 등록 성공!");
		} else {
			new BoardView().displayFail("게시글 등록 실패!");
		}
		
	}
	
	public void deleteBoard(int boardNo) {
		
		Board b = new BoardService().selectBoard(boardNo);
		if(b == null) {
			new BoardView().displayFail("게시글이 존재하지 않습니다.");
			return;
		}
		
		int result = new BoardService().deleteBoard(boardNo);
		
		if(result > 0) {
			new BoardView().displaySuccess("게시글 삭제 성공!");
		} else {
			new BoardView().displayFail("게시글 삭제 실패!");
		}
	}
	

}
