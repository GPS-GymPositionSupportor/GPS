<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // 로그인 상태 확인
    String userID = (String) session.getAttribute("userID");
    boolean isRedirected = (Boolean) session.getAttribute("redirected") != null;

    if (userID != null && !isRedirected) {
        // 로그인한 경우 다른 메인 페이지로 리디렉션
        session.setAttribute("redirected", true); // 리디렉션 플래그 설정
        response.sendRedirect("home.jsp");
        return; // 이후 코드 실행 방지
    }
%>
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
    <!-- Google API script -->
    <script src="https://apis.google.com/js/platform.js" async defer></script>
    
    <!-- Kakao API script -->
    <script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
    
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>

<body>
	<% if(session.getAttribute("userID") != null) { %>
	    <div class="navbar">
	        
			<div class="burbutton" id="burbutton">
	            <span></span>
	            <span class="middle"></span>
	            <span></span>
	        </div>
	        
	    <div id="search-container">
		    <input type="text" id="searchInput" placeholder="찾으시는 운동시설을 검색해주세요">
		    <span id="searchIcon" class="search-icon">
	        	<img src="image/icon_search.png" alt="검색" />
			</span>
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
            <form action="/auth/logout" method="post">
                <% if(session.getAttribute("userID") != null) { %>
                    <button type="submit" id="logoutButton">로그아웃</button>
                <% } %>
            </form>
        </div>
    </div> 

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

    <%@ include file="loginOverlay.jsp" %>
	
	<!-- script 파일 -->
	<%@ include file="gpsSc.jsp" %>
</body>
</html>