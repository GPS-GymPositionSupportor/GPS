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
		<script>
        
		<%-- 햄버거 버튼 작동 애니메이션을 위한 함수 --%>
	        window.onload = function() {
		        var burButton = document.querySelector('.burbutton');
		        var navLinks = document.getElementById('nav-links');
		        
		        burButton.addEventListener('click', (event) => {
		     	    event.currentTarget.classList.toggle('active');
		     	    navLinks.classList.toggle('active');
		        });
	        }
	   </script>
	<%-- 스크립트 소스, 함수 작성 --%>
	</head>
	    	
	<body>
		<%@ include file="DBC.jsp" %>
		<%
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        %>
        
		<!-- 햄버거 버튼 -->
		<div class="navbar">
        
        	<div class="burbutton" id="burbutton">
		        <span></span>
		        <span class="middle"></span>
		        <span></span> 
	    	</div>
	    	
	    	<div id="nav-links">
	            <form id="navForm" method="get">
	            	<%if( session.getAttribute("userID")!=null){%>
	                <button type="submit" name="selectedNav" value="A">A</button>
	                <button type="submit" name="selectedNav" value="B">B</button>
	                <button type="submit" name="selectedNav" value="C">C</button>
	                <button type="submit" name="selectedNav" value="D">D</button>
	                <button type="submit" name="selectedNav" value="E">E</button>
	                <button type="submit" name="selectedNav" value="F">F</button>
	                <button type="submit" name="selectedNav" value="G"><%=session.getAttribute("userID") %></button>
	                <%} else {%>
	                    <label name="selectedNav">로그인</label>
	                <%} %>
	            </form>
	            <form action="logout.jsp" method="post">
	            	<%if( session.getAttribute("userID")!=null){%>
	            	<button type="submit" id="logoutButton">로그아웃</button>
	            	<% } %>
	            </form>
            </div>
        </div>

        <div class = "test"></div>
        
	    	<div>
            <% String selectedNav=request.getParameter("selectedNav"); %>
        	<%-- <% if (session.getAttribute("userID") == null) { %>
            	<%@ include file="login.jsp" %>
            <% } else  --%> <% if ("A".equals(selectedNav)) { %>
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
                                            <% } else if (selectedNav == null || "G".equals(selectedNav)) { %>
                                            	<%@ include file="map.jsp" %>
                                                <%-- <%@ include file=".jsp" %> --%>
                                            <% } %>
        </div>
	</body>