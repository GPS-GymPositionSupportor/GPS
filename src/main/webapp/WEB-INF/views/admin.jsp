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
        <a href="/api/admin">
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
            <a href="/api/admin" class="nav-link">
                <i class="fas fa-chart-line"></i>통계
            </a>
        </li>
        <li class="nav-item">
            <a href="#" class="nav-link", onclick="loadGyms()">
                <i class="fas fa-store"></i>시설 관리
            </a>
        </li>
        <li class="nav-item">
            <a href="/" class="nav-link">
                <i class="fas fa-users"></i>회원 관리
            </a>
        </li>
        <li class="nav-item">
            <a href="#"  class="nav-link", onclick="loadReviewList()">
                <i class="fas fa-edit"></i>리뷰 관리
            </a>
        </li>
        <li class="nav-item">
            <a href="/" class="nav-link">
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

    async function loadGyms() {
        const mainContent = document.querySelector('.main-content');
        mainContent.innerHTML = `
       <div class="header">
           <h2>시설 관리</h2>
           <div class="search-bar">
               <input type="text" placeholder="찾으시는 시설이 있나요?" class="search-input">
               <button class="search-btn">검색</button>
           </div>
           <div class="buttons">  <!-- 버튼들을 div로 묶어서 관리 -->
                <button class="btn btn-primary">시설 추가</button>
                <button class="btn btn-primary">시설 삭제</button>
           </div>
       </div>
       <div class="gym-table">
           <table>
               <thead>
                   <tr>
                       <th><input type="checkbox" id="selectAll"></th>
                       <th>사진</th>
                       <th>시설명</th>
                       <th>주소</th>
                       <th>운영시간</th>
                       <th>홈페이지</th>
                       <th>전화번호</th>
                       <th>생성자</th>
                       <th>생성일</th>
                       <th>평점</th>
                   </tr>
               </thead>
               <tbody id="gymList"></tbody>
           </table>
       </div>
       <div class="pagination"></div>
       `;

        loadGymList();
    }

    async function loadGymList(page = 0, size = 12) {
        try {
            const response = await fetch('/api/showgyms?page=' + page + '&size=' + size, {
                credentials: 'include'
            });
            const data = await response.json();
            displayGyms(data.content, page);  // 페이지 번호 전달
            updateGymPagination(data.totalElements);
        } catch (error) {
            console.error('시설 로드 실패:', error);
        }
    }

    function displayGyms(gyms, currentPage) {
        const gymList = document.getElementById('gymList');
        const itemsPerPage = 12;
        const startIndex = currentPage * itemsPerPage;
        const endIndex = startIndex + itemsPerPage;
        const currentGyms = gyms;

        gymList.innerHTML = '';

        currentGyms.forEach(gym => {
            const row = document.createElement('tr');
            const date = gym.gCreatedAt ? gym.gCreatedAt.split('T')[0] : '-';
            const rating = gym.rating ? (gym.rating < 1 ? '0.0' : gym.rating.toFixed(1)) : '0.0';


            // 체크박스
            const checkboxCell = document.createElement('td');
            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.className = 'gym-select';
            checkbox.dataset.gymId = gym.gymId;
            checkboxCell.appendChild(checkbox);

            // 이미지
            const imgCell = document.createElement('td');
            const img = document.createElement('img');
            img.src = '../image/logo.png';  // 기본 이미지로 설정
            img.style.width = '25px';
            img.style.height = '25px';
            img.style.objectFit = 'cover';
            imgCell.appendChild(img);



            // 각 셀 생성
            const cells = [
                { content: gym.gname || '-' },
                { content: gym.address || '-' },
                { content: gym.openHour || '-' },
                { content: gym.homepage || '-' },
                { content: gym.phone || '-' },
                { content: gym.gCreatedBy || '-' },
                { content: date },
                { content: rating }
            ];

            row.appendChild(checkboxCell);
            row.appendChild(imgCell);

            cells.forEach(cellData => {
                const cell = document.createElement('td');
                cell.textContent = cellData.content;
                row.appendChild(cell);
            });



            row.insertBefore(imgCell, row.firstChild);
            row.insertBefore(checkboxCell, row.firstChild);
            gymList.appendChild(row);
        });

        updateGymPagination(gyms.totalElements);

    }

    function formatDate(dateString) {
        return new Date(dateString).toLocaleDateString('ko-KR');
    }





    let allReviews = [];  // 전체 리뷰 데이터 저장

    // 리뷰 관리 링크 클릭 시 호출되는 함수
    async function loadReviewList(page = 0) {
        const mainContent = document.querySelector('.main-content');
        mainContent.innerHTML = `
        <div class="header">
            <h2>리뷰 관리</h2>
            <div class="buttons">
                <button class="btn btn-primary" onclick="deleteSelectedReviews()">선택 삭제</button>
            </div>
        </div>
        <div class="review-list"></div>
        <div class="pagination"></div>
    `;

        try {
            const response = await fetch('/api/reviews/all?page=' + page + '&size=24');
            const data = await response.json();
            console.log("받은 데이터:", data); // 데이터 확인

            if (data && data.content) {
                displayReviews(data.content);
                updateReviewPagination(data.totalElements);
            }
        } catch (error) {
            console.error('리뷰 로드 실패:', error);
        }
    }

    function displayReviews(reviews, currentPage) {
        const reviewList = document.querySelector('.review-list');
        if (!reviewList || !reviews) return;

        reviewList.innerHTML = '';

        const leftColumn = document.createElement('div');
        leftColumn.className = 'review-column left';
        const rightColumn = document.createElement('div');
        rightColumn.className = 'review-column right';

        reviews.forEach((reviewData, index) => {
            const reviewElement = document.createElement('div');
            reviewElement.className = 'review-item';
            reviewElement.setAttribute('data-review-id', reviewData.rid);
            reviewElement.setAttribute('data-gym-id', reviewData.gymId);

            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.className = 'review-select';

            const img = document.createElement('img');
            img.src = '/image/logo.png';
            img.className = 'review-img';

            const textDiv = document.createElement('div');
            textDiv.className = 'review-text';
            textDiv.textContent = reviewData.comment;

            const infoDiv = document.createElement('div');
            infoDiv.className = 'review-info';

            const writerSpan = document.createElement('span');
            writerSpan.className = 'review-writer';
            writerSpan.textContent = reviewData.userName;

            const dateSpan = document.createElement('span');
            dateSpan.className = 'review-date';
            dateSpan.textContent = reviewData.addedAt.split('T')[0];

            infoDiv.appendChild(writerSpan);
            infoDiv.appendChild(dateSpan);

            reviewElement.appendChild(checkbox);
            reviewElement.appendChild(img);
            reviewElement.appendChild(textDiv);
            reviewElement.appendChild(infoDiv);

            if (index < 12) {
                leftColumn.appendChild(reviewElement);
            } else {
                rightColumn.appendChild(reviewElement);
            }
        });

        reviewList.appendChild(leftColumn);
        reviewList.appendChild(rightColumn);
    }




    function displayGyms(gyms, currentPage) {
        const gymList = document.getElementById('gymList');
        const itemsPerPage = 12;
        const startIndex = currentPage * itemsPerPage;
        const endIndex = startIndex + itemsPerPage;
        const currentGyms = gyms;

        gymList.innerHTML = '';

        currentGyms.forEach(gym => {
            const row = document.createElement('tr');
            const date = gym.gCreatedAt ? gym.gCreatedAt.split('T')[0] : '-';
            const rating = gym.rating ? (gym.rating < 1 ? '0.0' : gym.rating.toFixed(1)) : '0.0';

            // 체크박스 셀
            const checkboxCell = document.createElement('td');
            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.className = 'gym-select';
            checkbox.dataset.gymId = gym.gymId;
            checkboxCell.appendChild(checkbox);

            // 이미지 셀
            const imgCell = document.createElement('td');
            const img = document.createElement('img');
            img.src = '../image/logo.png';
            img.className = 'gym-thumbnail';  // 클래스 추가
            imgCell.appendChild(img);

            // 나머지 셀들 생성
            const nameCell = document.createElement('td');
            nameCell.textContent = gym.gname || '-';

            const addressCell = document.createElement('td');
            addressCell.textContent = gym.address || '-';

            const hourCell = document.createElement('td');
            hourCell.textContent = gym.openHour || '-';

            const homepageCell = document.createElement('td');
            homepageCell.textContent = gym.homepage || '-';

            const phoneCell = document.createElement('td');
            phoneCell.textContent = gym.phone || '-';

            const creatorCell = document.createElement('td');
            creatorCell.textContent = gym.gCreatedBy || '-';

            const dateCell = document.createElement('td');
            dateCell.textContent = date;

            const ratingCell = document.createElement('td');
            ratingCell.textContent = rating;

            // 순서대로 append
            row.appendChild(checkboxCell);
            row.appendChild(imgCell);
            row.appendChild(nameCell);
            row.appendChild(addressCell);
            row.appendChild(hourCell);
            row.appendChild(homepageCell);
            row.appendChild(phoneCell);
            row.appendChild(creatorCell);
            row.appendChild(dateCell);
            row.appendChild(ratingCell);

            gymList.appendChild(row);
        });

        updateGymPagination(gyms.totalElements);
    }

    // 탭별 currentPage 상태 저장
    let pageState = {
        gyms: {
            totalPages: 0,
            currentPage: 0
        },
        reviews: {
            totalPages: 0,
            currentPage: 0
        }
    };

    let currentPageGyms = 0;
    let currentPage
    let currentPageReviews = 0;
    const itemsPerPage = 12;
    const pagesPerStep = 5;



    function updateGymPagination(totalItems) {
        const pagination = document.querySelector('.pagination');
        const totalPages = Math.ceil(totalItems / itemsPerPage);
        pagination.innerHTML = '';

        // 시작 페이지와 끝 페이지 계산
        let startPage = Math.floor(currentPageGyms / 5) * 5;
        let endPage = Math.min(startPage + 4, totalPages - 1);

        // '<<' 버튼 (첫 페이지 그룹으로)
        if(startPage > 0) {
            const firstGroupButton = document.createElement('button');
            firstGroupButton.innerHTML = '<<';
            firstGroupButton.onclick = () => {
                currentPageGyms = 0;
                loadGymList(0);
            };
            pagination.appendChild(firstGroupButton);
        }

        // '<' 버튼 (이전 페이지 그룹으로)
        if(startPage > 0) {
            const prevGroupButton = document.createElement('button');
            prevGroupButton.innerHTML = '<';
            prevGroupButton.onclick = () => {
                currentPageGyms = startPage - 5;
                loadGymList(currentPageGyms);
            };
            pagination.appendChild(prevGroupButton);
        }

        // 페이지 번호 버튼들
        for(let i = startPage; i <= endPage; i++) {
            const pageButton = document.createElement('button');
            pageButton.textContent = i + 1;
            pageButton.className = i === currentPageGyms ? 'active' : '';
            pageButton.onclick = () => {
                currentPageGyms = i;
                loadGymList(i);
            };
            pagination.appendChild(pageButton);
        }

        // '>' 버튼 (다음 페이지 그룹으로)
        if(endPage < totalPages - 1) {
            const nextGroupButton = document.createElement('button');
            nextGroupButton.innerHTML = '>';
            nextGroupButton.onclick = () => {
                currentPageGyms = startPage + 5;
                loadGymList(currentPageGyms);
            };
            pagination.appendChild(nextGroupButton);
        }

        // '>>' 버튼 (마지막 페이지 그룹으로)
        if(endPage < totalPages - 1) {
            const lastGroupButton = document.createElement('button');
            lastGroupButton.innerHTML = '>>';
            lastGroupButton.onclick = () => {
                currentPageGyms = Math.floor((totalPages - 1) / 5) * 5;
                loadGymList(currentPageGyms);
            };
            pagination.appendChild(lastGroupButton);
        }
    }


    function updateReviewPagination(totalItems) {
        const pagination = document.querySelector('.pagination');
        const totalPages = Math.floor(totalItems / itemsPerPage);
        pagination.innerHTML = '';

        // 1번 버튼
        const firstButton = document.createElement('button');
        firstButton.textContent = '1';
        firstButton.className = currentPageReviews === 0 ? 'active' : '';
        firstButton.onclick = () => {
            currentPageReviews = 0;
            loadReviewList(0);
        };
        pagination.appendChild(firstButton);

        // 2페이지부터 totalPages까지 버튼 생성
        for(let i = 1; i < totalPages; i++) {
            const pageButton = document.createElement('button');
            pageButton.textContent = i + 1;
            pageButton.className = i === currentPageReviews ? 'active' : '';
            pageButton.onclick = () => {
                currentPageReviews = i;
                loadReviewList(i);
            };
            pagination.appendChild(pageButton);
        }

        // 5페이지 이상일 때만 > 와 >> 버튼 표시
        if(totalPages > 5) {
            const nextButton = document.createElement('button');
            nextButton.innerHTML = '>';
            nextButton.onclick = () => {
                currentPageReviews = Math.min(currentPageReviews + 1, totalPages - 1);
                loadReviewList(currentPageReviews);
            };
            pagination.appendChild(nextButton);

            const lastButton = document.createElement('button');
            lastButton.innerHTML = '>>';
            lastButton.onclick = () => {
                currentPageReviews = totalPages - 1;
                loadReviewList(totalPages - 1);
            };
            pagination.appendChild(lastButton);
        }
    }







    async function deleteSelectedReviews() {
        const selectedCheckboxes = document.querySelectorAll('.review-select:checked');
        if (selectedCheckboxes.length === 0) {
            alert('삭제할 리뷰를 선택해주세요.');
            return;
        }

        if (confirm('선택한 리뷰를 삭제하시겠습니까?')) {
            for (const checkbox of selectedCheckboxes) {
                const reviewElement = checkbox.closest('.review-item');
                const reviewId = parseInt(reviewElement.getAttribute('data-review-id'));
                const gymId = parseInt(reviewElement.getAttribute('data-gym-id'));


                console.log('Deleting review:', { reviewId, gymId }); // 삭제 시도 로그

                try {
                    const response = await fetch("/api/reviews/" + reviewId + "/" + gymId, {
                        method: 'DELETE',
                        credentials: 'include'
                    });

                    if (!response.ok) {
                        throw new Error(`삭제 실패 (Status: ${response.status})`);
                    }
                } catch (error) {
                    console.error('리뷰 삭제 중 오류:', error);
                    alert('리뷰 삭제 중 오류가 발생했습니다.');
                    return;
                }
            }

            await loadReviewList(); // 비동기 처리
            alert('선택한 리뷰가 삭제되었습니다.');
        }
    }





</script>
</body>
</html>