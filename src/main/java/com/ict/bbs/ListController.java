package com.ict.bbs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mybatis.dao.BbsDAO;
import mybatis.vo.BbsVO;
import spring.util.Paging;

@Controller
public class ListController {
	
		@Autowired
		private BbsDAO bbsDao;
		
		//페이징 기법에 필요한 여러 변수들 정의
		int nowPage;
		
		public final int BLOCK_LIST = 7;  //한 페이지 당 보여질 게시물 수를 상수로 정의
		public final int BLOCK_PAGE = 3; //한 블럭 당 보여질 페이지 수
		
		int rowTotal; // 전체 게시물 수
		String pageCode; //페이징 처리된 HTML 코드가 저장될 곳
		
		//나중에 게시물에 대한 검색을 한다면...
		String searchType; // 0이면 제목검색, 1이면 작성자 검색, 2면 내용 검색, 3이면 날짜 검색 등등
		String searchValue; //실제 검색할 키워드를 담는 곳
		
		@RequestMapping("/list")
		public ModelAndView list(String nowPage, String bname)throws Exception{ //사용자가 페이지 번호를 누르면 nowPage에 할당
			
			//현재 페이지 값이 없으면 1로 초기화
			if(nowPage == null) {
				this.nowPage = 1;    //this.nowPage는 멤버변수  그냥 nowPage는 지역변수
			}else { //사용자가 페이지 번호를 누르거나 페이지 블럭을 변경한 경우
				this.nowPage = Integer.parseInt(nowPage);
			}
			
			//게시판 종류를 구별하자!
			if(bname == null)
				bname = "BBS"; //일반 게시판
			
			//전체 게시물의 수를 구한다.
			rowTotal = bbsDao.getTotalCount(bname);
			
			//*********** 페이징 처리를 해주는 객체 생성!! (HTML코드가 자동으로 다 생성됨!!)
			Paging page = new Paging(this.nowPage, rowTotal, BLOCK_LIST, BLOCK_PAGE);
			
			//JSP에서 표현될 페이징 코드! 페이징처리 객체의 getter를 통해 StringBuffer를 호출
			pageCode = page.getSb().toString();
			
			//nowPage 변화에 따라 연산이 이루어져야 되기 때문에 int타입으로 받고!
			//getList()를 호출할 때 문자열로 변환한다.
			int begin = page.getBegin();
			int end = page.getEnd();
			
			BbsVO[] ar = bbsDao.getList(String.valueOf(begin), String.valueOf(end), bname);
			
			ModelAndView mv = new ModelAndView();
			mv.addObject("list", ar);
			mv.addObject("pageCode", pageCode);
			mv.addObject("nowPage", this.nowPage);
			mv.addObject("blockList", BLOCK_LIST);
			mv.addObject("rowTotal", rowTotal);
			
			mv.setViewName("list");  // views에 있는 list.jsp를 의미
			
			return mv;
		}
}
