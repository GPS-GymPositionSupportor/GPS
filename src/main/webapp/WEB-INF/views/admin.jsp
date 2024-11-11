<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
            <img src="../image/logo.png" alt="GPS Logo">
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
                <i class="fas fa-chart-line"></i> 통계
            </a>
        </li>
        <li class="nav-item">
            <a href="#" class="nav-link">
                <i class="fas fa-store"></i> 시설 관리
            </a>
            <ul class="sub-menu">
                <li><a href="#">헬스장 전체 리스트</a></li>
                <li><a href="#">수정 및 삭제</a></li>
            </ul>
        </li>
        <li class="nav-item">
            <a href="#" class="nav-link">
                <i class="fas fa-users"></i> 회원 관리
            </a>
            <ul class="sub-menu">
                <li><a href="#">회원 전체 리스트</a></li>
                <li><a href="#">수정 및 삭제</a></li>
            </ul>
        </li>
        <li class="nav-item">
            <a href="#" class="nav-link">
                <i class="fas fa-edit"></i> 리뷰 관리
            </a>
            <ul class="sub-menu">
                <li><a href="#">전체 리뷰</a></li>
                <li><a href="#">삭제</a></li>
            </ul>
        </li>
        <li class="nav-item">
            <a href="#" class="nav-link">
                <i class="fas fa-comment"></i> 댓글 관리
            </a>
            <ul class="sub-menu">
                <li><a href="#">전체 댓글</a></li>
                <li><a href="#">삭제</a></li>
            </ul>
        </li>
    </ul>
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

    <!-- 콘텐츠 그리드 -->
    <div class="content-grid">
        <!-- 접속자 평균 -->
        <div class="content-box">
            <h3>접속자 평균</h3>
            <div class="pagination">이번 달 통계</div>
        </div>

        <!-- 최근 댓글 -->
        <div class="content-box">
            <h3>최근 댓글</h3>
            <div class="pagination">최근 댓글</div>
        </div>

        <!-- 전체 조회수 -->
        <div class="content-box">
            <h3>전체 조회수</h3>
            <div class="pagination">이번 달 통계</div>
        </div>

        <!-- 전체 댓글수 -->
        <div class="content-box">
            <h3>전체 댓글수</h3>
            <div class="pagination">전체 통계</div>
        </div>
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
            const userId = userData.userId;

            // 2. 프로필 정보 가져오기
            const profileResponse = await fetch(`/api/member/${userId}/myProfile`, {
                credentials: 'include'
            });

            if (!profileResponse.ok) {
                throw new Error('프로필 정보를 불러올 수 없습니다.');
            }

            const profileData = await profileResponse.json();
            updateAdminProfile(profileData);

        } catch (error) {
            console.error('Error:', error);
        }
    });

    function updateAdminProfile(data) {
        // 프로필 이미지 업데이트
        const profileImage = document.getElementById('adminProfileImage');
        if (data.profileImage) {
            profileImage.src = data.profileImage;
        }

        // 닉네임 업데이트
        const profileName = document.getElementById('adminNickname');
        if (data.nickname) {
            profileName.textContent = data.nickname;
        }
    }


</script>
</body>
</html>