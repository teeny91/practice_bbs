<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>게시글 수정 페이지</title>

<link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="resources/css/summernote-lite.min.css">

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
<body>
	<div id="bbs">
	<form action="edit_ok" method="post" 
	encType="multipart/form-data">
		<table summary="게시판 글수정">
			<caption>게시판 글수정</caption>
			<tbody>
				<tr>
					<th>제목:</th>
					<td><input type="text" name="subject" size="45" value="${vo.subject }"/></td>
				</tr>
				<tr>
					<th>이름:</th>
					<td><input type="text" name="writer" size="12" value="${vo.writer }" disabled/></td>
					<!-- readonly는 편집만 불가할 뿐, vo객체의 멤버변수인 파라미터값이 넘어간다. 
					     disabled는 아얘 파라미터값을 사용하지 않겠다는 의미 -->
				</tr>
				<tr>
					<th>내용:</th>
					<td><textarea name="content" id="content" cols="50" 
							rows="8" >${vo.content }</textarea></td>
				</tr>
				<tr>
					<th>첨부파일:</th>
					<td><input type="file" name="file"/>
					<c:if test="${vo.file_name ne null }">
						(${vo.file_name })
					</c:if>
					</td>
				</tr>

				<tr>
					<th>비밀번호:</th>
					<td><input type="password" name="pwd" id="pwd" size="12"/> </td>
				</tr>

				<tr>
					<td colspan="2">
						<input type="button" value="보내기"
						onclick="sendData()"/>
						<input type="button" value="다시"/>
						<input type="button" value="목록"/>
					</td>
				</tr>
			</tbody>
		</table>
		
		<input type="hidden" name="b_idx" value="${vo.b_idx }"/>
		<input type="hidden" name="nowPage" value="${ nowPage}"/>
		<input type="hidden" name="bname" value="${bname}"/>
		
	</form>
	</div>
	
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

<script src="resources/js/summernote-lite.min.js"></script>
<script src="resources/js/lang/summernote-ko-KR.js"></script>

<script>
	$(function(){
		$("#content").summernote({
			height: 300,
			width: 450,
			lang: 'ko-KR',
			focus: true,
			callbacks:{    //callback : 어떤 상황이 발생할 때 자동으로 실행되는 함수
				onImageUpload: function(files, editor){
					//에디터에 이미지를 올리면 무조건 수행하는 곳
					//console.log('Image upload:'+files);
					
					for(var i=0; i<files.length; i++)
						sendFile(files[i], editor);
				}
			}
		});
	});

function sendFile(file, editor){
	//업로드 시, 이미지가 배열로 인식됨. 이를 서버로 비동기식 통신을 하는 함수를 호출하자.
	// 파일 파라미터를 전달하기 위해 form 객체를 만든다.( 파일첨부가 된 경우에만 적용!! 단순한 form객체가 아님)
	var frm = new FormData();  //	 <form encType="multipart/form-data"/>
	
	//위의 frm 객체에 f라는 파라미터를 지정!
	frm.append("file", file);    //frm에 첨부된 파일을 f라는 파라미터로 저장
	
	//비동기식 통신
	//jQuery의 ajax 메서드를 호출하고 , 제대로 수행됐을 때 done()를 호출함. 이때 넘어오는 데이터를 done의 인자로 받는다.
	$.ajax({
		url: "saveImage",
		data: frm, 
		contentType: false,  //기본적으로 application으로 지정되어있음. 이 경우 파일첨부는 불가하므로 false값을 지정해야함!
		processData: false, //일반적인 data 전송 방식이 아님을 증명해야 한다.
		type: "post",
		dataType: "json" //서버로부터 받을 데이터의 형식을 지정!
		
	}).done(function(data){
		//서버에서 정상적으로 처리가 되었을 때, 서버에서 반환하는 값을 data로 받아서 수행!
		//console.log(data);  //데이터가 잘 넘어오는지 확인용
		
		//에디터에 image경로를 넣어준다.
		$("#content").summernote("editor.insertImage", data.url);
		
		//var path = data.path;         //데이터데 저장된 이미지 경로
		//var fname = data.fname;   //데이터데 저장된 파일 실제 이름
		
		/* 에디터에 이미지를 보여주는 방식 1 (예전 방식)
		//img 태그와 속성 정의
		var image = $("<img>").attr("src", path+"/"+fname);
		//에디터에 정의한 img태그를 보여준다.
		$("#content").summernote('insertNode', image[0]); 
		*/
		
		//에디터에 이미지를 보여주는 방식 2 (최신 방식)
		/* $("#content").summernote("editor.insertImage", path+"/"+fname); */
		
	}).fail(function(err){
		//서버에서 오류 발생 시, 수행(브라우저 상의 console에 찍음)
		console.log(err);
	});
}

function sendData(){
	//필요한 유효성 감사!
	
	var subject = document.forms[0].subject.value;
	
	if(subject.trim().length < 1){
		//제목을 입력하지 않은 경우
		alert("제목을 입력하세요!");
		document.forms[0].subject.focus();
		return;
	}
	

	document.forms[0].submit();
}
</script>
	
</body>
</html>