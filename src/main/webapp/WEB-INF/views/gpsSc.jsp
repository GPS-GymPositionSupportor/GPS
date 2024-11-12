	<script>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		
		document.addEventListener('DOMContentLoaded', function() {
			var loginForm = document.getElementById('loginForm');
		    loginForm.addEventListener('submit', function(event) {
		        if (!validForm()) {
		            event.preventDefault(); // í¼ ì ì¶ ë°©ì§
		        }
		    });
		    
			var loginForm = document.getElementById('loginForm');
		    var username = loginForm.querySelector('input[name="username"]');
		    var password = loginForm.querySelector('input[name="password"]');
		
		    loginForm.addEventListener('submit', function(event) {
		        if (!validForm()) {
		            event.preventDefault(); // í¼ ì ì¶ ë°©ì§
		        }
		    });
		
		    function validForm() {
		        var isValid = true;
		
		        // ì í¨ì± ê²ì¬
		        if (username.value.trim() === "") {
		            username.classList.add('error'); // ìë¬ í´ëì¤ ì¶ê°
		            isValid = false;
		        } else {
		            username.classList.remove('error'); // ìë¬ í´ëì¤ ì ê±°
		        }
		
		        if (password.value.trim() === "") {
		            password.classList.add('error'); // ìë¬ í´ëì¤ ì¶ê°
		            isValid = false;
		        } else {
		            password.classList.remove('error'); // ìë¬ í´ëì¤ ì ê±°
		        }
		
		        return isValid; // ì í¨ì± ê²ì¬ ê²°ê³¼ ë°í
		    }
		
		    // í´ë¦­ ì´ë²¤í¸ ì¶ê°
		    username.addEventListener('focus', function() {
		        username.classList.remove('error'); // ìë¬ í´ëì¤ ì ê±°
		    });
		
		    password.addEventListener('focus', function() {
		        password.classList.remove('error'); // ìë¬ í´ëì¤ ì ê±°
		    });
		    
		    var togglePassword = document.getElementById('togglePassword');
		    var passwordInput = document.getElementById('password');
		    var eyeIcon = document.getElementById('eyeIcon');

		    togglePassword.addEventListener('click', function() {
		        var type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
		        passwordInput.setAttribute('type', type);
		        
		        eyeIcon.src = type === 'password' ? '../image/closed_eyes.svg' : '../image/open_eyes.svg'; // ë¹ë°ë²í¸ê° ë³´ì¼ ë ìì´ì½ ë³ê²½
		    });
		    
		    <%-- íë²ê±° ë²í¼--%>
		    var burButton = document.querySelector('.burbutton');
		    var navLinks = document.getElementById('nav-links');
	
		    burButton.addEventListener('click', (event) => {
		        event.currentTarget.classList.toggle('active');
		        navLinks.classList.toggle('active');
		    });
	
		    function adjustNavLinks() {
	            var navLinks = document.getElementById('nav-links');

	            if (window.innerWidth <= 767) {
	                // ëª¨ë°ì¼ êµ¬ì¡°ë¡ ë³ê²½
	                navLinks.innerHTML = `
	                	<div class="myPage">
		                    <div class="user-info">
		                        <img
            src="<%= session.getAttribute("profile_img") != null ? session.getAttribute("profile_img") : "../image/myPage_image.png" %>"
            alt="myPage_image" title="myPage_image" class="picture">
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
		                    <button type="submit" name="selectedNav" value="B">íë¡í</button>
		                    <button type="submit" name="selectedNav" value="C">ì¶ì² í¼ë</button>
		                    <button type="submit" name="selectedNav" value="D">ì¤í¬ë©í ì¥ì</button>
		                    <button type="submit" name="selectedNav" value="E">ë´ê° ì´ ë¦¬ë·°</button>
		                </form>
		                <div id="logoutContainer">
		                    <form action="/api/logout" method="post" id="logoutForm">
		                        <button type="submit" id="logoutButton">ë¡ê·¸ìì <img src="../image/Frame.png" alt="logout_icon" class="logout_icon"></button>
		                    </form>
	                    <div>
	                `;
	            } else {
	                // ë°ì¤í¬í± êµ¬ì¡°ë¡ ë³ê²½
	                navLinks.innerHTML = `
	                    <form id="navForm" method="get">
	                        <button type="submit" name="selectedNav" value="A">A</button>
	                        <button type="submit" name="selectedNav" value="B">íë¡í</button>
	                        <button type="submit" name="selectedNav" value="C">ì¶ì² í¼ë</button>
	                        <button type="submit" name="selectedNav" value="D">ì¤í¬ë©í ì¥ì</button>
	                        <button type="submit" name="selectedNav" value="E">ë´ê° ì´ ë¦¬ë·°</button>
	                    </form>
	                    <form action="/api/logout" method="post">
	                        <button type="submit" id="logoutButton">ë¡ê·¸ìì</button>
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
		            document.querySelector('.login-form').innerHTML = data; // ì¤ë²ë ì´ ë´ì©ì ë³ê²½
		        })
		        .catch(error => console.error('Error loading findIdPw.jsp:', error));
		});

		//ê° ë²í¼ì í´ë¦­íì ë ê°ê°ì í¼ì ë³´ì¬ì£¼ë í¨ì
		function showForm(formType) {
			var idPwBtn = document.getElementById('idPw-btn');
		    var findIdForm = document.getElementById('findIdForm');
		    var findPwForm = document.getElementById('findPwForm');
		    var cancelBtn = document.getElementById('cancelBtn');

		    idPwBtn.style.display = 'none';
		    findIdForm.style.display = 'none';
		    findPwForm.style.display = 'none';
		    cancelBtn.style.display = 'none';
		
		    // ì íë í¼ë§ ë³´ì´ê¸°
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

        // 소셜 로그인 버튼 클릭 핸들러
        function kakaoLogin() {
            window.location.href = '/oauth2/authorization/kakao';
        }

        function googleLogin() {
            window.location.href = '/oauth2/authorization/google';
        }

        // 페이지 로드시 초기화 및 소셜 로그인 콜백 처리
        document.addEventListener('DOMContentLoaded', function() {
            const currentPath = window.location.pathname;
            const urlParams = new URLSearchParams(window.location.search);
            const code = urlParams.get('code');

            // 카카오 로그인 콜백 처리
            if (currentPath === '/auth/kakao' && code) {
                handleKakaoCallback(code);
            }
            // 구글 로그인 콜백 처리
            else if (currentPath === '/auth/google' && code) {
                handleGoogleCallback(code);
            }
            // 회원가입 페이지에서 소셜 로그인 정보 처리
            else if (currentPath === '/register') {
                handleSocialRegistration();
            }
        });

        // 카카오 로그인 콜백 처리
        function handleKakaoCallback(code) {
            fetch(`/auth/kakao?code=${code}`)
                .then(response => response.json())
                .then(data => {
                    console.log('Kakao login response:', data);

                    if (data.status === 'success') {
                        window.location.href = data.redirectUrl;
                    } else if (data.status === 'registration_required') {
                        const params = new URLSearchParams({
                            kakaoId: data.kakaoId,
                            nickname: data.nickname,
                            email: data.email,
                            profileImage: data.profileImage,
                            provider: 'KAKAO'
                        });
                        window.location.href = `/register?${params.toString()}`;
                    }
                })
                .catch(error => {
                    console.error('Kakao Login Error:', error);
                    alert('카카오 로그인 처리 중 오류가 발생했습니다.');
                });
        }

        // 구글 로그인 콜백 처리
        function handleGoogleCallback(code) {
            fetch(`/auth/google?code=${code}`)
                .then(response => response.json())
                .then(data => {
                    console.log('Google login response:', data);

                    if (data.status === 'success') {
                        window.location.href = data.redirectUrl;
                    } else if (data.status === 'registration_required') {
                        const params = new URLSearchParams({
                            googleId: data.googleId,
                            name: data.name,
                            email: data.email,
                            profileImage: data.profileImage,
                            provider: 'GOOGLE'
                        });
                        window.location.href = `/register?${params.toString()}`;
                    }
                })
                .catch(error => {
                    console.error('Google Login Error:', error);
                    alert('구글 로그인 처리 중 오류가 발생했습니다.');
                });
        }

        // 회원가입 페이지에서 소셜 로그인 정보 처리
        function handleSocialRegistration() {
            const urlParams = new URLSearchParams(window.location.search);
            const provider = urlParams.get('provider');

            if (provider === 'KAKAO') {
                setupKakaoRegistration(urlParams);
            } else if (provider === 'GOOGLE') {
                setupGoogleRegistration(urlParams);
            }
        }

        // 카카오 회원가입 폼 설정
        function setupKakaoRegistration(urlParams) {
            // ID/PW 필드 숨기기
            hideLoginFields();

            // 폼 자동 완성
            autoFillKakaoForm(urlParams);

            // Hidden 필드 추가
            addKakaoHiddenFields(urlParams);

            // 안내 메시지 추가
            addSocialLoginInfo('카카오');

            // 스타일 적용
            applySocialLoginStyles();
        }

        // 구글 회원가입 폼 설정
        function setupGoogleRegistration(urlParams) {
            // ID/PW 필드 숨기기
            hideLoginFields();

            // 폼 자동 완성
            autoFillGoogleForm(urlParams);

            // Hidden 필드 추가
            addGoogleHiddenFields(urlParams);

            // 안내 메시지 추가
            addSocialLoginInfo('구글');

            // 스타일 적용
            applySocialLoginStyles();
        }

        // 유틸리티 함수들
        function hideLoginFields() {
            document.querySelector('.form-group:has(#username)').style.display = 'none';
            document.querySelector('.form-group:has(#password)').style.display = 'none';
            document.querySelector('.form-group:has(#confirm-password)').style.display = 'none';
        }

        function autoFillKakaoForm(urlParams) {
            document.getElementById('nickname').value = urlParams.get('nickname') || '';
            document.getElementById('email').value = urlParams.get('email') || '';

            // 자동 입력된 필드 읽기 전용 설정
            document.getElementById('email').readOnly = true;
            document.getElementById('nickname').readOnly = true;
        }

        function autoFillGoogleForm(urlParams) {
            document.getElementById('name').value = urlParams.get('name') || '';
            document.getElementById('email').value = urlParams.get('email') || '';

            // 자동 입력된 필드 읽기 전용 설정
            document.getElementById('email').readOnly = true;
            document.getElementById('name').readOnly = true;
        }

        function addKakaoHiddenFields(urlParams) {
            const form = document.querySelector('form');
            const hiddenFields = `
        <input type="hidden" name="providerType" value="KAKAO">
        <input type="hidden" name="providerId" value="${urlParams.get('kakaoId')}">
        <input type="hidden" name="profileImg" value="${urlParams.get('profileImage')}">
    `;
            form.insertAdjacentHTML('beforeend', hiddenFields);
        }

        function addGoogleHiddenFields(urlParams) {
            const form = document.querySelector('form');
            const hiddenFields = `
        <input type="hidden" name="providerType" value="GOOGLE">
        <input type="hidden" name="providerId" value="${urlParams.get('googleId')}">
        <input type="hidden" name="profileImg" value="${urlParams.get('profileImage')}">
    `;
            form.insertAdjacentHTML('beforeend', hiddenFields);
        }

        function addSocialLoginInfo(provider) {
            const form = document.querySelector('form');
            const infoText = document.createElement('div');
            infoText.className = 'social-login-info';
            infoText.innerHTML = `${provider} 계정으로 회원가입을 진행합니다.<br>추가 정보를 입력해주세요.`;
            form.insertBefore(infoText, form.firstChild);
        }

        function applySocialLoginStyles() {
            const style = document.createElement('style');
            style.textContent = `
        .social-login-info {
            background-color: ${provider == 'KAKAO' ? '#FEE500' : '#4285F4'};
            color: ${provider == 'KAKAO' ? '#000000' : '#FFFFFF'};
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            text-align: center;
            font-weight: bold;
        }

        .form-group input:disabled {
            background-color: #f8f9fa;
        }
    `;
            document.head.appendChild(style);
        }

        // 회원가입 폼 제출 처리
        document.querySelector('form')?.addEventListener('submit', function(e) {
            e.preventDefault();

            const formData = new FormData(this);
            const data = Object.fromEntries(formData.entries());

            fetch('/api/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data)
            })
                .then(response => response.json())
                .then(result => {
                    if (result.status === 'success') {
                        alert('회원가입이 완료되었습니다.');
                        window.location.href = '/';
                    } else {
                        alert(result.message || '회원가입 중 오류가 발생했습니다.');
                    }
                })
                .catch(error => {
                    console.error('Registration Error:', error);
                    alert('회원가입 처리 중 오류가 발생했습니다.');
                });
        });




    </script>