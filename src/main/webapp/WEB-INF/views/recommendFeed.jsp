<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
	
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8">
    <title>recommendFeed</title>
	<style>
		<%@ include file="styleRecommendFeed.jsp" %>
	</style>
</head>
<body>
	<div>
		<a href="/api/main" class="exit-button" title="BackToHome"></a>
	</div>
	<div id="recommendFeed">
		<p id="recommendP">추천 피드</p>
		<div id="feedContainer">
			<div id="feed-item"> <!-- 반복으로 추가 되는 feed div -->
				<img
					src="<%= session.getAttribute("ImageUrl") != null ? session.getAttribute("ImageUrl") : "../image/logo.png" %>"
				    alt="edit_recommendFeed_image" title="edit_recommendFeed_image" id="editRecommendFeedImage">
				    <p id="gymName"><%= session.getAttribute("g_name") %></p>
				    <p id="gymAddress"><%= session.getAttribute("address1") %></p>
				    <button id="bookMark" onclick="toggleBookmark('${feed.id}', this)">
				    	<img src="../image/bookMark.svg" alt="bookMarkIcon" class="bookMarkIcon" id="bookMarkIcon">
				    </button>
				    <button id="gymReadMore" onclick="">자세히 보기</button>
			</div>
		</div> <!-- 피드를 담을 컨테이너 -->
		<div id="loading" style="display: none;">로딩 중...</div> <!-- 로딩 메시지 -->
	</div>
	
	<script>
	let currentPage = 0; // 현재 페이지 번호
	var pageSize = 10; // 한 번에 로드할 피드 수

	// 피드를 로드하는 함수
	function loadFeeds() {
	    var loadingDiv = document.getElementById('loading');
	    loadingDiv.style.display = 'block'; // 로딩 메시지 표시

	    // AJAX 요청을 통해 피드를 가져오는 예시
	    fetch(`getFeeds.jsp?page=${currentPage}&size=${pageSize}`)
	        .then(response => response.json())
	        .then(data => {
	            var feedContainer = document.getElementById('feedContainer');

	            data.feeds.forEach(feed => {
	                var feedDiv = document.createElement('div');
	                feedDiv.className = 'feed-item'; // 피드 아이템 클래스 추가

	                // HTML 구조에 맞춰 피드 항목 생성
	                feedDiv.innerHTML = `
	                    <img
	                        src="${feed.imageUrl || 'image/logo.png'}" 
	                        alt="edit_recommendFeed_image" 
	                        title="edit_recommendFeed_image" 
	                        id="editRecommendFeedImage">
	                    <p id="gymName">${feed.gymName}</p>
	                    <p id="gymAddress">${feed.gymAddress}</p>
	                    <button id="bookMark">
	                        <img src="image/bookMark.svg" alt="bookMarkIcon" class="bookMarkIcon" id="bookMarkIcon">
	                    </button>
	                    <button id="gymReadMore" onclick="readMore('${feed.id}')">자세히 보기</button>
	                `;
	                
	                feedContainer.appendChild(feedDiv); // 피드 컨테이너에 추가
	            });

	            currentPage++; // 페이지 증가
	            loadingDiv.style.display = 'none'; // 로딩 메시지 숨김
	        })
	        .catch(error => {
	            console.error('Error loading feeds:', error);
	            loadingDiv.style.display = 'none'; // 로딩 메시지 숨김
	        });
	}

	// 스크롤 이벤트로 더 많은 피드를 로드
	window.addEventListener('scroll', () => {
	    if (window.innerHeight + window.scrollY >= document.body.offsetHeight) {
	        loadFeeds(); // 스크롤이 바닥에 닿으면 피드 로드
	    }
	});

	// 초기 로드
	loadFeeds();

	// 자세히 보기 버튼 클릭 시 동작할 함수
	function readMore(feedId) {
	    // 여기에 자세히 보기 로직을 추가
	    console.log("자세히 보기 클릭:", feedId);
	}
	
	function toggleBookmark(feedId, button) {
	    const icon = button.querySelector('.bookMarkIcon');
	    const isBookmarked = icon.src.includes('bookMark2.svg');

	    // 북마크 상태에 따라 이미지 변경
	    if (isBookmarked) {
	        icon.src = '../image/bookMark.svg'; // 북마크 해제
	    } else {
	        icon.src = '../image/bookMark2.svg'; // 북마크 추가
	    }

	    // AJAX 요청으로 북마크 상태 저장
	    fetch(`toggleBookmark.jsp?feedId=${feedId}&bookmarked=${!isBookmarked}`, {
	        method: 'POST'
	    })
	    .then(response => response.json())
	    .then(data => {
	        // 성공적으로 저장되었을 경우의 처리
	        console.log("북마크 상태 저장됨:", data);
	    })
	    .catch(error => {
	        console.error('북마크 상태 저장 실패:', error);
	    });
	}
	</script>
	
</body>
</html>