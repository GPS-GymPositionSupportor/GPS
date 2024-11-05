<script>
	 
		document.addEventListener('DOMContentLoaded', function() {
			var loginForm = document.getElementById('loginForm');
		    loginForm.addEventListener('submit', function(event) {
		        if (!validForm()) {
		            event.preventDefault(); // 폼 제출 방지
		        }
		    });
		    
			var loginForm = document.getElementById('loginForm');
		    var username = loginForm.querySelector('input[name="username"]');
		    var password = loginForm.querySelector('input[name="password"]');
		
		    loginForm.addEventListener('submit', function(event) {
		        if (!validForm()) {
		            event.preventDefault(); // 폼 제출 방지
		        }
		    });
		
		    function validForm() {
		        var isValid = true;
		
		        // 유효성 검사
		        if (username.value.trim() === "") {
		            username.classList.add('error'); // 에러 클래스 추가
		            isValid = false;
		        } else {
		            username.classList.remove('error'); // 에러 클래스 제거
		        }
		
		        if (password.value.trim() === "") {
		            password.classList.add('error'); // 에러 클래스 추가
		            isValid = false;
		        } else {
		            password.classList.remove('error'); // 에러 클래스 제거
		        }
		
		        return isValid; // 유효성 검사 결과 반환
		    }
		
		    // 클릭 이벤트 추가
		    username.addEventListener('focus', function() {
		        username.classList.remove('error'); // 에러 클래스 제거
		    });
		
		    password.addEventListener('focus', function() {
		        password.classList.remove('error'); // 에러 클래스 제거
		    });
		    
		    var togglePassword = document.getElementById('togglePassword');
		    var passwordInput = document.getElementById('password');
		    var eyeIcon = document.getElementById('eyeIcon');

		    togglePassword.addEventListener('click', function() {
		        var type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
		        passwordInput.setAttribute('type', type);
		        
		        eyeIcon.src = type === 'password' ? 'image/closed_eyes.svg' : 'image/open_eyes.svg'; // 비밀번호가 보일 때 아이콘 변경
		    });
		    
		    <%-- 햄버거 버튼--%>
		    var burButton = document.querySelector('.burbutton');
		    var navLinks = document.getElementById('nav-links');
	
		    burButton.addEventListener('click', (event) => {
		        event.currentTarget.classList.toggle('active');
		        navLinks.classList.toggle('active');
		    });
	
		    function adjustNavLinks() {
	            var navLinks = document.getElementById('nav-links');

	            if (window.innerWidth <= 767) {
	                // 모바일 구조로 변경
	                navLinks.innerHTML = `
	                	<div class="myPage">
		                    <div class="user-info">
		                        <img src="image/myPage_image.png" alt="myPage_image" title="myPage_image" class="picture">
		                        <div class="greeting">
		                            <a class="hello">안녕하세요</a>
		                            <div class="mrUser">
		                                <a class="mrUserName"><%= session.getAttribute("userID") %></a>
		                                <a class="mr">님</a>
		                            </div>
		                        </div>
		                    </div>
		                </div>
	                <div>
	                	<form id="navForm" method="get">
		                    <button type="submit" name="selectedNav" value="A">A</button>
		                    <button type="submit" name="selectedNav" value="B">프로필</button>
		                    <button type="submit" name="selectedNav" value="C">추천 피드</button>
		                    <button type="submit" name="selectedNav" value="D">스크랩한 장소</button>
		                    <button type="submit" name="selectedNav" value="E">내가 쓴 리뷰</button>
		                </form>
		                <div id="logoutContainer">
		                    <form action="logout.jsp" method="post" id="logoutForm">
		                        <button type="submit" id="logoutButton">로그아웃 <img src="image/Frame.png" alt="logout_icon" class="logout_icon"></button>
		                    </form>
	                    <div>
	                `;
	            } else {
	                // 데스크톱 구조로 변경
	                navLinks.innerHTML = `
	                    <form id="navForm" method="get">
	                        <button type="submit" name="selectedNav" value="A">A</button>
	                        <button type="submit" name="selectedNav" value="B">프로필</button>
	                        <button type="submit" name="selectedNav" value="C">추천 피드</button>
	                        <button type="submit" name="selectedNav" value="D">스크랩한 장소</button>
	                        <button type="submit" name="selectedNav" value="E">내가 쓴 리뷰</button>
	                    </form>
	                    <form action="logout.jsp" method="post">
	                        <button type="submit" id="logoutButton">로그아웃</button>
	                    </form>
	                `;
	            }
	        }

	        adjustNavLinks();

	        window.addEventListener('resize', adjustNavLinks);
		});
    
		document.getElementById('findIdPwBtn').addEventListener('click', function() {
		    fetch('findIdPw.jsp')
		        .then(response => response.text())
		        .then(data => {
		            document.querySelector('.login-form').innerHTML = data; // 오버레이 내용을 변경
		        })
		        .catch(error => console.error('Error loading findIdPw.jsp:', error));
		});
		
		//각 버튼을 클릭했을 때 각각의 폼을 보여주는 함수
		function showForm(formType) {
			var idPwBtn = document.getElementById('idPw-btn');
		    var findIdForm = document.getElementById('findIdForm');
		    var findPwForm = document.getElementById('findPwForm');
		    var cancelBtn = document.getElementById('cancelBtn');

		    idPwBtn.style.display = 'none';
		    findIdForm.style.display = 'none';
		    findPwForm.style.display = 'none';
		    cancelBtn.style.display = 'none';
		
		    // 선택된 폼만 보이기
		    if (formType === 'findBtn') {
		    	idPwBtn.style.display = 'block';
		    } else if (formType === 'findId') {
		        findIdForm.style.display = 'block';
		        cancelBtn.style.display = 'block';
		    } else if (formType === 'findPw') {
		        findPwForm.style.display = 'block';
		        cancelBtn.style.display = 'block';
		    }
		}
		
    </script>