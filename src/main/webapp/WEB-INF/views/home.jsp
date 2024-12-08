<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
	
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8">
    <title>Home</title>
	<style>
		<%@ include file="styleHome.jsp" %>
	</style>
</head>

<body>
	<div id="homePage">
		<img src="../image/homeLogo.svg" alt="logo" title="logo" class="homeLogo">
		
		<div id="search-container">
	    <input type="text" id="searchInput" placeholder="찾으시는 운동시설을 검색해주세요">
	    <span id="searchIcon" class="search-icon">
        	<img src="../image/icon_search2.svg" alt="검색" />
		</span>
		</div>
		
		<div id="imgBtns">
			<div id="profileBtnBlock">
				<button id="profileBtn" onclick="location.href='index.jsp?selectedNav=B'">
				<img
		            src="<%= session.getAttribute("profile_img") != null ? session.getAttribute("profile_img") : "image/myPage_image.svg" %>"
		            alt="myPage_image" title="myPage_image" class="picture">
		            <p id="mrUserName"><%= session.getAttribute("nickname") %></p>
		            <p id="name"><%= session.getAttribute("name") %></p>
		            <p id="gender"><%= session.getAttribute("gender") %></p>
				</button>
			</div>
			<div id="findGymBlock">
			<button id="findGym" onclick="location.href='index.jsp?selectedNav=A'">
				<p id="findGymP">운동시설 찾기</p>
				<img src="../image/findGym.svg" alt="findGym" class="findGym">
			</button>
			</div>
			<div id="justDoItBlock">
				<img src="../image/just.svg" alt="justDoIt" class="justDoIt">
			</div>
			<div id="recommendFeedBlock">
				<button id="recommendFeed" onclick="location.href='index.jsp?selectedNav=C'">
					<p id="recommendFeedP">추천피드</p>
					<img src="../image/recommendFeed.svg" alt="recommendFeed" class="recommendFeed">
				</button>
			</div>
				<div id="myReviewBlock">
				<button id="myReview" onclick="location.href='index.jsp?selectedNav=D'">
					<p id="myReviewP">내가 쓴 리뷰</p>
					<img src="../image/myReview.svg" alt="myReview" class="myReview">
				</button>
			</div>
				<div id="scrapBlock">
				<button id="scrap" onclick="location.href='index.jsp?selectedNav=E'">
					<p id="scrapP">스크랩한 장소</p>
					<img src="../image/scrap.svg" alt="scrap" class="scrap">
				</button>
			</div>
		</div>
	</div>
	
	<div id="logoutContainer">
		<form action="/auth/logout" method="post">
		<button type="submit" id="logoutButton">로그아웃 <img src="../image/logout.png" alt="logout_icon" class="logout_icon"></button>
		</form>
	</div>
	<script>
		document.getElementById('searchIcon').addEventListener('click', function() {
		    var searchQuery = document.getElementById('searchInput').value;
		    if (searchQuery) {
		        console.log('검색어:', searchQuery);
		        // 검색 결과를 표시하거나 다른 페이지로 이동할 수 있습니다.
		        // 예: window.location.href = '/searchResults.jsp?query=' + encodeURIComponent(searchQuery);
		    } else {
		        alert('검색어를 입력하세요.');
		    }
		});
		
		window.onload = function() {
	        // 스크롤 잠금
	        document.body.style.position = 'fixed';
	        document.body.style.width = '100%';
	        document.body.style.height = '100%';
	    };
	</script>
</body>
</html>