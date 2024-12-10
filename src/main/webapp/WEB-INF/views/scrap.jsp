<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
	
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8">
    <title>scrap</title>
	<style>
		<%@ include file="styleScrap.jsp" %>
	</style>
</head>
<body>
	<div>
		<a href="home.jsp" class="exit-button" title="BackToHome"></a>
	</div>
	<div id="scrap">
		<p id="scrapP">스크랩한 장소</p>
		<div id="scrapContainer">
			<div id="scrap-item"> <!-- 반복으로 추가 되는 scrap div -->
				<img
					src="<%= session.getAttribute("ImageUrl") != null ? session.getAttribute("ImageUrl") : "image/logo.png" %>"
				    alt="edit_scrap_image" title="edit_scrap_image" id="editScrapImage">
				    <p id="gymName"><%= session.getAttribute("g_name") %></p>
				    <p id="gymAddress"><%= session.getAttribute("address1") %></p>
				    <button id="bookMark" onclick="toggleBookmark('${scrap.id}', this)">
				    	<img src="image/bookMark.svg" alt="bookMarkIcon" class="bookMarkIcon" id="bookMarkIcon">
				    </button>
				    <button id="gymReadMore" onclick="readMore">자세히 보기</button>
			</div>
		</div> <!-- 스크랩 담을 컨테이너 -->
		<div id="loading" style="display: none;">로딩 중...</div> <!-- 로딩 메시지 -->
	</div>
	
	<script>
	let currentPage = 0; // 현재 페이지 번호
	var pageSize = 10; // 한 번에 로드할 피드 수

	// 스크랩 로드하는 함수
	function loadScraps() {
	    var loadingDiv = document.getElementById('loading');
	    loadingDiv.style.display = 'block'; // 로딩 메시지 표시

	    // AJAX 요청을 통해 피드를 가져오는 예시
	    fetch(`getScraps.jsp?page=${currentPage}&size=${pageSize}`)
	        .then(response => response.json())
	        .then(data => {
	            var scrapContainer = document.getElementById('scrapContainer');

	            data.scraps.forEach(scrap => {
	                var scrapDiv = document.createElement('div');
	                scrapDiv.className = 'scrap-item'; // 피드 아이템 클래스 추가

	                // HTML 구조에 맞춰 피드 항목 생성
	                scrapDiv.innerHTML = `
	                    <img
	                        src="${scrap.imageUrl || 'image/logo.png'}" 
	                        alt="edit_scrap_image" 
	                        title="edit_scrap_image" 
	                        id="editScrapImage">
	                    <p id="gymName">${scrap.gymName}</p>
	                    <p id="gymAddress">${scrap.gymAddress}</p>
	                    <button id="bookMark">
	                        <img src="image/bookMark.svg" alt="bookMarkIcon" class="bookMarkIcon" id="bookMarkIcon">
	                    </button>
	                    <button id="gymReadMore" onclick="readMore('${scrap.id}')">자세히 보기</button>
	                `;
	                
	                scrapContainer.appendChild(scrapDiv); // 피드 컨테이너에 추가
	            });

	            currentPage++; // 페이지 증가
	            loadingDiv.style.display = 'none'; // 로딩 메시지 숨김
	        })
	        .catch(error => {
	            console.error('Error loading scraps:', error);
	            loadingDiv.style.display = 'none'; // 로딩 메시지 숨김
	        });
	}

	// 스크롤 이벤트로 더 많은 피드를 로드
	window.addEventListener('scroll', () => {
	    if (window.innerHeight + window.scrollY >= document.body.offsetHeight) {
	        loadScraps(); // 스크롤이 바닥에 닿으면 피드 로드
	    }
	});

	// 초기 로드
	loadScraps();

	// 자세히 보기 버튼 클릭 시 동작할 함수
	function readMore(scrapId) {
	    // 여기에 자세히 보기 로직을 추가
	    console.log("자세히 보기 클릭:", scrapId);
	}
	
	function toggleBookmark(scrapId, button) {
	    const icon = button.querySelector('.bookMarkIcon');
	    const isBookmarked = icon.src.includes('bookMark2.svg');

	    // 북마크 상태에 따라 이미지 변경
	    if (isBookmarked) {
	        icon.src = 'image/bookMark.svg'; // 북마크 해제
	    } else {
	        icon.src = 'image/bookMark2.svg'; // 북마크 추가
	    }

	    // AJAX 요청으로 북마크 상태 저장
	    fetch(`toggleBookmark.jsp?scrapId=${scrapId}&bookmarked=${!isBookmarked}`, {
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