<!-- id 인증완료 페이지 -->
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8">
    <title>findIdCertification</title>
    <style>
        <%@ include file="style.jsp" %>

    </style>
</head>
<body>
	<p id="findIdP">아이디 찾기</p>
	<div id="findIdComplete">
		<img src="image/certificationComplete.png" alt="certificationComplete">
		<p id="certificationComplete">인증완료</p>
		<p id="idMessage">메일로 아이디를 보내드리겠습니다!</p>
		
		<button id="toFindPw" onclick="location.href='index.jsp?show=findPw'">비밀번호 찾기</button>
		
		<button id="toLoginFindId" onclick="location.href='index.jsp'">로그인 화면</button>
	</div>
</body>
</html>