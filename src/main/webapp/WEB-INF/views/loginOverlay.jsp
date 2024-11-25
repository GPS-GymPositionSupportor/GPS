<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 로그인 오버레이 -->
    <div class="login-overlay" id="loginOverlay" style="display: <%= (session.getAttribute("userID") == null) ? "flex" : "none" %>;">
	    <div class="login-form">
	    	<div class="images">
	    		<img src="../image/logo.png" alt="logo1" title="logo" class="picture">
	    		<img src="../image/LookForYourMovement.png" alt="moto" title="moto" class="picture">
	    	</div>
	    	
	    	<%
	    	String loginError = (String) session.getAttribute("loginError");
		    %>
		
		    <!-- 고정된 오류 메시지 공간 -->
		    <div id="error-message">
		        <%
		        if (loginError != null) {
		            out.print(loginError.replace("<br>", "<br/>")); // HTML 태그 출력
		            session.removeAttribute("loginError"); // 메시지 출력 후 세션에서 제거
		        }
		        %>
		    </div>
		    
	        <form action="/api/login" method="post" id="loginForm">
	            <input type="text" name="username" placeholder="아이디를 입력해주세요">
	            <div class="password-container">
			        <input type="password" name="password" id="password" minlength="8" maxlength="16" placeholder="비밀번호를 입력해주세요">
			        <button type="button" id="togglePassword" class="eye-btn">
			        	<img src="../image/closed_eyes.svg" alt="Toggle Password" id="eyeIcon">
			        </button>
			    </div>
	            <button type="submit">로그인</button> 
	        </form>
	        
	        <div class="find-regist-btn">
		        	<button class="findIdPw-btn" id="findIdPwBtn">아이디/비밀번호 찾기</button>
		        	<button class="register-btn" onclick="location.href='/api/register'">회원가입</button>
			</div>
			<p class="sns">sns로 시작하기</p>
			<hr class="divideLine">
	        <div class="social-buttons">
		        <button class="google-btn" onclick="googleLogin()">
		        	<img src="../image/Google Login.png" alt="googleLogin">
		        </button>
		        <button class="kakao-btn" onclick="kakaoLogin()">
		        	<img src="../image/Kakao Login.png" alt="kakaoLogin">
		        </button>
	        </div>
	    </div>
	</div>


