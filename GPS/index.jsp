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
		        	<button class="findIdPw-btn" onclick="location.href='findIdPw.jsp'">아이디/비밀번호 찾기</button>
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
	<script>
	
    
		document.addEventListener('DOMContentLoaded', function() {
			var loginForm = document.getElementById('loginForm');
		    loginForm.addEventListener('submit', function(event) {
		        if (!validForm()) {
		            event.preventDefault(); // 폼 제출 방지
		        }
		    });
		    
			var loginForm = document.getElementById('loginForm');
		    var username = loginForm.querySelector('input[name="username"]');
		    var password = loginForm.querySelector('input[name="password"]');
		
		    loginForm.addEventListener('submit', function(event) {
		        if (!validForm()) {
		            event.preventDefault(); // 폼 제출 방지
		        }
		    });
		
		    function validForm() {
		        var isValid = true;
		
		        // 유효성 검사
		        if (username.value.trim() === "") {
		            username.classList.add('error'); // 에러 클래스 추가
		            isValid = false;
		        } else {
		            username.classList.remove('error'); // 에러 클래스 제거
		        }
		
		        if (password.value.trim() === "") {
		            password.classList.add('error'); // 에러 클래스 추가
		            isValid = false;
		        } else {
		            password.classList.remove('error'); // 에러 클래스 제거
		        }
		
		        return isValid; // 유효성 검사 결과 반환
		    }
		
		    // 클릭 이벤트 추가
		    username.addEventListener('focus', function() {
		        username.classList.remove('error'); // 에러 클래스 제거
		    });
		
		    password.addEventListener('focus', function() {
		        password.classList.remove('error'); // 에러 클래스 제거
		    });
		    
		    var togglePassword = document.getElementById('togglePassword');
		    var passwordInput = document.getElementById('password');
		    var eyeIcon = document.getElementById('eyeIcon');

		    togglePassword.addEventListener('click', function() {
		        var type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
		        passwordInput.setAttribute('type', type);
		        
		        eyeIcon.src = type === 'password' ? 'image/closed_eyes.svg' : 'image/open_eyes.svg'; // 비밀번호가 보일 때 아이콘 변경
		    });
		    
		    <%-- 햄버거 버튼--%>
		    var burButton = document.querySelector('.burbutton');
		    var navLinks = document.getElementById('nav-links');
	
		    burButton.addEventListener('click', (event) => {
		        event.currentTarget.classList.toggle('active');
		        navLinks.classList.toggle('active');
		    });
	
		    function adjustNavLinks() {
	            var navLinks = document.getElementById('nav-links');

	            if (window.innerWidth <= 767) {
	                // 모바일 구조로 변경
	                navLinks.innerHTML = `
	                	<div class="myPage">
		                    <div class="user-info">
		                        <img src="image/myPage_image.png" alt="myPage_image" title="myPage_image" class="picture">
		                        <div class="greeting">
		                            <a class="hello">안녕하세요</a>
		                            <div class="mrUser">
		                                <a class="mrUserName"><%= session.getAttribute("userID") %></a>
		                                <a class="mr">님</a>
		                            </div>
		                        </div>
		                    </div>
		                </div>
	                <div>
	                	<form id="navForm" method="get">
		                    <button type="submit" name="selectedNav" value="A">A</button>
		                    <button type="submit" name="selectedNav" value="B">프로필</button>
		                    <button type="submit" name="selectedNav" value="C">추천 피드</button>
		                    <button type="submit" name="selectedNav" value="D">스크랩한 장소</button>
		                    <button type="submit" name="selectedNav" value="E">내가 쓴 리뷰</button>
		                </form>
		                <div id="logoutContainer">
		                    <form action="logout.jsp" method="post" id="logoutForm">
		                        <button type="submit" id="logoutButton">로그아웃 <img src="image/Frame.png" alt="logout_icon" class="logout_icon"></button>
		                    </form>
	                    <div>
	                `;
	            } else {
	                // 데스크톱 구조로 변경
	                navLinks.innerHTML = `
	                    <form id="navForm" method="get">
	                        <button type="submit" name="selectedNav" value="A">A</button>
	                        <button type="submit" name="selectedNav" value="B">프로필</button>
	                        <button type="submit" name="selectedNav" value="C">추천 피드</button>
	                        <button type="submit" name="selectedNav" value="D">스크랩한 장소</button>
	                        <button type="submit" name="selectedNav" value="E">내가 쓴 리뷰</button>
	                    </form>
	                    <form action="logout.jsp" method="post">
	                        <button type="submit" id="logoutButton">로그아웃</button>
	                    </form>
	                `;
	            }
	        }

	        adjustNavLinks();

	        window.addEventListener('resize', adjustNavLinks);
		});
    
        
    </script>
</body>
</html>