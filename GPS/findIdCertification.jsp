<!-- id 찾기 이메일로 인증 보냄 알림 및 인증 번호 입력 페이지 -->
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8">
    <title>findIdCertification</title>
    <style>
        <%@ include file="style.jsp" %>

    </style>
</head>
<body>
	<p id="findIdP">아이디 찾기</p>
	<div id="findCId">
		<!-- 뒤로가기 버튼 findIdPw.jsp로 직접 돌아가면 로그인 오버레이로 띄우던 코드 특성상 findIdPw에 스타일이 적용안됨 버튼 삭제 유무 필요 -->
		    <button id="cancelBtn" onclick="location.href='index.jsp?idCertification=findId'">
		    	<img src="image/icon_back.png" alt="뒤로가기">
		    </button>
		    
		    <img src="image/certification.png" alt="certification">
		    
		    <p id="sendMail">메일을 보냈어요!<br>메일의 인증코드를 입력해주세요</p>
		    
		    <div id="certificationCodeId">
		    	<!-- action 인증 프로세스 코드로 연결 필요 -->
		    	<form id="certificationCodeIdForm" action="findIdComplete.jsp" method="post">
		    		<input id="cCodeId" type="text" name="code" placeholder="인증코드 입력">
		    		<%
						String cCodeErrorId = (String) session.getAttribute("cCodeErrorId");
					%>
					
					<!-- 고정된 오류 메시지 공간 -->
					<div id="error-message">
						<%
						if (cCodeErrorId != null) {
							out.print(cCodeErrorId.replace("<br>", "<br/>"));
							session.removeAttribute("cCodeErrorId"); // 메시지 출력 후 세션에서 제거
							//잘못된 인증코드입니다. 메시지 저장 필요
						}
					%>
					</div>
		    		<button type="submit" id="inputCCode">인증하기</button> 
		    	</form>
		    </div>
		<button id="toLoginFindId" onclick="location.href='index.jsp'">로그인 화면</button>
	</div>
	
	<script>
		    
		document.addEventListener('DOMContentLoaded', function() {
	        var certificationCodeIdForm = document.getElementById('certificationCodeIdForm');
	      //var cCodeId = certificationCodeIdForm.querySelector('input[name="username"]');;	// 쿼리셀렉터 인증코드 json 변경 필요
	        var cCodeId = document.getElementById('cCodeId');
	        
	        certificationCodeIdForm.addEventListener('submit', function(event) {
	            if (!validForm()) {
	                event.preventDefault(); // 유효성 검사 실패 시 폼 제출 방지
	            }
	        });
	
	        // 인증코드 유효성 검사
	        function validForm() {
	            var isValid = true;
	
	            if (cCodeId.value.trim() === "") {
	                cCodeId.classList.add('error');
	                isValid = false;
	            } else {
	                cCodeId.classList.remove('error');
	            }
	
	            return isValid;
	        }
	        
	        cCodeId.addEventListener('focus', function() {
	        	cCodeId.classList.remove('error');
		    });
	    });
	</script>
</body>
</html>
