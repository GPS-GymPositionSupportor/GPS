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
		<a href="/api/main" class="exit-button" title="BackToHome"></a>
	</div>
	<div id="review">
		<p id="reviewP">내가 쓴 리뷰</p>
		<div id="reviewContainer">
			<div id="review-item"> <!-- 반복으로 추가 되는 review div -->
				<img
					src="<%= session.getAttribute("ImageUrl") != null ? session.getAttribute("ImageUrl") : "../image/logo.png" %>"
				    alt="edit_review_image" title="edit_review_image" id="editReviewImage">
				    <p id="gymName"><%= session.getAttribute("gName") %></p>
				    <p id="r_comment"><%= session.getAttribute("r_comment") %></p>
					<p id="reviewWrite"><%= session.getAttribute("added_at") %>
				    <button id="bookMark" onclick="toggleBookmark('${review.id}', this)">
				    	<img src="../image/bookMark.svg" alt="bookMarkIcon" class="bookMarkIcon" id="bookMarkIcon">
				    </button>
				    <button id="gymReadMore" onclick="">자세히 보기</button>
			</div>
		</div> <!-- 스크랩 담을 컨테이너 -->
		<div class="pagination"></div>
		<div id="loading" style="display: none;">로딩 중...</div> <!-- 로딩 메시지 -->
	</div>
	
	<script>
	let currentPage = 0; // 현재 페이지 번호
	var itemsPerPage = 10; // 한 번에 로드할 피드 수

	// 스크랩 로드하는 함수
	function displayReviews(reviews) {
		const reviewContainer = document.getElementById('reviewContainer');
		reviewContainer.innerHTML = '';

		reviews.forEach(review => {
			const reviewElement = document.createElement('div');
			reviewElement.className = 'review-item';

			// 날짜 포맷팅
			const addedDate = new Date(review.addedAt);
			const formattedDate = addedDate.toLocaleDateString('ko-KR', {
				year: 'numeric',
				month: '2-digit',
				day: '2-digit'
			});

			reviewElement.innerHTML =
					'<img src="../image/logo.png"' +
					'alt="edit_review_image"' +
					'title="edit_review_image"' +
					'id="editReviewImage">' +
					'<p id="gymAddress">작성일: ' + review.formattedDate + '</p>' +
					'<p>리뷰내용: ' + review.comment + '</p>' +
					'<button id="bookMark">' +
					'<img src="../image/bookMark.svg" alt="bookMarkIcon" class="bookMarkIcon">' +
					'</button>' +
					'<button id="gymReadMore" onclick="readMore(\'' + review.rId + '\')">자세히 보기</button>';

			reviewContainer.appendChild(reviewElement);
		});
	}

	function updatePagination(totalItems) {
		const pagination = document.querySelector('.pagination');
		const totalPages = Math.ceil(totalItems / itemsPerPage);
		pagination.innerHTML = '';

		let startPage = Math.floor(currentPage / 5) * 5;
		let endPage = Math.min(startPage + 4, totalPages - 1);

		// '<<' 버튼
		if(startPage > 0) {
			const firstGroupButton = document.createElement('button');
			firstGroupButton.innerHTML = '<<';
			firstGroupButton.onclick = function() {
				currentPage = 0;
				loadReviews(0);
			};
			pagination.appendChild(firstGroupButton);
		}

		// '<' 버튼
		if(startPage > 0) {
			const prevGroupButton = document.createElement('button');
			prevGroupButton.innerHTML = '<';
			prevGroupButton.onclick = function() {
				currentPage = startPage - 5;
				loadReviews(currentPage);
			};
			pagination.appendChild(prevGroupButton);
		}

		// 페이지 번호 버튼들
		for(let i = startPage; i <= endPage; i++) {
			const pageButton = document.createElement('button');
			pageButton.textContent = i + 1;
			pageButton.className = i === currentPage ? 'active' : '';
			pageButton.onclick = function() {
				currentPage = i;
				loadReviews(i);
			};
			pagination.appendChild(pageButton);
		}

		// '>' 버튼
		if(endPage < totalPages - 1) {
			const nextGroupButton = document.createElement('button');
			nextGroupButton.innerHTML = '>';
			nextGroupButton.onclick = function() {
				currentPage = startPage + 5;
				loadReviews(currentPage);
			};
			pagination.appendChild(nextGroupButton);
		}

		// '>>' 버튼
		if(endPage < totalPages - 1) {
			const lastGroupButton = document.createElement('button');
			lastGroupButton.innerHTML = '>>';
			lastGroupButton.onclick = function() {
				currentPage = Math.floor((totalPages - 1) / 5) * 5;
				loadReviews(totalPages - 1);
			};
			pagination.appendChild(lastGroupButton);
		}
	}

	function loadReviews(page) {
		const loadingDiv = document.getElementById('loading');
		loadingDiv.style.display = 'block';

		fetch('/api/reviews/myReviews?page=' + page + '&size=' + itemsPerPage)
				.then(function(response) { return response.json(); })
				.then(function(data) {
					console.log("Received data:", data);
					if (data && data.content) {
						displayReviews(data.content);
						updatePagination(data.totalElements);
					}
					loadingDiv.style.display = 'none';
				})
				.catch(function(error) {
					console.error('Error loading reviews:', error);
					loadingDiv.style.display = 'none';
				});
	}

	// 초기 로드
	loadReviews(0);

	function readMore(reviewId) {
		console.error('자세히 보기:', reviewId);
		// 자세히 보기 구현
	}
	</script>
	
</body>
</html>