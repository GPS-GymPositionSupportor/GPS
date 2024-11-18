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
overflow-x: hidden;
}


/* 사이드바 스타일 */
.sidebar {
width: 250px;
height: 100vh;
position: sticky;
background-color: #2C2C2C;
color: white;
padding: 20px;
top: 0;
}

.logo {
padding: 0 20px 20px;
border-bottom: 1px solid #444;
}

.logo img {
max-width: 60%;
height: auto;
}

.nav-menu .nav-item {
position: relative;
}

.nav-item > a {
display: flex;
align-items: center;
justify-content: space-evenly;
padding: 12px 20px;
color: #fff;
text-decoration: none;
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
background-color: #3D3D3D;
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
background-color: #3D3D3D;
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
position: relative;
flex: 1;
padding: 20px;
margin-left: 0;
background-color: #f4f4f4;
min-height: 100vh;
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

.header .buttons {
display: flex;
gap: 10px;
padding-right: 20px;  /* 오른쪽 여백 추가 */
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
background-color: #B05D5D;
color: white;
}

.btn-secondary {
background-color: #6c757d;
color: white;
}

/* 콘텐츠 그리드 */
.content-grid {
display: flex;
flex-direction: column;
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

ul {
list-style: none;
}

ul::marker {
display: none; /* ::marker 가상 요소를 표시하지 않음 */
}

/* 로그아웃 버튼 */
.logout-btn {
position: absolute;
bottom: 20px;
left: 20px;
right: 20px;
padding: 12px;
background-color: transparent;
border: none;
color: white;
display: flex;
align-items: center;
cursor: pointer;
border-radius: 6px;
transition: background-color 0.3s;
justify-content: center;
}

.logout-btn:hover {
background-color: #3D3D3D;
}

.logout-btn i {
margin-right: 10px;
}




/* 차트 컨테이너 스타일 */
.chart-container {
background: white;
border-radius: 8px;
padding: 20px;
margin-bottom: 20px;
box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.chart-header {
display: flex;
justify-content: space-between;
align-items: center;
margin-bottom: 15px;
}

.chart-title {
font-size: 18px;
font-weight: 600;
color: #333;
}

.chart-period {
color: #666;
font-size: 14px;
}





/* 리뷰 관리를 위해 추가된 새로운 스타일 */
/* 리뷰 그리드 레이아웃 */
.review-grid {
display: flex;
gap: 20px;
margin-bottom: 20px;
}

/* 리뷰 관리 스타일 */
.review-header {
display: flex;
justify-content: space-between;
align-items: center;
padding: 20px;
background: #fff;
margin-bottom: 20px;
}

/* 리뷰 정보 (작성자, 날짜) */
.review-info {
display: flex;
flex-direction: column;
align-items: flex-end;
min-width: 120px;
}

.review-column {
flex: 1;
display: flex;
flex-direction: column;
gap: 15px;
}

/* 리뷰 리스트 컨테이너 */
.review-list {
display: flex;
justify-content: space-between;
gap: 30px;
padding: 20px;
background: #f8f9fa;
height: calc(100vh - 200px);
overflow-y: auto;
}

/* 첫 번째 column (왼쪽 12개) */
.review-left {
grid-column: 1;  /* 첫 번째 열 */
}

/* 두 번째 column (오른쪽 12개) */
.review-right {
grid-column: 2;  /* 두 번째 열 */
}

/* 개별 리뷰 아이템 */
.review-item {
display: flex;
align-items: center;
padding: 20px;
background: white;
border-radius: 8px;
box-shadow: 0 2px 4px rgba(0,0,0,0.05);
transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.review-item:hover {
transform: translateY(-2px);
box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

/* 체크박스 */
.review-select {
width: 20px;
height: 20px;
margin-right: 15px;
cursor: pointer;
}

/* 리뷰 이미지 */
.review-img {
width: 70px;
height: 70px;
object-fit: cover;
border-radius: 6px;
margin: 0 20px;
}

.no-image {
width: 60px;
height: 60px;
background: #f0f0f0;
display: flex;
align-items: center;
justify-content: center;
margin: 0 10px;
border-radius: 4px;
color: #999;
font-size: 12px;
}

/* 리뷰 내용 스타일 */
.review-content {
flex: 1;
overflow: hidden;
}

.review-text {
flex: 1;
font-size: 15px;
color: #333;
line-height: 1.5;
margin-right: 20px;
word-break: break-word;
}

.review-writer {
font-weight: 600;
color: #2c3e50;
margin-bottom: 5px;
}

.review-date {
font-size: 13px;
color: #B00C0C;
}


/* pagination 스타일 수정 */
.pagination {
display: flex;
justify-content: center;
gap: 10px;
position: absolute;
bottom: 20px;
left: 50%;
transform: translateX(-50%);
width: fit-content;
padding: 10px 0;
}

.pagination button {
padding: 5px 15px;
border: 1px solid #ddd;
background: #fff;
cursor: pointer;
min-width: 40px;
text-align: center;
}

.pagination button.active {
background: #f0f0f0;
border-bottom: 2px solid #000;
font-weight: bold;
}

.pagination button.arrow {
font-weight: bold;
font-size: 18px;
}

.pagination button.double-arrow {
font-weight: bold;
font-size: 16px;
}

.pagination button:hover {
background: #f5f5f5;
}

.pagination button:hover:not(:disabled) {
background: #f0f0f0;
}

.pagination button:disabled {
background: #f0f0f0;
cursor: not-allowed;
opacity: 0.6;
}

/* 버튼 스타일 */
.btn-primary {
background: #dc3545;
color: white;
border: none;
padding: 8px 16px;
border-radius: 4px;
cursor: pointer;
}

.btn-secondary {
background: #6c757d;
color: white;
border: none;
padding: 8px 16px;
border-radius: 4px;
cursor: pointer;
}

.gym-table {
background: white;
border-radius: 8px;
padding: 20px;
margin-top: 20px;
overflow-x: auto;
}

.gym-table th {
padding: 15px;
font-weight: 600;
background-color: #f8f9fa;
border-bottom: 2px solid #dee2e6;
}

.gym-row {
transition: background-color 0.2s;
}

.gym-row:hover {
background-color: #f8f9fa;
}

.gym-row td {
padding: 12px 15px;
vertical-align: middle;
}

.gym-row img {
width: 50px;
height: 50px;
object-fit: cover;
border-radius: 4px;
}

.gym-table th {
padding: 15px;
font-weight: 600;
background-color: #f8f9fa;
border-bottom: 2px solid #dee2e6;
}

table {
width: 100%;
border-collapse: collapse;
}

.gym-item {
display: flex;
align-items: center;
padding: 20px;
background: white;
border-radius: 8px;
box-shadow: 0 2px 4px rgba(0,0,0,0.05);
transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.gym-item:hover {
transform: translateY(-2px);
box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

/* 체크박스 */
.gym-select {
width: 18px;
height: 18px;
margin: 0;
cursor: pointer;
}

th, td {
padding: 12px 15px;
text-align: left;
border-bottom: 1px solid #eee;
}

th {
background-color: #f8f9fa;
font-weight: 600;
}

tr:hover {
background-color: #f8f9fa;
}

.search-bar {
display: flex;
gap: 10px;
margin: 20px 0;
}

.search-input {
flex: 1;
padding: 8px 12px;
border: 1px solid #ddd;
border-radius: 4px;
}

.search-btn {
padding: 8px 16px;
background: #B05D5D;
color: white;
border: none;
border-radius: 4px;
cursor: pointer;
}


/* 모달 스타일 */
.modal {
display: none;
position: fixed;
top: 0;
left: 0;
width: 100%;
height: 100%;
background-color: rgba(0, 0, 0, 0.5);
z-index: 1000;
}

.modal-content {
position: relative;
background-color: #fff;
width: 800px;
margin: 50px auto;
padding: 20px;
border-radius: 8px;
box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.form-group {
margin-bottom: 20px;
position: relative;
}

.form-group label {
display: flex;
align-items: center;
gap: 10px;
margin-bottom: 8px;
color: #666;
}

.form-group i {
color: #888;
}

.form-control {
width: 100%;
padding: 10px;
border: 1px solid #ddd;
border-radius: 4px;
font-size: 14px;
}

.form-control:focus {
border-color: #B05D5D;
outline: none;
}

.edit-buttons {
display: flex;
justify-content: flex-end;
gap: 10px;
margin-top: 20px;
}

.btn-edit {
padding: 8px 16px;
border: none;
border-radius: 4px;
cursor: pointer;
}

.btn-save {
background-color: #B05D5D;
color: white;
}

.btn-cancel {
background-color: #6c757d;
color: white;
}
