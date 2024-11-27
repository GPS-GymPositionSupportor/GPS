<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<script>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		
		document.addEventListener('DOMContentLoaded', function() {
		    
			var loginForm = document.getElementById('loginForm');
		    var username = loginForm.querySelector('input[name="username"]');
		    var password = loginForm.querySelector('input[name="password"]');
		
		    loginForm.addEventListener('submit', function(event) {
		        if (!validForm()) {
		            event.preventDefault();
		        }
		    });
		
		    // 로그인 유효성 검사
		    function validForm() {
		        var isValid = true;
		
		        if (username.value.trim() === "") {
		            username.classList.add('error');
		            isValid = false;
		        } else {
		            username.classList.remove('error');
		        }
		
		        if (password.value.trim() === "") {
		            password.classList.add('error');
		            isValid = false;
		        } else {
		            password.classList.remove('error');
		        }
		
		        return isValid;
		    }
		
		    // id 유효성 검사 이후 id/pw 입력 폼 클릭 시 error 제거
		    username.addEventListener('focus', function() {
		        username.classList.remove('error');
		    });
		
		    password.addEventListener('focus', function() {
		        password.classList.remove('error');
		    });
		    
		    // pw 입력 폼 눈 아이콘
		    var togglePassword = document.getElementById('togglePassword');
		    var passwordInput = document.getElementById('password');
		    var eyeIcon = document.getElementById('eyeIcon');

		    togglePassword.addEventListener('click', function() {
		        var type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
		        passwordInput.setAttribute('type', type);
		        
		        eyeIcon.src = type === 'password' ? 'image/closed_eyes.svg' : 'image/open_eyes.svg';
		    });
		    
		    <%-- 햄버거 버튼 --%>
		    var burButton = document.querySelector('.burbutton');
            var navLinks = document.getElementById('nav-links');
            if(burButton && navLinks) { // 두 요소가 모두 존재할 때만 실행
                burButton.addEventListener('click', (event) => {
                    event.currentTarget.classList.toggle('active');
                    navLinks.classList.toggle('active');
                });
            }
	
		    <%-- 사용자 화면당 화면 구조 변경 --%>
		    function adjustNavLinks() {
	            var navLinks = document.getElementById('nav-links');

	            if (window.innerWidth <= 767) {
	                // 모바일 구조로 변경
	            	navLinks.innerHTML = `
	                	<div class="myPage">
		                    <div class="user-info">
		                        <img
            src="<%= session.getAttribute("profile_img") != null ? session.getAttribute("profile_img") : "image/myPage_image.svg" %>"
            alt="myPage_image" title="myPage_image" class="picture">
		                        <div class="greeting">
		                            <a class="hello">안녕하세요</a>
		                            <div class="mrUser">
		                                <a class="mrUserName"><%= session.getAttribute("nickname") %></a>
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
		                	<form action="/auth/logout" method="post" id="logoutForm">	// action 나중에 다시 변경
		                        <button type="submit" id="logoutButton">로그아웃 <img src="image/logout.png" alt="logout_icon" class="logout_icon"></button>
		                    </form>
	                    </div>
	                `;
	            } else {
	                // 데스크톱 구조로 변경
	            	navLinks.innerHTML = `
	                	<div class="myPage">
		                    <div class="user-info">
		                        <img
            src="<%= session.getAttribute("profile_img") != null ? session.getAttribute("profile_img") : "image/myPage_image.svg" %>"
            alt="myPage_image" title="myPage_image" class="picture">
		                        <div class="greeting">
		                            <a class="hello">안녕하세요</a>
		                            <div class="mrUser">
		                                <a class="mrUserName"><%= session.getAttribute("nickname") %></a>
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
		                	<form action="/auth/logout" method="post">
		                        <button type="submit" id="logoutButton">로그아웃 <img src="image/logout.png" alt="logout_icon" class="logout_icon"></button>
		                    </form>
	                    </div>
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
		        	// 가져온 데이터를 login-form 요소에 삽입
		            document.querySelector('.login-form').innerHTML = data;
		            
		            // id 찾기 폼 가져옴
		            var findIdForm = document.getElementById('findIdForm2');
		    	    if (findIdForm) {
		    	        findIdForm.addEventListener('submit', function(event) {
		    	            if (!validFindIdForm()) {
		    	                event.preventDefault(); // 유효성 검사 실패 시 폼 제출 방지
		    	                console.log("ID validation failed, form submission prevented.");
		    	            }
		    	        });
		    	    }
		    	
		    	    // pw 찾기 폼 가져옴
		    	    var findPwForm = document.getElementById('findPwForm2');
		    	    if (findPwForm) {
		    	        findPwForm.addEventListener('submit', function(event) {
		    	            if (!validFindPwForm()) {
		    	                event.preventDefault(); // 유효성 검사 실패 시 폼 제출 방지
		    	                console.log("Password validation failed, form submission prevented.");
		    	            }
		    	        });
		    	    }     
				})
		        .catch(error => console.error('Error loading findIdPw.jsp:', error));
		});

		// id찾기 유효성 검사
		function validFindIdForm() {
		    var isValid = true;
		
		    var nameField = document.getElementById('findIdF');
		    var emailId = document.getElementById('findIdEmailId');
		    var emailDomain = document.getElementById('findIdEmailDomain');
			var plzEmailId = document.getElementById('plzEmailId');
			
		    if (nameField.value.trim() === "") {
		        nameField.classList.add('error');
		        isValid = false;
		    } else {
		        nameField.classList.remove('error');
		    }
		
		    if (emailId.value.trim() === "" || emailDomain.value.trim() === "") {
		    	plzEmailId.classList.add('error');
		    	//emailId.classList.add('error');
		        //emailDomain.classList.add('error');
		        isValid = false;
		    } else {
		    	plzEmailId.classList.remove('error');
		        //emailId.classList.remove('error');
		        //emailDomain.classList.remove('error');
		    }
		
		    return isValid;
		}
		
		// pw 찾기 유효성 검사
		function validFindPwForm() {
		    var isValid = true;
		
		    var idField = document.getElementById('findPwF');
		    var emailId = document.getElementById('findPwEmailId');
		    var emailDomain = document.getElementById('findPwEmailDomain');
		    var plzEmailPw = document.getElementById('plzEmailPw');
		    
		    if (idField.value.trim() === "") {
		        idField.classList.add('error');
		        isValid = false;
		    } else {
		        idField.classList.remove('error');
		    }
		
		    if (emailId.value.trim() === "" || emailDomain.value.trim() === "") {
		    	plzEmailPw.classList.add('error');
		        //emailId.classList.add('error');
		        //emailDomain.classList.add('error');
		        isValid = false;
		    } else {
		    	plzEmailPw.classList.remove('error');
		        //emailId.classList.remove('error');
		        //emailDomain.classList.remove('error');
		    }
		
		    return isValid;
		}
		
		// id/pw 찾기 페이지에서 각 버튼을 클릭했을 때 각각의 폼을 보여주는 함수
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
		
		// findIdComplete.jsp의 비밀번호 찾기 버튼 클릭 시 비밀번호 찾기 폼을 띄워주는 ajax 함수
		document.addEventListener('DOMContentLoaded', function() {
		    var urlParams = new URLSearchParams(window.location.search);
		    var show = urlParams.get('show');

		    if (show === 'findPw') {
		        // 로그인 오버레이를 보여줍니다.
		        var loginOverlay = document.getElementById('loginOverlay');
		        loginOverlay.style.display = 'flex';

		        // findIdPw.jsp를 AJAX로 요청하여 오버레이에 내용을 로드합니다.
		        fetch('findIdPw.jsp')
		            .then(response => response.text())
		            .then(data => {
		                document.querySelector('.login-form').innerHTML = data;

		                showForm('findPw');
		                
		                window.history.replaceState({}, document.title, window.location.pathname);
		            })
		            .catch(error => console.error('Error loading findIdPw.jsp:', error));
		    }
		});
		
		//findIdCertification.jsp에서 뒤로가기 버튼 클릭 시 아이디 찾기 폼을 띄워주는 ajax 함수
		document.addEventListener('DOMContentLoaded', function() {
		    var urlParams = new URLSearchParams(window.location.search);
		    var idCertification = urlParams.get('idCertification');

		    if (idCertification === 'findId') {
		        var loginOverlay = document.getElementById('loginOverlay');
		        loginOverlay.style.display = 'flex';

		        fetch('findIdPw.jsp')
		            .then(response => response.text())
		            .then(data => {
		                document.querySelector('.login-form').innerHTML = data;

		                showForm('findId');
		                
		                window.history.replaceState({}, document.title, window.location.pathname);
		            })
		            .catch(error => console.error('Error loading findIdPw.jsp:', error));
		    }
		});
		
		//findPwCertification.jsp에서 뒤로가기 버튼 클릭 시 비밀번호 찾기 폼을 띄워주는 ajax 함수
		document.addEventListener('DOMContentLoaded', function() {
		    var urlParams = new URLSearchParams(window.location.search);
		    var pwCertification = urlParams.get('pwCertification');

		    if (pwCertification === 'findPw') {
		        var loginOverlay = document.getElementById('loginOverlay');
		        loginOverlay.style.display = 'flex';

		        fetch('findIdPw.jsp')
		            .then(response => response.text())
		            .then(data => {
		                document.querySelector('.login-form').innerHTML = data;

		                showForm('findPw');
		                
		                window.history.replaceState({}, document.title, window.location.pathname);
		            })
		            .catch(error => console.error('Error loading findIdPw.jsp:', error));
		    }
		});
		/*
        SSO 관련
         */


        // SSO 관련 유틸리티 함수
        const SSOUtil = {
            setToken(token) {
                if (token) {
                    localStorage.setItem('ssoToken', token);
                    console.log('Token saved:', token);
                }
            },

            getToken() {
                const token = localStorage.getItem('ssoToken');
                return token;
            },

            removeToken() {
                localStorage.removeItem('ssoToken');
            },

            async checkSSOStatus() {
                try {
                    console.log('Checking SSO status...');
                    const response = await fetch('/auth/validate-token', {
                        credentials: 'include'  // 세션 쿠키 포함
                    });
                    const data = await response.json();

                    // 서버에서 토큰을 받으면 localStorage에 저장
                    if (data.authenticated && data.ssoToken) {
                        this.setToken(data.ssoToken);
                    }

                    return data.authenticated;
                } catch (error) {
                    console.error('SSO check failed:', error);
                    return false;
                }
            }
        };

    // 페이지 로드 시 SSO 상태 체크
    document.addEventListener('DOMContentLoaded', async function() {
        console.log('Checking SSO status on page load');
        const isAuthenticated = await SSOUtil.checkSSOStatus();
        console.log('Authentication status:', isAuthenticated);
    });

        
		// 소셜 로그인 버튼 클릭 핸들러
        async function kakaoLogin() {
            const isAuthenticated = await SSOUtil.checkSSOStatus();
            if (isAuthenticated) {
                window.location.href = '/';
                return;
            }
            window.location.href = '/oauth2/authorization/kakao';
        }

        async function handleKakaoCallback(code) {
            console.log('Handling Kakao callback with code:', code);

            try {
                const response = await fetch(`/auth/kakao?code=` + code);
                const data = await response.json();
                console.log('Kakao login response:', data);

                if (data.status === 'success') {
                    if (data.ssoToken) {
                        SSOUtil.setToken(data.ssoToken);
                    }
                    window.location.href = data.redirectUrl;
                } else if (data.status === 'registration_required') {
                    const params = new URLSearchParams();
                    params.append('kakaoId', data.kakaoId);
                    params.append('nickname', data.nickname);
                    params.append('email', data.email);
                    params.append('profileImage', data.profileImage);
                    params.append('provider', 'KAKAO');
                    window.location.href = '/register?' + params.toString();
                }
            } catch (error) {
                console.error('Kakao Login Error:', error);
                alert('카카오 로그인 처리 중 오류가 발생했습니다.');
            }
        }


        async function googleLogin() {
            console.log('Initiating Google login');

            // SSO 상태 확인
            const isAuthenticated = await SSOUtil.checkSSOStatus();
            if (isAuthenticated) {
                window.location.href = '/';
                return;
            }

            try {
                const response = await fetch('/auth/google/url');
                const url = await response.text();
                console.log('Redirecting to:', url);
                window.location.href = url;
            } catch (error) {
                console.error('Error getting Google auth URL:', error);
                alert('구글 로그인 초기화 중 오류가 발생했습니다.');
            }
        }


        // 페이지 로드시 초기화 및 소셜 로그인 콜백 처리
        document.addEventListener('DOMContentLoaded', async function() {
            console.log("DOM Content Loaded");

            const currentPath = window.location.pathname;
            const urlParams = new URLSearchParams(window.location.search);
            const code = urlParams.get('code');

            console.log('Current Path:', currentPath);
            console.log('Code:', code);

            // SSO 상태 확인
            const isAuthenticated = await SSOUtil.checkSSOStatus();
            if (isAuthenticated && currentPath === '/login') {
                window.location.href = '/';
                return;
            }

            // 카카오 로그인 콜백 처리
            if (currentPath === '/auth/kakao' && code) {
                console.log('Processing Kakao callback');
                handleKakaoCallback(code);
            }
            // 구글 로그인 콜백 처리
            else if (currentPath === '/auth/google' && code) {
                console.log('Processing Google callback');
                handleGoogleCallback(code);
            }
            // 회원가입 페이지에서 소셜 로그인 정보 처리
            else if (currentPath === '/register') {
                console.log('Processing registration page');
                handleSocialRegistration();
            }
        });



        // 구글 로그인 콜백 처리
        async function handleGoogleCallback(code) {
            console.log('Handling Google callback with code:', code);

            try {
                const response = await fetch(`/auth/google?code=` + code);
                const data = await response.json();
                console.log('Google login response:', data);

                if (data.status === 'success') {
                    // SSO 토큰 저장
                    if (data.ssoToken) {
                        SSOUtil.setToken(data.ssoToken);
                    }
                    window.location.href = data.redirectUrl;
                } else if (data.status === 'registration_required') {
                    const params = new URLSearchParams();
                    params.append('googleId', data.providerId);
                    params.append('name', data.name);
                    params.append('email', data.email);
                    params.append('profileImage', data.profileImage);
                    params.append('provider', 'GOOGLE');
                    window.location.href = '/api/register?' + params.toString();
                }
            } catch (error) {
                console.error('Google Login Error:', error);
                alert('구글 로그인 처리 중 오류가 발생했습니다.');
            }
        }

        // 로그아웃 처리 (필요한 경우 추가)
        async function logout() {
            try {
                await fetch('/auth/logout', {
                    method: 'POST' ,
                    headers: {
                        'Authorization': 'Bearer ' + SSOUtil.getToken()
                    }
                });

                SSOUtil.removeToken();
                window.location.href = '/login';
            } catch (error) {
                console.error('Logout Error:', error);
                alert('로그아웃 처리 중 오류가 발생했습니다.');
            }
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