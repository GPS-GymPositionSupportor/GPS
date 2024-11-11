* {
margin: 0;
padding: 0;
box-sizing: border-box;
}

body {
display: flex;
min-height: 100vh;
background-color: #f0f0f0;
font-family: 'Noto Sans KR', sans-serif;
}


/* 사이드바 스타일 */
.sidebar {
width: 240px;
background-color: #333;
color: #fff;
padding: 20px 0;
border-right: 1px solid #ddd;
}

.logo {
padding: 0 20px 20px;
border-bottom: 1px solid #444;
}

.logo img {
max-width: 100%;
height: auto;
}

.nav-menu .nav-item {
position: relative;
cursor: pointer;
}

.nav-item > a {
display: flex;
align-items: center;
justify-content: space-between;
padding: 12px 20px;
color: #fff;
text-decoration: none;
}

/* 삼각형 아이콘 */
.nav-item > a::after {
content: '▼';
font-size: 10px;
transition: transform 0.3s ease;
}

/* 펼쳐진 상태의 삼각형 회전 */
.nav-item.active > a::after {
transform: rotate(180deg);
}


.nav-item > a i {
width: 20px;
text-align: center;
}

.nav-item a span {
flex: 1;
}


.nav-link {
display: flex;
align-items: center;
padding: 12px 20px;
color: #fff;
text-decoration: none;
transition: background-color 0.3s;
position: relative;
}

.nav-link:hover {
background-color: #444;
}

.nav-link i {
margin-right: 10px;
width: 20px;
}

/* 서브메뉴 스타일 */
.sub-menu {
list-style: none;
padding-left: 42px;
max-height: 0;
overflow: hidden;
transition: max-height 0.3s ease;
background-color: #2b2b2b;
}

/* 펼쳐진 상태의 서브메뉴 */
.nav-item.active .sub-menu {
max-height: 500px; /* 적당한 값으로 조정 */
}

.sub-menu li {
position: relative;
padding: 0;
}

/* 서브메뉴 링크 스타일 수정 */
.sub-menu li a {
text-decoration: none;  /* 밑줄 제거 */
color: #b3b3b3;        /* 연한 회색으로 변경 */
display: block;        /* 전체 영역 클릭 가능하도록 */
padding: 8px 0;        /* 상하 여백 */
transition: color 0.2s ease;  /* 부드러운 색상 변경 효과 */
}

/* 호버 효과 */
.sub-menu li a:hover {
color: #ffffff;  /* 흰색으로 변경 */
}

/* 상단 업데이트 영역 */
.update-section {
background-color: #fff;
border-radius: 4px;
padding: 15px;
margin-bottom: 20px;
}

.update-section h2 {
font-size: 16px;
color: #333;
margin-bottom: 10px;
}

/* 메인 컨텐츠 영역 */
.main-content {
flex: 1;
padding: 20px;
background-color: #f0f0f0;
}

/* 헤더 영역 */
.header {
display: flex;
justify-content: space-between;
align-items: center;
background-color: #fff;
padding: 15px 20px;
border-radius: 4px;
margin-bottom: 20px;
border: 1px solid #ddd;
}

.header h2 {
font-size: 18px;
color: #333;
font-weight: 500;
}

.buttons {
display: flex;
gap: 10px;
}

.btn {
padding: 8px 16px;
border: none;
border-radius: 4px;
cursor: pointer;
font-size: 14px;
font-weight: 500;
transition: opacity 0.2s;
}

.btn:hover {
opacity: 0.9;
}

.btn-primary {
background-color: #ff0000;
color: white;
}

.btn-secondary {
background-color: #6c757d;
color: white;
}

/* 콘텐츠 그리드 */
.content-grid {
display: grid;
grid-template-columns: repeat(2, 1fr);
gap: 20px;
}

.content-box {
background-color: #fff;
border-radius: 4px;
padding: 20px;
min-height: 300px;
position: relative;
border: 1px solid #ddd;
transition: transform 0.2s, box-shadow 0.2s;
}

.content-box h3 {
font-size: 16px;
color: #333;
margin-bottom: 15px;
font-weight: 500;
padding-bottom: 10px;
border-bottom: 1px solid #eee;
}

.pagination {
position: absolute;
top: 20px;
right: 20px;
font-size: 12px;
color: #888;
}

/* 반응형 디자인 */
@media (max-width: 1024px) {
.content-grid {
grid-template-columns: 1fr;
}
}

@media (max-width: 768px) {
.sidebar {
width: 200px;
}

.main-content {
padding: 15px;
}

.header {
flex-direction: column;
gap: 10px;
}

.buttons {
width: 100%;
justify-content: flex-end;
}
}

/* 호버 효과 */
.content-box {
transition: transform 0.2s, box-shadow 0.2s;
}

.content-box:hover {
transform: translateY(-2px);
box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

/* 스크롤바 스타일 */
::-webkit-scrollbar {
width: 8px;
}

::-webkit-scrollbar-track {
background: #f1f1f1;
}

::-webkit-scrollbar-thumb {
background: #888;
border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
background: #555;
}

/* 프로필 섹션 스타일 */
.profile-section {
padding: 20px;
margin: 10px 20px 20px 20px;
border-bottom: 1px solid #444;
display: flex;
align-items: center;
gap: 15px;
}

/* 프로필 이미지 컨테이너 */
.profile-image {
width: 50px;
height: 50px;
border-radius: 50%;
overflow: hidden;
border: 2px solid #fff;
flex-shrink: 0;
}

/* 프로필 이미지 */
.profile-image img {
width: 100%;
height: 100%;
object-fit: cover;
}

/* 프로필 정보 */
.profile-info {
flex-grow: 1;
}

/* 프로필 이름 */
.profile-name {
color: #fff;
font-size: 16px;
font-weight: 500;
margin-bottom: 4px;
}

/* ADMIN 배지 */
.admin-badge {
background-color: rgba(255, 0, 0, 0.1);
color: #ff0000;
padding: 2px 8px;
border-radius: 4px;
font-size: 12px;
font-weight: 600;
border: 1px solid rgba(255, 0, 0, 0.2);
}

/* 프로필 이미지 호버 효과 (선택사항) */
.profile-image:hover {
transform: scale(1.05);
transition: transform 0.2s ease;
}

.rotate {
transform: rotate(180deg);
}