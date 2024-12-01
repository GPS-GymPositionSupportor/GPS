<!-- pw 인증완료 페이지 -->
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
	<p id="findPwP">비밀번호 찾기</p>
	<div id="findPwComplete">
		<img src="image/certificationComplete.png" alt="certificationComplete">
		<p id="certificationComplete">인증완료</p>
		<p id="pwMessage">메일로 임시 비밀번호를 보내드리겠습니다!<br>임시 비밀번호로 로그인하여 비밀번호를<br>변경해주세요.</p>
		
		<button id="toLoginFindId" onclick="location.href='index.jsp'">로그인 화면</button>
	</div>
</body>
</html>