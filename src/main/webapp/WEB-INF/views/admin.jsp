<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GPS 관리자</title>
    <style>
        <%@include file="adminStyle.jsp"%>
    </style>

</head>
<body>

<!-- 사이드바 -->
<div class="sidebar">
    <div class="logo">
        <a href="redirect:/">
            <img src="../image/gpsLogo.svg" alt="GPS Logo">
        </a>
    </div>

    <div class="profile-section">
        <div class="profile-image">
            <c:choose>
                <c:when test="${not empty sessionScope.profileImage}">
                    <img src="<%= session.getAttribute("profile_img") != null ? session.getAttribute("profile_img") : "../image/myPage_image.png" %>" alt="myPage_image" title="myPage_image" class="picture">
                </c:when>
                <c:otherwise>
                    <img src="../image/logo.png" alt="Admin Profile" id="adminProfileImage">
                </c:otherwise>
            </c:choose>
        </div>
        <div class="profile-info">
            <div class="profile-name" id="adminNickname">${sessionScope.nickname}</div>
            <c:if test="${sessionScope.authority eq 'ADMIN'}">
                <span class="admin-badge">ADMIN</span>
            </c:if>
        </div>
    </div>

    <ul class="nav-menu">
        <li class="nav-item">
            <a href="#" class="nav-link">
                <i class="fas fa-chart-line"></i>통계
            </a>
        </li>
        <li class="nav-item">
            <a href="#" class="nav-link">
                <i class="fas fa-store"></i>시설 관리
            </a>
        </li>
        <li class="nav-item">
            <a href="#" class="nav-link">
                <i class="fas fa-users"></i>회원 관리
            </a>
        </li>
        <li class="nav-item">
            <a href="#" class="nav-link", onclick="loadReviews()">
                <i class="fas fa-edit"></i>리뷰 관리
            </a>
        </li>
        <li class="nav-item">
            <a href="#" class="nav-link">
                <i class="fas fa-comment"></i>댓글 관리
            </a>
        </li>
    </ul>
    <button class="logout-btn" onclick="logout()">
        <i class="fas fa-sign-out-alt"></i>로그아웃
    </button>
</div>

<!-- 메인 컨텐츠 -->
<div class="main-content">
    <div class="header">
        <h2>신규 업데이트</h2>
        <div class="buttons">
            <button class="btn btn-primary">변경 저장</button>
            <button class="btn btn-secondary">나가기</button>
        </div>
    </div>

    <!-- 차트 컨테이너들 -->
    <div class="chart-container">
        <div class="chart-header">
            <span class="chart-title">접속자 평균</span>
            <span class="chart-period">월별 통계</span>
        </div>
        <div class="chart" id="visitorChart"></div>
    </div>

    <div class="chart-container">
        <div class="chart-header">
            <span class="chart-title">전체 조회수</span>
            <span class="chart-period">일별 통계</span>
        </div>
        <div class="chart" id="viewsChart"></div>
    </div>

    <div class="chart-container">
        <div class="chart-header">
            <span class="chart-title">전체 댓글수</span>
            <span class="chart-period">일별 통계</span>
        </div>
        <div class="chart" id="commentsChart"></div>
    </div>
</div>


<!-- Font Awesome -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/js/all.min.js"></script>

