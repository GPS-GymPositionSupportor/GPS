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
	<div id="homeDiv">
		<div id="homePage">
			<img src="image/homeLogo.svg" alt="logo" title="logo" class="homeLogo">
			
			<div id="search-container">
		    <input type="text" id="searchInput" placeholder="찾으시는 운동시설을 검색해주세요">
		    <span id="searchIcon" class="search-icon">
	        	<img src="image/icon_search2.svg" alt="검색" />
			</span>
			</div>
			
			<div id="imgBtns">
				<div id="profileBtnBlock">
									<!-- myPage.jsp -->
					<button id="profileBtn" onclick="/api/">
					<img
			            src="<%= session.getAttribute("profile_img") != null ? session.getAttribute("profile_img") : "image/myPage_image.svg" %>"
			            alt="myPage_image" title="myPage_image" class="picture">
			            <p id="mrUserName"><%= session.getAttribute("nickname") %></p>
			            <p id="name"><%= session.getAttribute("name") %></p>
			            <p id="gender"><%= session.getAttribute("gender") %></p>
					</button>
				</div>
				<div id="findGymBlock">
									<!-- index.jsp -->
				<button id="findGym" onclick="/api/">
					<p id="findGymP">운동시설 찾기</p>
					<img src="image/findGym.svg" alt="findGym" class="findGym">
				</button>
				</div>
				<div id="justDoItBlock">
					<img src="image/just.svg" alt="justDoIt" id="justDoIt">
				</div>
				<div id="recommendFeedBlock">
									<!-- recommendFeed.jsp -->
					<button id="recommendFeed" onclick="/api/">
						<p id="recommendFeedP">추천피드</p>
						<img src="image/recommendFeed.svg" alt="recommendFeed" class="recommendFeed">
					</button>
				</div>
					<div id="myReviewBlock">
										<!-- myReview.jsp -->
					<button id="myReview" onclick="/api/">
						<p id="myReviewP">내가 쓴 리뷰</p>
						<img src="image/myReview.svg" alt="myReview" class="myReview">
					</button>
				</div>
					<div id="scrapBlock">
										<!-- scrap.jsp -->
					<button id="scrap" onclick="/api/">
						<p id="scrapP">스크랩한 장소</p>
						<img src="image/scrap.svg" alt="scrap" class="scrap">
					</button>
				</div>
			</div>
		</div>
		
		<div id="logoutContainer">
			<form action="/auth/logout" method="post">
			<button type="submit" id="logoutButton">로그아웃 <img src="image/logout.png" alt="logout_icon" class="logout_icon"></button>
			</form>
		</div>
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
	</script>
</body>
</html>