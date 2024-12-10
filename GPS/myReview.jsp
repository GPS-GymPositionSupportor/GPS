<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
	
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8">
    <title>review</title>
	<style>
		<%@ include file="styleReview.jsp" %>
	</style>
</head>
<body>
	<div>
		<a href="home.jsp" class="exit-button" title="BackToHome"></a>
	</div>
	<div id="review">
		<p id="reviewP">내가 쓴 리뷰</p>
		<div id="reviewContainer">
			<div id="review-item"> <!-- 반복으로 추가 되는 review div -->
				<img
					src="<%= session.getAttribute("ImageUrl") != null ? session.getAttribute("ImageUrl") : "image/logo.png" %>"
				    alt="edit_review_image" title="edit_review_image" id="editReviewImage">
				    <p id="gymName"><%= session.getAttribute("g_name") %></p>
				    <p id="gymAddress"><%= session.getAttribute("address1") %></p>
				    <button id="bookMark" onclick="toggleBookmark('${review.id}', this)">
				    	<img src="image/bookMark.svg" alt="bookMarkIcon" class="bookMarkIcon" id="bookMarkIcon">
				    </button>
				    <button id="gymReadMore" onclick="">자세히 보기</button>
			</div>
		</div> <!-- 스크랩 담을 컨테이너 -->
		<div id="loading" style="display: none;">로딩 중...</div> <!-- 로딩 메시지 -->
	</div>
	
	<script>
	let currentPage = 0; // 현재 페이지 번호
	var pageSize = 10; // 한 번에 로드할 피드 수

	// 스크랩 로드하는 함수
	function loadReviews() {
	    var loadingDiv = document.getElementById('loading');
	    loadingDiv.style.display = 'block'; // 로딩 메시지 표시

	    // AJAX 요청을 통해 피드를 가져오는 예시
	    fetch(`getReviews.jsp?page=${currentPage}&size=${pageSize}`)
	        .then(response => response.json())
	        .then(data => {
	            var reviewContainer = document.getElementById('reviewContainer');

	            data.reviews.forEach(review => {
	                var reviewDiv = document.createElement('div');
	                reviewDiv.className = 'review-item'; // 피드 아이템 클래스 추가

	                // HTML 구조에 맞춰 피드 항목 생성
	                reviewDiv.innerHTML = `
	                    <img
	                        src="${review.imageUrl || 'image/logo.png'}" 
	                        alt="edit_review_image" 
	                        title="edit_review_image" 
	                        id="editReviewImage">
	                    <p id="gymName">${review.gymName}</p>
	                    <p id="gymAddress">${review.gymAddress}</p>
	                    <button id="bookMark">
	                        <img src="image/bookMark.svg" alt="bookMarkIcon" class="bookMarkIcon" id="bookMarkIcon">
	                    </button>
	                    <button id="gymReadMore" onclick="readMore('${review.id}')">자세히 보기</button>
	                `;
	                
	                reviewContainer.appendChild(reviewDiv); // 피드 컨테이너에 추가
	            });

	            currentPage++; // 페이지 증가
	            loadingDiv.style.display = 'none'; // 로딩 메시지 숨김
	        })
	        .catch(error => {
	            console.error('Error loading reviews:', error);
	            loadingDiv.style.display = 'none'; // 로딩 메시지 숨김
	        });
	}

	// 스크롤 이벤트로 더 많은 피드를 로드
	window.addEventListener('scroll', () => {
	    if (window.innerHeight + window.scrollY >= document.body.offsetHeight) {
	        loadReviews(); // 스크롤이 바닥에 닿으면 피드 로드
	    }
	});

	// 초기 로드
	loadReviews();

	// 자세히 보기 버튼 클릭 시 동작할 함수
	function readMore(reviewId) {
	    // 여기에 자세히 보기 로직을 추가
	    console.log("자세히 보기 클릭:", reviewId);
	}
	
	</script>
	
</body>
</html>