<!-- id 찾기 이메일로 인증 보냄 알림 및 인증 번호 입력 페이지 -->
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8">
    <title>GPS</title>
    <style>
        <%@ include file="style.jsp" %>

    </style>
</head>
<body>
	<p id="findIdP">아이디 찾기</p>
	<div id="findCId">
		<!-- 뒤로가기 버튼 -->
		    <button id="cancelBtn" onclick="showForm('findBtn')">
		    	<img src="image/icon_back.png" alt="뒤로가기">
		    </button>
		    
		    <img src="image/certification.png" alt="certification">
		    
		    <p id="sendMail">메일을 보냈어요!<br>메일의 인증코드를 입력해주세요</p>
		    
		    <div id="certificationCodeId">
		    	<form id="certificationCodeIdForm" action="" method="post">
		    		<input id="cCodeId" type="text" name="code" placeholder="인증코드 입력">
		    		<%
						String cCodeError = (String) session.getAttribute("cCodeError");
					%>
					
					<!-- 고정된 오류 메시지 공간 -->
					<div id="error-message">
					<%
						if (cCodeError != null) {
							out.print(cCodeError.replace("<br>", "<br/>"));
							session.removeAttribute("cCodeError"); // 메시지 출력 후 세션에서 제거
							//세션에 등록되지 않은 계정입니다. 메시지 저장 필요
						}
					%>
					</div>
		    		<button type="submit" id="inputCCode">인증하기</button>
		    	</form>
		    </div>
		<button id="toLogin" onclick="location.href='index.jsp'">로그인 화면</button>
	</div>
</body>
</html>
