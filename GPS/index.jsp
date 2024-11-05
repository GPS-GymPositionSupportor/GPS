<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8">
    <title>GPS</title>
    <style>
        <%@ include file="style.jsp" %>

        /* 로그인 페이지 스타일 추가 */
        body {
            margin: 0;
            font-family: Roboto;
        }

        .navbar {
            position: relative;
            z-index: 2;
        }

        
    </style>
    
</head>

<body>
    <%@ include file="DBC.jsp" %>
    <%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    %>
    
	<% if(session.getAttribute("userID") != null) { %>
	    <div class="navbar">
	        
			<div class="burbutton" id="burbutton">
	            <span></span>
	            <span class="middle"></span>
	            <span></span>
	        </div>
	        
	<% } %>

        <div id="nav-links">
            <form id="navForm" method="get">
                <% if(session.getAttribute("userID") != null) { %>
                    <button type="submit" name="selectedNav" value="A">A</button>
                    <button type="submit" name="selectedNav" value="B">프로필</button>
                    <button type="submit" name="selectedNav" value="C">추천 피드</button>
                    <button type="submit" name="selectedNav" value="D">스크랩한 장소</button>
                    <button type="submit" name="selectedNav" value="E">내가 쓴 리뷰</button>
                <% } %>
            </form>
            <form action="logout.jsp" method="post">
                <% if(session.getAttribute("userID") != null) { %>
                    <button type="submit" id="logoutButton">로그아웃</button>
                <% } %>
            </form>
        </div>
    </div>

    <div class="test"></div>

    <div>
        <% String selectedNav = request.getParameter("selectedNav"); %>
        <% if (selectedNav == null || "A".equals(selectedNav)) { %>
            <%@ include file="map.jsp" %>
        <% } else if ("B".equals(selectedNav)) { %>
            <h4>B</h4>
        <% } else if ("C".equals(selectedNav)) { %>
            <h4>C</h4>
        <% } else if ("D".equals(selectedNav)) { %>
            <h4>D</h4>
        <% } else if ("E".equals(selectedNav)) { %>
            <h4>E</h4>
        <% } else if ("F".equals(selectedNav)) { %>
            <h4>F</h4>
        <% } else if ("G".equals(selectedNav)) { %>
            <h5>G</h5>
        <% } %>
    </div>

    <!-- 로그인 오버레이 -->
    <div class="login-overlay" id="loginOverlay" style="display: <%= (session.getAttribute("userID") == null) ? "flex" : "none" %>;">
	    <div class="login-form">
	    	<div class="images">
	    		<img src="image/logo.png" alt="logo1" title="logo" class="picture">
	    		<img src="image/LookForYourMovement.png" alt="moto" title="moto" class="picture">
	    	</div>
	    	
	    	<%
	    	String loginError = (String) session.getAttribute("loginError");
		    %>
		
		    <!-- 고정된 오류 메시지 공간 -->
		    <div id="error-message"">
		        <%
		        if (loginError != null) {
		            out.print(loginError.replace("<br>", "<br/>")); // HTML 태그 출력
		            session.removeAttribute("loginError"); // 메시지 출력 후 세션에서 제거
		        }
		        %>
		    </div>
		    
	        <form action="loginProcess.jsp" method="post" id="loginForm">
	            <input type="text" name="username" placeholder="아이디를 입력해주세요">
	            <div class="password-container">
			        <input type="password" name="password" id="password" minlength="8" maxlength="16" placeholder="비밀번호를 입력해주세요">
			        <button type="button" id="togglePassword" class="eye-btn">
			        	<img src="image/closed_eyes.svg" alt="Toggle Password" id="eyeIcon">
			        </button>
			    </div>
	            <button type="submit">로그인</button> 
	        </form>
	        
	        <div class="find-regist-btn">
		        	<button class="findIdPw-btn" id="findIdPwBtn">아이디/비밀번호 찾기</button>
		        	<button class="register-btn" onclick="location.href='register.jsp'">회원가입</button>
			</div>
			<p class="sns">sns로 시작하기</p>
			<hr class="divideLine">
	        <div class="social-buttons">
		        <button class="google-btn">Sign in with Google</button>
		        <button class="kakao-btn">Login with Kakao</button>
	        </div>
	    </div>
    	
	</div>
	
	<!-- script 파일 -->
	<%@ include file="gpsSc.jsp" %>
</body>
</html>