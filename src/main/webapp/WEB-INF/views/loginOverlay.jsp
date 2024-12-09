<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="gpsSc.jsp" %>
<!-- 로그인 오버레이 -->

    <div class="login-overlay" id="loginOverlay" style="display: <%= (session.getAttribute("userID") == null) ? "flex" : "none" %>;">
	    <div class="login-form">
	    	<div class="images">
	    		<img src="../image/logo.png" alt="logo1" title="logo" class="picture">
	    		<img src="../image/LookForYourMovement.png" alt="moto" title="moto" class="picture">
	    	</div>
	    	
	    	<%
	    	String loginError = (String) session.getAttribute("loginError");
		    %>
		
		    <!-- 고정된 오류 메시지 공간 -->
		    <div id="error-message">
		        <%
		        if (loginError != null) {
		            out.print(loginError.replace("<br>", "<br/>")); // HTML 태그 출력
		            session.removeAttribute("loginError"); // 메시지 출력 후 세션에서 제거
		        }
		        %>
		    </div>
		    
	        <form action="/api/login" method="post" id="loginForm">
	            <input type="text" name="username" placeholder="아이디를 입력해주세요">
	            <div class="password-container">
			        <input type="password" name="password" id="password" minlength="8" maxlength="16" placeholder="비밀번호를 입력해주세요">
			        <button type="button" id="togglePassword" class="eye-btn">
			        	<img src="../image/closed_eyes.svg" alt="Toggle Password" id="eyeIcon">
			        </button>
			    </div>
	            <button type="submit">로그인</button> 
	        </form>
	        
	        <div class="find-regist-btn">
		        	<button class="findIdPw-btn" id="findIdPwBtn" onclick="location.href='/email/findIdPassword' ">아이디/비밀번호 찾기</button>
		        	<button class="register-btn" onclick="location.href='/api/register'">회원가입</button>
			</div>
			<p class="sns">sns로 시작하기</p>
			<hr class="divideLine">
	        <div class="social-buttons">
		        <button class="google-btn" onclick="googleLogin()">
		        	<img src="../image/Google Login.png" alt="googleLogin">
		        </button>
		        <button class="kakao-btn" onclick="kakaoLogin()">
		        	<img src="../image/Kakao Login.png" alt="kakaoLogin">
		        </button>
	        </div>
	    </div>
	</div>

	<script>
		/*
           SSO 관련
           */


		// SSO 관련 유틸리티 함수
		const SSOUtil = {
			setToken: function (token) {
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



