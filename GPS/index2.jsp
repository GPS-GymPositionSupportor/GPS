<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8">
    <title>GPS</title>
    <style>
        /* 기존 스타일 포함 */
        <%@ include file="style.jsp" %>

        /* 로그인 페이지 스타일 추가 */
        body {
            margin: 0;
            font-family: Arial, sans-serif;
        }

        .navbar {
            position: relative;
            z-index: 2;
        }

        
    </style>
    <script>
        window.onload = function() {
            var burButton = document.querySelector('.burbutton');
            var navLinks = document.getElementById('nav-links');
            
            burButton.addEventListener('click', (event) => {
                event.currentTarget.classList.toggle('active');
                navLinks.classList.toggle('active');
            });
        }
    </script>
</head>

<body>
    <%@ include file="DBC.jsp" %>
    <%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    %>
		<div class="burbutton" id="burbutton">
            <span></span>
            <span class="middle"></span>
            <span></span>
        </div>

    <div class="navbar">
        

        <div id="nav-links">
            <form id="navForm" method="get">
                <% if(session.getAttribute("userID") != null) { %>
                    <button type="submit" name="selectedNav" value="A">A</button>
                    <button type="submit" name="selectedNav" value="B">B</button>
                    <button type="submit" name="selectedNav" value="C">C</button>
                    <button type="submit" name="selectedNav" value="D">D</button>
                    <button type="submit" name="selectedNav" value="E">E</button>
                    <button type="submit" name="selectedNav" value="F">F</button>
                    <button type="submit" name="selectedNav" value="G"><%= session.getAttribute("userID") %></button>
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
        <form action="loginProcess.jsp" method="post">
            <input type="text" name="username" placeholder="아이디를 입력해주세요" required>
            <input type="password" name="password" placeholder="비밀번호를 입력해주세요" required>
            <button type="submit">로그인 하기</button> 
        </form>
        
        <div class="register-botton">
        	<button class="register-btn" onclick="location.href='register.jsp'">회원가입</button>
		</div>
		
        <div class="social-buttons">
	        <button class="google-btn">Sign in with Google</button>
	        <button class="kakao-btn">Login with Kakao</button>
        </div>
    </div>
    	
</div>
</body>
</html>