<script>

    document.addEventListener('DOMContentLoaded', function() {
        const navItems = document.querySelectorAll('.nav-item');

        navItems.forEach(item => {
            const link = item.querySelector('.nav-link');  // nav-link 클래스 사용
            const subMenu = item.querySelector('.sub-menu');

            if (subMenu) {  // 서브메뉴가 있는 항목만
                link.addEventListener('click', function(e) {
                    e.preventDefault();  // 기본 링크 동작 방지
                    item.classList.toggle('active');

                    // 다른 열린 메뉴 닫기 (선택사항)
                    navItems.forEach(otherItem => {
                        if (otherItem !== item && otherItem.classList.contains('active')) {
                            otherItem.classList.remove('active');
                        }
                    });
                });
            }
        });
    });

    // 프로필 정보 로드 스크립트
    document.addEventListener('DOMContentLoaded', async function() {
        try {
            // 1. 현재 로그인된 사용자 정보 가져오기
            const userResponse = await fetch('/api/user-info', {
                credentials: 'include',
                headers: {
                    'Accept': 'application/json'
                }
            });

            if (!userResponse.ok) {
                throw new Error('로그인이 필요합니다.');
            }

            const userData = await userResponse.json();

            // 프로필 정보 업데이트
            updateAdminProfile({
                name: userData.name,
                userId: userData.userID
            });

        } catch (error) {
            console.error('프로필 로드 실패 : ', error);
        }
    });

    function updateAdminProfile(data) {
        // 닉네임 업데이트
        const profileName = document.getElementById('adminNickname');
        if (profileName && data.name) {
            profileName.textContent = data.name;
        }
    }

    // 로그아웃 처리 함수
    async function logout() {
        try {
            const response = await fetch('/api/logout', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include' // 쿠키 포함
            });

            if (response.ok) {
                // 로그아웃 성공 시 로그인 페이지로 리다이렉트
                window.location.href = '/api/login';
            } else {
                alert('로그아웃 처리 중 오류가 발생했습니다.');
            }
        } catch (error) {
            console.error('로그아웃 에러:', error);
            alert('로그아웃 처리 중 오류가 발생했습니다.');
        }
    }

    let allReviews = [];  // 전체 리뷰 데이터 저장
    let currentPage = 0;  // 현재 페이지
    const itemsPerPage = 24;  // 페이지당 아이템 수

    // 리뷰 관리 링크 클릭 시 호출되는 함수
    async function loadReviews() {
        const mainContent = document.querySelector('.main-content');
        if (!mainContent) {
            console.error('main-content element not found');
            return;
        }

        mainContent.innerHTML = `
        <div class="header">
            <h2>리뷰 관리</h2>
            <div class="buttons">
                <button class="btn btn-primary">선택 삭제</button>
                <button class="btn btn-secondary">나가기</button>
            </div>
        </div>
        <div class="review-list"></div>
        <div class="pagination"></div>
    `;

        try {
            const response = await fetch('/api/reviews/all');
            const reviews = await response.json();
            console.log("API Response:", reviews);  // API 응답 확인
            displayReviews(reviews);
        } catch (error) {
            console.error('리뷰 로드 실패:', error);
        }
    }


    function displayReviews(reviews) {
        console.log("All reviews:", reviews);  // 전체 데이터 확인

        const reviewList = document.querySelector('.review-list');
        if (!reviewList) {
            console.error('review-list element not found');
            return;
        }

        reviewList.innerHTML = '';

        const leftColumn = document.createElement('div');
        leftColumn.className = 'review-column left';
        const rightColumn = document.createElement('div');
        rightColumn.className = 'review-column right';

        const startIndex = currentPage * itemsPerPage;
        const endIndex = startIndex + itemsPerPage;
        const currentReviews = reviews.slice(startIndex, endIndex);

        currentReviews.forEach((review, index) => {
            const reviewElement = document.createElement('div');
            reviewElement.className = 'review-item';
            reviewElement.innerHTML = `
            <input type="checkbox" class="review-select">
            <img src="/image/logo.png" class="review-img">
            <div class="review-text">${review.comment ? review.comment.substring(0, 30) + (review.comment.length > 30 ? '...' : '') : ''}</div>
            <div class="review-info">
                <span class="review-writer">${review.userName || ''}</span>
                <span class="review-date">${review.addedAt ? review.addedAt.split('T')[0] : ''}</span>
            </div>
        `;

            if (index < 12) {
                leftColumn.appendChild(reviewElement);
            } else {
                rightColumn.appendChild(reviewElement);
            }
        });

        reviewList.appendChild(leftColumn);
        reviewList.appendChild(rightColumn);

        updatePagination(reviews.length);
    }

    function updatePagination(totalItems) {
        const pagination = document.querySelector('.pagination');
        if (!pagination) {
            console.error('pagination element not found');
            return;
        }

        const totalPages = Math.ceil(totalItems / itemsPerPage);
        pagination.innerHTML = '';

        // 이전 버튼
        const prevButton = document.createElement('button');
        prevButton.innerHTML = '<';
        prevButton.disabled = currentPage === 0;
        prevButton.onclick = () => {
            if (currentPage > 0) {
                currentPage--;
                displayReviews(allReviews);
            }
        };
        pagination.appendChild(prevButton);

        // 페이지 번호
        for (let i = 0; i < totalPages; i++) {
            const pageButton = document.createElement('button');
            pageButton.textContent = i + 1;
            pageButton.className = i === currentPage ? 'active' : '';
            pageButton.onclick = () => {
                currentPage = i;
                displayReviews(allReviews);
            };
            pagination.appendChild(pageButton);
        }

        // 다음 버튼
        const nextButton = document.createElement('button');
        nextButton.innerHTML = '>';
        nextButton.disabled = currentPage === totalPages - 1;
        nextButton.onclick = () => {
            if (currentPage < totalPages - 1) {
                currentPage++;
                displayReviews(allReviews);
            }
        };
        pagination.appendChild(nextButton);
    }





</script>
</body>
</html>