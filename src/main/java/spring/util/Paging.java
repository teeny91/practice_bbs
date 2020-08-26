/**
 * 
 */
package spring.util;

import com.ict.bbs.ListController;

public class Paging {
	
	private int nowPage, //현재 페이지
				rowTotal, //총 게시물 수
				blockList, //한 페이지에 표현될 게시물 수
				blockPage, //한 블럭당 표현할 페이지 수
				totalPage, //총 페이지 수
				startPage, endPage, begin, end;
	
	private boolean isPrePage;  //이전 블럭 이동 기능 가능여부
	private boolean isNextPage;  //다음 블럭 이동 기능 가능여부
	
	//JSP에서 표현할 페이징 HTML 코드를 저장할 곳!
	private StringBuffer sb;
	
	//생성자 구현
	public Paging(int nowPage, int rowTotal, int blockList, int blockPage) {
		
		//인자로 넘어온 지역변수의 값을 멤버 변수에 저장
		this.nowPage = nowPage;
		this.rowTotal = rowTotal;
		this.blockList = blockList;
		this.blockPage = blockPage;
		
		makeHTML();
	}
		public void makeHTML() {
			//먼저 이전페이지 이동기능과 다음페이지 이동기능을 초기화
			isPrePage = false;
			isNextPage = false;
			
			//입력된 전체 게시물의 수를 통해 전체 페이지 수를 구한다.
			/*totalPage = rowTotal / blockList;
			if(rowTotal%blockList > 0) {
				totalPage ++;*/
			
			totalPage = (int)Math.ceil((double)rowTotal/blockList);
			
			//현재 페이지 값이 전체 페이지 값보다 크다면 오류발생 가능! 
			//현재 페이지 값을 전체 페이지 값으로 지정해준다!
			if(nowPage > totalPage)
				nowPage = totalPage;
			
			//현재 블럭의 시작페이지 값과 마지막 페이지 값을 구한다.
			//현재 페이지 : 1 ==> 시작 페이지:1, 마지막 페이지:5
			//현재 페이지 : 2 ==> 시작 페이지:1, 마지막 페이지:5
			startPage = (int)((nowPage-1)/blockPage)*blockPage+1;
			endPage = startPage + blockPage - 1;
			
			//마지막 페이지 값이 전체 페이지 값보다 크면, 
			//마지막 페이지 값을 전체 페이지 값으로 지정한다.
			if(endPage > totalPage)
				endPage = totalPage;
			
			//현재 페이지 값에 의해 시작 게시물의 행번호(begin)와
			//마지막 게시물의 행번호(end)를 지정한다.
			//현재 페이지 : 1 ==> begin:1, end: 7
			//현재 페이지 : 2 ==> begin:8, end: 14
			//현재 페이지 : 3 ==> begin:15, end: 21
			
			begin = (nowPage-1)*blockList+1;
			end = begin + blockList -1;
			
			//이전페이지 이동기능 가능 여부
			if(startPage > 1) //1, 6, 11
				isPrePage = true;
			
			//다음페이지 이동기능 가능 여부
			if(endPage < totalPage)
				isNextPage = true;
			
			//이제 현재페이지 값도 알고, 시작 페이지와 마지막 페이지 값을 알았으니
			//페이지 기법에 사용할 코드를 작성하여 StringBuffer에 저장하자!
			
			sb = new StringBuffer("<ol class='paging'>");
			
			//이전 페이지 이동기능 가능 여부
			if(isPrePage) {
				sb.append("<li><a href='list.inc?nowPage=");   
				sb.append(nowPage - blockPage);
				sb.append("'>&lt; </a></li>");
			}else {
				sb.append("<li class='disable'> &lt; </li>");
			}
			
			//페이지 번호를 출력하는 반복분(startPage ~ endPage)
			for(int i=startPage; i<= endPage; i++) {
				
				//i의 값이 현재페이지 값과 같을 때, a태그를 지정하지 않고 숫자만 출력!
				if(i == nowPage) {
					sb.append("<li class='now'>");
					sb.append(i);
					sb.append("</li>");
				}else {
				sb.append("<li><a href='list.inc?nowPage=");
				sb.append(i); //전달되는 파라미터 값일 뿐, 화면에 출력되는 값은 아님!
				sb.append("'>");
				sb.append(i); //화면에 출력되는 페이지 값
				sb.append("</a></li>");
				}
			}//for문 끝
			
			//다음 페이지 이동기능 가능 여부
			if(isNextPage) {
				sb.append("<li><a href='list.inc?nowPage=");
				sb.append(nowPage+blockPage);
				sb.append("'>&gt; </a></li>");
			}else {
				sb.append("<li class='disable'> &gt; </li>");
			}
			sb.append("</ol>");
		}
		public int getNowPage() {
			return nowPage;
		}
		public void setNowPage(int nowPage) {
			this.nowPage = nowPage;
		}
		public int getRowTotal() {
			return rowTotal;
		}
		public void setRowTotal(int rowTotal) {
			this.rowTotal = rowTotal;
		}
		public int getBlockList() {
			return blockList;
		}
		public void setBlockList(int blockList) {
			this.blockList = blockList;
		}
		public int getBlockPage() {
			return blockPage;
		}
		public void setBlockPage(int blockPage) {
			this.blockPage = blockPage;
		}
		public int getTotalPage() {
			return totalPage;
		}
		public void setTotalPage(int totalPage) {
			this.totalPage = totalPage;
		}
		public int getStartPage() {
			return startPage;
		}
		public void setStartPage(int startPage) {
			this.startPage = startPage;
		}
		public int getEndPage() {
			return endPage;
		}
		public void setEndPage(int endPage) {
			this.endPage = endPage;
		}
		public int getBegin() {
			return begin;
		}
		public void setBegin(int begin) {
			this.begin = begin;
		}
		public int getEnd() {
			return end;
		}
		public void setEnd(int end) {
			this.end = end;
		}
		public boolean isPrePage() {
			return isPrePage;
		}
		public void setPrePage(boolean isPrePage) {
			this.isPrePage = isPrePage;
		}
		public boolean isNextPage() {
			return isNextPage;
		}
		public void setNextPage(boolean isNextPage) {
			this.isNextPage = isNextPage;
		}
		public StringBuffer getSb() {
			return sb;
		}
		public void setSb(StringBuffer sb) {
			this.sb = sb;
		}
			
		}

