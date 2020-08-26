<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>    
    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
	#bbs table {
	    width:580px;
	    margin-left:10px;
	    border:1px solid black;
	    border-collapse:collapse;
	    font-size:14px;
	    
	}
	
	#bbs table caption {
	    font-size:20px;
	    font-weight:bold;
	    margin-bottom:10px;
	}
	
	#bbs table th {
	    text-align:center;
	    border:1px solid black;
	    padding:4px 10px;
	}
	
	#bbs table td {
	    text-align:left;
	    border:1px solid black;
	    padding:4px 10px;
	}
	
	.no {width:15%}
	.subject {width:30%}
	.writer {width:20%}
	.reg {width:20%}
	.hit {width:15%}
	.title{background:lightsteelblue}
	
	.odd {background:silver}
	
</style>

</head>
<body onload="init()">
	<div id="bbs">
	<form method="post" >
	
		<table summary="게시판 글쓰기">
			<caption>게시판 글쓰기</caption>
			<tbody>
				<tr>
					<th>제목:</th>
					<td>${vo.subject }</td>
				</tr>
				
		<!-- 첨부파일이 존재할 때만 출력하는 부분 -->
		<c:if test="${vo.file_name != null and fn:length(vo.file_name) > 4}">
				<tr>
					<th>첨부파일:</th>
					<td><a href="javascript:fDown( ${vo.file_name }')">    <!-- 다운로드 링크, 다운로드는 servlet에서 다룸, a태그는 파일이나 링크밖에 인식하지 못함 그래서 javascript 명시해야함. 파일명 인식을 위해 홑따옴표 추가 -->
						${vo.file_name }
					</a></td>
				</tr>
		</c:if>
				<tr>
					<th>이름:</th>
					<td>${vo.writer }</td>
				</tr>
				<tr>
					<th>내용:</th>
					<td>${vo.content }</td>
				</tr>
		
				<tr>
					<td colspan="2">
						<input type="button" value="수정" onclick="goEdit()"/>
						<input type="button" value="삭제" />
						<input type="button" value="목록" onclick="goList()"/>
					</td>
				</tr>
			</tbody>
		</table>
	</form>
	<form method="post" action="ans_write.jsp">
		이름:<input type="text" name="writer"/><br/>
		내용:<textarea rows="4" cols="55" name="comm"></textarea><br/>
		비밀번호:<input type="password" name="pwd"/><br/>
		
		
		<input type="hidden" name="b_idx" value="">
		<input type="hidden" name="index" value=""/>
		<input type="submit" value="저장하기"/> 
	</form>
	
	댓글들<hr/>
	
		<div>
			이름:글쓴이 &nbsp;&nbsp;
			날짜:날짜<br/>
			내용:댓글 내용
		</div>
	</div>
		
		<form method="post" name="f">
				<input type="hidden" id="b_idx" name="b_idx" value="${vo.b_idx }"/>
		   		<input type="hidden" id="nowPage" name="nowPage" value="${nowPage }"/>
		   		<input type="hidden" id="bname" name="bname" value="${vo.bname }"/>
		   		<input type="hidden" id="ch" name="ch"  value="${ch }"/>
		</form>
			
	   <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
		<script>
				function goEdit(){

					document.f.action = "edit";
					document.f.submit();
					
				}
				
				function goList(){
						document.f.action = "list";
						document.f.submit();
						
				}
				
				function init(){
					//현재 문서에서 id가 ch인 요소를 검색한다.
					var ch = document.getElementById("ch");
					if(ch.value == 'true'){
						alert('수정 완료');
					}
				}
				
		</script>
	
	
</body>
</html>
