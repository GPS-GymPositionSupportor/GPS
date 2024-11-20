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
            <a href="#" class="nav-link" onclick="loadUsers()">
                <i class="fas fa-users"></i>회원 관리
            </a>
        </li>
        <li class="nav-item">
            <a href="#"  class="nav-link", onclick="loadReviewList()">
                <i class="fas fa-edit"></i>리뷰 관리
            </a>
        </li>
        <li class="nav-item">
            <a href="#" class="nav-link", onclick="loadCommentList()">
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
                <button class="btn btn-primary" onclick="showEditModal()">시설 수정</button>
                <button class="btn btn-primary" onclick="deleteSelectedGyms()">시설 삭제</button>
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
    let currentGyms = [];

    async function loadGymList(page = 0, size = 12) {
        try {
            const response = await fetch('/api/showgyms?page=' + page + '&size=' + size, {
                credentials: 'include'
            });
            const data = await response.json();
            currentGyms = data.content;
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
            row.className = 'gym-row';
            row.setAttribute('data-gym-id', gym.gymId);
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
            if (gym.gymImage && gym.gymImage.imageUrl) {
                // Google 드라이브 링크 체크
                if (gym.gymImage.imageUrl.includes('google.com')) {
                    img.src = '../image/logo.png';
                } else {
                    img.src = gym.gymImage.imageUrl;
                }
            } else {
                img.src = '../image/logo.png';
            }
            img.style.width = '50px';
            img.style.height = '50px';
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

    async function deleteSelectedGyms() {
        const selectedCheckboxes = document.querySelectorAll('.gym-select:checked');
        if (selectedCheckboxes.length === 0) {
            alert('삭제할 시설을 선택해주세요.');
            return;
        }

        if (confirm('선택한 시설을 삭제하시겠습니까?')) {
            for (const checkbox of selectedCheckboxes) {
                const gymElement = checkbox.closest('.gym-row');
                const gymId = parseInt(gymElement.getAttribute('data-gym-id'));

                console.log('Deleting gym:', { gymId }); // 삭제 시도 로그

                try {
                    const response = await fetch(`/api/` + gymId, {
                        method: 'DELETE',
                        credentials: 'include',
                    });

                    if (!response.ok) {
                        throw new Error(`삭제 실패 (Status: ` + response.status + ` )`);
                    }
                } catch (error) {
                    console.error('시설 삭제 중 오류:', error);
                    alert('시설 삭제 중 오류가 발생했습니다.');
                    return;
                }
            }

            await loadGymList(); // 삭제 후 목록 갱신
            alert('선택한 시설이 삭제되었습니다.');
        }
    }


    // 모달
    async function showEditModal() {
        const selectedCheckboxes = document.querySelectorAll('.gym-select:checked');
        if (selectedCheckboxes.length === 0) {
            alert('수정할 시설을 선택해주세요.');
            return;
        }

        if (selectedCheckboxes.length > 1) {
            alert('한 번에 하나의 시설만 수정할 수 있습니다.');
            return;
        }

        const gymRow = selectedCheckboxes[0].closest('.gym-row');
        const gymId = parseInt(gymRow.getAttribute('data-gym-id'));
        const gym = currentGyms.find(g => g.gymId === gymId);

        if (!gym) return;

        const modal = document.createElement('div');
        modal.className = 'modal';
        modal.innerHTML = ''
            + '<div class="modal-content">\n'
            + '    <div class="current-gym-image">\n'
            + '        <img src="' + (gym.gymImage?.imageUrl || '../image/logo.png') + '" alt="체육관 이미지" class="gym-edit-image">\n'
            + '        <input type="file" id="gymImageInput" accept="image/*" class="image-input">\n'
            + '        <label for="gymImageInput" class="image-upload-btn">\n'
            + '            <i class="fas fa-camera"></i> 이미지 변경\n'
            + '        </label>\n'
            + '    </div>\n'
            + '    <div class="basic-info">\n'
            + '        <div class="form-group">\n'
            + '            <label><i class="fas fa-store"></i> 시설명</label>\n'
            + '            <input type="text" class="form-control" id="editGymName" value="' + (gym.gname || '') + '">\n'
            + '        </div>\n'
            + '        <div class="form-group">\n'
            + '            <label><i class="fas fa-map-marker-alt"></i> 주소</label>\n'
            + '            <input type="text" class="form-control" id="editAddress" value="' + (gym.address || '') + '">\n'
            + '        </div>\n'
            + '        <div class="form-group">\n'
            + '            <label><i class="fas fa-phone"></i> 전화번호</label>\n'
            + '            <input type="text" class="form-control" id="editPhone" value="' + (gym.phone || '') + '">\n'
            + '        </div>\n'
            + '        <div class="form-group">\n'
            + '            <label><i class="fas fa-globe"></i> 홈페이지</label>\n'
            + '            <input type="text" class="form-control" id="editHomepage" value="' + (gym.homepage || '') + '">\n'
            + '        </div>\n'
            + '    </div>\n'
            + '    <div class="operation-info">\n'
            + '        <div class="form-group">\n'
            + '            <label><i class="fas fa-clock"></i> 영업시간</label>\n'
            + '            <textarea class="form-control operation-hours" id="editOpenHour">' + (gym.openHour || '') + '</textarea>\n'
            + '        </div>\n'
            + '        <div class="fixed-info">\n'
            + '            <div class="fixed-info-item">\n'
            + '                <span>생성자</span>\n'
            + '                <span>' + (gym.gCreatedBy || '-') + '</span>\n'
            + '            </div>\n'
            + '            <div class="fixed-info-item">\n'
            + '                <span>생성일</span>\n'
            + '                <span>' + (gym.gCreatedAt ? gym.gCreatedAt.split('T')[0] : '-') + '</span>\n'
            + '            </div>\n'
            + '            <div class="fixed-info-item">\n'
            + '                <span>평점</span>\n'
            + '                <span>' + (gym.rating ? gym.rating.toFixed(1) : '0.0') + '</span>\n'
            + '            </div>\n'
            + '        </div>\n'
            + '    </div>\n'
            + '    <div class="edit-buttons">\n'
            + '        <button class="btn-edit btn-save" onclick="saveGymChanges(' + gym.gymId + ')">변경 저장</button>\n'
            + '        <button class="btn-edit btn-cancel" onclick="closeModal()">나가기</button>\n'
            + '    </div>\n'
            + '</div>';

        document.body.appendChild(modal);
        modal.style.display = 'block';
    }

    function closeModal() {
        const modal = document.querySelector('.modal');
        if (modal) {
            modal.remove();
        }
    }

    async function saveGymChanges(gymId) {
        const updatedGym = {
            gname: document.getElementById('editGymName').value,
            address: document.getElementById('editAddress').value,
            phone: document.getElementById('editPhone').value,
            homepage: document.getElementById('editHomepage').value,
            openHour: document.getElementById('editOpenHour').value
        };

        try {
            const response = await fetch(`/api/gyms/` + gymId, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updatedGym),
                credentials: 'include'
            });

            if (response.ok) {
                alert('시설 정보가 수정되었습니다.');
                closeModal();
                loadGymList();
            } else {
                throw new Error('수정 실패');
            }
        } catch (error) {
            console.error('시설 수정 중 오류:', error);
            alert('시설 수정 중 오류가 발생했습니다.');
        }
    }

    // 이미지 미리보기 기능
    document.addEventListener('change', function(e) {
        if (e.target && e.target.id === 'gymImageInput') {
            const file = e.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    document.querySelector('.gym-edit-image').src = e.target.result;
                };
                reader.readAsDataURL(file);
            }
        }
    });

    // 댓글 관리 메뉴 클릭시 호출되는 함수
    async function loadCommentList() {
        const mainContent = document.querySelector('.main-content');
        mainContent.innerHTML = ''
            + '<div class="header">'
            + '    <h2>댓글 관리</h2>'
            + '    <div class="buttons">'
            + '        <button class="btn btn-primary" onclick="deleteSelectedComments()">선택 삭제</button>'
            + '    </div>'
            + '</div>'
            + '<div class="review-list"></div>'
            + '<div class="pagination"></div>';

        try {
            const response = await fetch('/api/reviews/comments/all?page=0', {
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                }
            });
            const data = await response.json();
            console.log('댓글 데이터:', data);

            if (data && data.content) {
                displayComments(data.content);
                updateCommentPagination(data.totalElements, 0);
            }
        } catch (error) {
            console.error('댓글 로드 실패:', error);
        }
    }

    function displayComments(comments) {
        const commentList = document.querySelector('.review-list');
        if (!commentList) return;

        commentList.innerHTML = '';
        const leftColumn = document.createElement('div');
        leftColumn.className = 'review-column left';
        const rightColumn = document.createElement('div');
        rightColumn.className = 'review-column right';

        comments.forEach((comment, index) => {
            const commentElement = document.createElement('div');
            commentElement.className = 'review-item';
            commentElement.setAttribute('data-comment-id', comment.cid);
            commentElement.setAttribute('data-review-id', comment.reviewId);

            // 체크박스, 텍스트, 작성자 정보를 담을 컨테이너들
            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.className = 'review-select';

            const textDiv = document.createElement('div');
            textDiv.className = 'review-text';
            textDiv.textContent = comment.comment;

            const infoDiv = document.createElement('div');
            infoDiv.className = 'review-info';

            const writerSpan = document.createElement('span');
            writerSpan.className = 'review-writer';
            writerSpan.textContent = comment.userName;

            const dateSpan = document.createElement('span');
            dateSpan.className = 'review-date';
            dateSpan.textContent = new Date(comment.createdAt).toLocaleDateString();

            // 요소들 조립
            infoDiv.appendChild(writerSpan);
            infoDiv.appendChild(dateSpan);

            commentElement.appendChild(checkbox);
            commentElement.appendChild(textDiv);
            commentElement.appendChild(infoDiv);

            // 왼쪽/오른쪽 컬럼에 분배
            if (index < 12) {
                leftColumn.appendChild(commentElement);
            } else {
                rightColumn.appendChild(commentElement);
            }
        });

        commentList.appendChild(leftColumn);
        commentList.appendChild(rightColumn);
    }

    function updateCommentPagination(totalElements, currentPage) {
        const pagination = document.querySelector('.pagination');
        if (!pagination) return;

        const totalPages = Math.ceil(totalElements / 24);
        pagination.innerHTML = '';

        // 첫 페이지
        const firstButton = document.createElement('button');
        firstButton.textContent = '<<';
        firstButton.onclick = () => loadComments(0);
        pagination.appendChild(firstButton);

        // 이전 페이지
        if (currentPage > 0) {
            const prevButton = document.createElement('button');
            prevButton.textContent = '<';
            prevButton.onclick = () => loadComments(currentPage - 1);
            pagination.appendChild(prevButton);
        }

        // 페이지 번호
        for (let i = Math.max(0, currentPage - 2); i <= Math.min(currentPage + 2, totalPages - 1); i++) {
            const button = document.createElement('button');
            button.textContent = i + 1;
            button.className = i === currentPage ? 'active' : '';
            button.onclick = () => loadComments(i);
            pagination.appendChild(button);
        }

        // 다음 페이지
        if (currentPage < totalPages - 1) {
            const nextButton = document.createElement('button');
            nextButton.textContent = '>';
            nextButton.onclick = () => loadComments(currentPage + 1);
            pagination.appendChild(nextButton);
        }

        // 마지막 페이지
        const lastButton = document.createElement('button');
        lastButton.textContent = '>>';
        lastButton.onclick = () => loadComments(totalPages - 1);
        pagination.appendChild(lastButton);
    }

    async function loadComments(page = 0) {
        try {
            const response = await fetch('/api/reviews/comments/all?page=' + page);
            const data = await response.json();
            if (data && data.content) {
                displayComments(data.content);
                updateCommentPagination(data.totalElements, page);
            }
        } catch (error) {
            console.error('댓글 로드 실패:', error);
        }
    }

    async function deleteSelectedComments() {
        const selectedComments = document.querySelectorAll('.review-select:checked');
        if (selectedComments.length === 0) {
            alert('삭제할 댓글을 선택해주세요.');
            return;
        }

        if (confirm('선택한 댓글을 삭제하시겠습니까?')) {
            try {
                for (const checkbox of selectedComments) {
                    const commentElement = checkbox.closest('.review-item');
                    const reviewId = commentElement.getAttribute('data-review-id');
                    const commentId = commentElement.getAttribute('data-comment-id');

                    if (!reviewId || !commentId) {
                        console.error('리뷰 ID 또는 댓글 ID를 찾을 수 없습니다.');
                        continue;
                    }

                    const response = await fetch(`/api/reviews/` + reviewId + `/comments/` + commentId, {
                        method: 'DELETE',
                        credentials: 'include'
                    });

                    if (!response.ok) {
                        throw new Error(`댓글 삭제 실패 (Status: ${response.status})`);
                    }
                }

                alert('선택한 댓글이 삭제되었습니다.');
                loadComments(0); // 댓글 목록 새로고침
            } catch (error) {
                console.error('댓글 삭제 중 오류:', error);
                alert('댓글 삭제 중 오류가 발생했습니다.');
            }
        }
    }

    document.addEventListener('DOMContentLoaded', function() {
        loadComments(0);
    });


    async function loadUsers() {
        const mainContent = document.querySelector('.main-content');

        mainContent.innerHTML = `
       <div class="header">
           <h2>회원 관리</h2>
           <div class="search-bar">
               <input type="text" placeholder="찾으시는 회원이 있나요?" class="search-input">
               <button class="search-btn">검색</button>
           </div>
           <div class="buttons">
                <button class="btn btn-primary" onclick="deleteSelectedUsers()">회원 삭제</button>
           </div>
       </div>
       <div class="user-table">
           <table>
               <thead>
                   <tr>
                       <th><input type="checkbox" id="selectAll"></th>
                       <th>ID</th>
                       <th>성명</th>
                       <th>닉네임</th>
                       <th>이메일</th>
                       <th>생년월일</th>
                       <th>성별</th>
                       <th>생성일</th>
                       <th>최근 접속일</th>
                       <th>권한</th>
                   </tr>
               </thead>
               <tbody id="userList"></tbody>
           </table>
       </div>
       <div class="pagination"></div>
    `;

        loadUserList();
    }

    let currentUsers = [];

    async function loadUserList(page = 0, size = 12) {
        try {
            const response = await fetch('/api/members?page=' + page + '&size=' + size, {
                credentials: 'include'
            });
            const data = await response.json();
            console.log("받은 회원 데이터 : {}", data);
            currentUsers = data.content;
            displayUsers(data.content, page);
            updateUserPagination(data.totalElements);
        } catch (error) {
            console.error('회원 목록 로드 실패:', error);
        }
    }

    function displayUsers(users, currentPage) {
        const userList = document.getElementById('userList');
        if (!userList || !users) return;

        userList.innerHTML = '';

        users.forEach(user => {
            const row = document.createElement('tr');
            row.className = 'user-row';
            row.setAttribute('data-user-id', user.mid);

            // 체크박스
            const checkboxCell = document.createElement('td');
            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.className = 'user-select';
            checkbox.dataset.userId = user.mId;
            checkboxCell.appendChild(checkbox);
            row.appendChild(checkboxCell);

            // 회원 정보 셀들
            const cells = [
                { content: user.mid || '-' },
                { content: user.name || '-' },
                { content: user.nickname || '-' },
                { content: user.email || '-' },
                { content: user.birth || '-' },
                { content: user.gender || '-' },
                { content: user.mcreatedAt ? new Date(user.mcreatedAt).toLocaleDateString() : '-' },
                { content: user.lastLogin ? new Date(user.lastLogin).toLocaleDateString() : '-' },
                { content: user.authority || '-' }
            ];

            cells.forEach(cellData => {
                const cell = document.createElement('td');
                cell.textContent = cellData.content;
                row.appendChild(cell);
            });

            userList.appendChild(row);
        });
    }

    function updateUserPagination(totalItems) {
        const pagination = document.querySelector('.pagination');
        const totalPages = Math.ceil(totalItems / itemsPerPage);
        pagination.innerHTML = '';

        // 시작 페이지와 끝 페이지 계산
        let startPage = Math.floor(currentPageUsers / 5) * 5;
        let endPage = Math.min(startPage + 4, totalPages - 1);

        // '<<' 버튼 (첫 페이지 그룹으로)
        if(startPage > 0) {
            const firstGroupButton = document.createElement('button');
            firstGroupButton.innerHTML = '<<';
            firstGroupButton.onclick = () => {
                currentPageUsers = 0;
                loadUserList(0);
            };
            pagination.appendChild(firstGroupButton);
        }

        // '<' 버튼 (이전 페이지 그룹으로)
        if(startPage > 0) {
            const prevGroupButton = document.createElement('button');
            prevGroupButton.innerHTML = '<';
            prevGroupButton.onclick = () => {
                currentPageUsers = startPage - 5;
                loadUserList(currentPageUsers);
            };
            pagination.appendChild(prevGroupButton);
        }

        // 페이지 번호 버튼들
        for(let i = startPage; i <= endPage; i++) {
            const pageButton = document.createElement('button');
            pageButton.textContent = i + 1;
            pageButton.className = i === currentPageUsers ? 'active' : '';
            pageButton.onclick = () => {
                currentPageUsers = i;
                loadUserList(i);
            };
            pagination.appendChild(pageButton);
        }

        // '>' 버튼 (다음 페이지 그룹으로)
        if(endPage < totalPages - 1) {
            const nextGroupButton = document.createElement('button');
            nextGroupButton.innerHTML = '>';
            nextGroupButton.onclick = () => {
                currentPageUsers = startPage + 5;
                loadUserList(currentPageUsers);
            };
            pagination.appendChild(nextGroupButton);
        }

        // '>>' 버튼 (마지막 페이지 그룹으로)
        if(endPage < totalPages - 1) {
            const lastGroupButton = document.createElement('button');
            lastGroupButton.innerHTML = '>>';
            lastGroupButton.onclick = () => {
                currentPageUsers = Math.floor((totalPages - 1) / 5) * 5;
                loadUserList(currentPageUsers);
            };
            pagination.appendChild(lastGroupButton);
        }
    }

    async function deleteSelectedUsers() {
        const selectedCheckboxes = document.querySelectorAll('.user-select:checked');
        if (selectedCheckboxes.length === 0) {
            alert('삭제할 회원을 선택해주세요.');
            return;
        }

        if (confirm('선택한 회원을 삭제하시겠습니까?')) {
            try {
                for (const checkbox of selectedCheckboxes) {
                    const userId = checkbox.dataset.userId;

                    const response = await fetch(`/api/users/${userId}`, {
                        method: 'DELETE',
                        credentials: 'include'
                    });

                    if (!response.ok) {
                        throw new Error(`회원 삭제 실패 (Status: ${response.status})`);
                    }
                }

                alert('선택한 회원이 삭제되었습니다.');
                loadUserList(currentPageUsers);
            } catch (error) {
                console.error('회원 삭제 중 오류:', error);
                alert('회원 삭제 중 오류가 발생했습니다.');
            }
        }
    }

    // 전역 변수 추가
    let currentPageUsers = 0;





</script>
</body>
</html>