	<script>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		
		document.addEventListener('DOMContentLoaded', function() {
			var loginForm = document.getElementById('loginForm');
		    loginForm.addEventListener('submit', function(event) {
		        if (!validForm()) {
		            event.preventDefault();
		        }
		    });
		    
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
		        
		        eyeIcon.src = type === 'password' ? 'image/closed_eyes.svg' : 'image/open_eyes.svg'; // Ã«Â¹ÂÃ«Â°ÂÃ«Â²ÂÃ­ÂÂ¸ÃªÂ°Â Ã«Â³Â´Ã¬ÂÂ¼ Ã«ÂÂ Ã¬ÂÂÃ¬ÂÂ´Ã¬Â½Â Ã«Â³ÂÃªÂ²Â½
		    });
		    
		    <%-- 햄버거 버튼 --%>
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
		                    <button type="submit" name="selectedNav" value="B">프로필</button>
		                    <button type="submit" name="selectedNav" value="C">추천 피드</button>
		                    <button type="submit" name="selectedNav" value="D">스크랩한 장소</button>
		                    <button type="submit" name="selectedNav" value="E">내가 쓴 리뷰</button>
		                </form>
		                <div id="logoutContainer">
		                    <form action="/api/logout" method="post" id="logoutForm">
		                        <button type="submit" id="logoutButton">로그아웃 <img src="../image/Frame.png" alt="logout_icon" class="logout_icon"></button>
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
	                    <form action="/api/logout" method="post">
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
		            document.querySelector('.login-form').innerHTML = data; // Ã¬ÂÂ¤Ã«Â²ÂÃ«Â ÂÃ¬ÂÂ´ Ã«ÂÂ´Ã¬ÂÂ©Ã¬ÂÂ Ã«Â³ÂÃªÂ²Â½
		            
		            // id ì°¾ê¸° í¼
		            var findIdForm = document.getElementById('findIdForm2');
		    	    if (findIdForm) {
		    	        findIdForm.addEventListener('submit', function(event) {
		    	            if (!validFindIdForm()) {
		    	                event.preventDefault(); // ì í¨ì± ê²ì¬ ì¤í¨ ì ì ì¶ ë°©ì§
		    	                console.log("ID validation failed, form submission prevented.");
		    	            }
		    	        });
		    	    }
		    	
		    	    // ë¹ë°ë²í¸ ì°¾ê¸° í¼
		    	    var findPwForm = document.getElementById('findPwForm2');
		    	    if (findPwForm) {
		    	        findPwForm.addEventListener('submit', function(event) {
		    	            if (!validFindPwForm()) {
		    	                event.preventDefault(); // ì í¨ì± ê²ì¬ ì¤í¨ ì ì ì¶ ë°©ì§
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

        // ìì ë¡ê·¸ì¸ ë²í¼ í´ë¦­ í¸ë¤ë¬
        function kakaoLogin() {
            window.location.href = '/oauth2/authorization/kakao';
        }

        function googleLogin() {
            console.log('Initiating Google login');

            fetch('/auth/google/url')
                .then(response => {
                    console.log('URL response:', response);
                    return response.text();
                })
                .then(url => {
                    console.log('Redirecting to:', url);
                    window.location.href = url;
                })
                .catch(error => {
                    console.error('Error getting Google auth URL:', error);
                    alert('êµ¬ê¸ ë¡ê·¸ì¸ ì´ê¸°í ì¤ ì¤ë¥ê° ë°ìíìµëë¤.');
                });
        }

        // íì´ì§ ë¡ëì ì´ê¸°í ë° ìì ë¡ê·¸ì¸ ì½ë°± ì²ë¦¬
        document.addEventListener('DOMContentLoaded', function() {

            console.log("DOM Content Loaded");

            const currentPath = window.location.pathname;
            const urlParams = new URLSearchParams(window.location.search);
            const code = urlParams.get('code');

            console.log('Current Path:', currentPath); // íì¬ ê²½ë¡ íì¸
            console.log('Code:', code);               // code íë¼ë¯¸í° íì¸


            // ì¹´ì¹´ì¤ ë¡ê·¸ì¸ ì½ë°± ì²ë¦¬
            if (currentPath === '/auth/kakao' && code) {
                console.log('Processing Kakao callback');
                handleKakaoCallback(code);
            }
            // êµ¬ê¸ ë¡ê·¸ì¸ ì½ë°± ì²ë¦¬
            else if (currentPath === '/auth/google' && code) {
                console.log('Processing Google callback');
                handleGoogleCallback(code);
            }
            // íìê°ì íì´ì§ìì ìì ë¡ê·¸ì¸ ì ë³´ ì²ë¦¬
            else if (currentPath === '/register') {
                console.log('Processing registration page');
                handleSocialRegistration();
            }
        });

        // ì¹´ì¹´ì¤ ë¡ê·¸ì¸ ì½ë°± ì²ë¦¬
        function handleKakaoCallback(code) {
            console.log('Handling Kakao callback with code:', code);

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
                    alert('ì¹´ì¹´ì¤ ë¡ê·¸ì¸ ì²ë¦¬ ì¤ ì¤ë¥ê° ë°ìíìµëë¤.');
                });
        }

        // êµ¬ê¸ ë¡ê·¸ì¸ ì½ë°± ì²ë¦¬
        function handleGoogleCallback(code) {
            console.log('Handling Google callback with code:', code);

            fetch(`/auth/google?code=${code}`)
                .then(response => response.json())
                .then(data => {
                    console.log('Google login response:', data);

                    if (data.status === 'success') {
                        window.location.href = data.redirectUrl;
                    } else if (data.status === 'registration_required') {
                        const params = new URLSearchParams({
                            googleId: data.providerId,
                            name: data.name,
                            email: data.email,
                            profileImage: data.profileImage,
                            provider: 'GOOGLE'
                        });
                        window.location.href = `/api/register?${params.toString()}`;
                    }
                })
                .catch(error => {
                    console.error('Google Login Error:', error);
                    alert('êµ¬ê¸ ë¡ê·¸ì¸ ì²ë¦¬ ì¤ ì¤ë¥ê° ë°ìíìµëë¤.');
                });
        }

        // íìê°ì íì´ì§ìì ìì ë¡ê·¸ì¸ ì ë³´ ì²ë¦¬
        function handleSocialRegistration() {
            const urlParams = new URLSearchParams(window.location.search);
            const provider = urlParams.get('provider');

            if (provider === 'KAKAO') {
                setupKakaoRegistration(urlParams);
            } else if (provider === 'GOOGLE') {
                setupGoogleRegistration(urlParams);
            }
        }

        // ì¹´ì¹´ì¤ íìê°ì í¼ ì¤ì 
        function setupKakaoRegistration(urlParams) {
            // ID/PW íë ì¨ê¸°ê¸°
            hideLoginFields();

            // í¼ ìë ìì±
            autoFillKakaoForm(urlParams);

            // Hidden íë ì¶ê°
            addKakaoHiddenFields(urlParams);

            // ìë´ ë©ìì§ ì¶ê°
            addSocialLoginInfo('ì¹´ì¹´ì¤');

            // ì¤íì¼ ì ì©
            applySocialLoginStyles();
        }

        // êµ¬ê¸ íìê°ì í¼ ì¤ì 
        function setupGoogleRegistration(urlParams) {
            // ID/PW íë ì¨ê¸°ê¸°
            hideLoginFields();

            // í¼ ìë ìì±
            autoFillGoogleForm(urlParams);

            // Hidden íë ì¶ê°
            addGoogleHiddenFields(urlParams);

            // ìë´ ë©ìì§ ì¶ê°
            addSocialLoginInfo('êµ¬ê¸');

            // ì¤íì¼ ì ì©
            applySocialLoginStyles();
        }

        // ì í¸ë¦¬í° í¨ìë¤
        function hideLoginFields() {
            document.querySelector('.form-group:has(#username)').style.display = 'none';
            document.querySelector('.form-group:has(#password)').style.display = 'none';
            document.querySelector('.form-group:has(#confirm-password)').style.display = 'none';
        }

        function autoFillKakaoForm(urlParams) {
            document.getElementById('nickname').value = urlParams.get('nickname') || '';
            document.getElementById('email').value = urlParams.get('email') || '';

            // ìë ìë ¥ë íë ì½ê¸° ì ì© ì¤ì 
            document.getElementById('email').readOnly = true;
            document.getElementById('nickname').readOnly = true;
        }

        function autoFillGoogleForm(urlParams) {
            document.getElementById('name').value = urlParams.get('name') || '';
            document.getElementById('email').value = urlParams.get('email') || '';

            // ìë ìë ¥ë íë ì½ê¸° ì ì© ì¤ì 
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
            infoText.innerHTML = `${provider} ê³ì ì¼ë¡ íìê°ìì ì§íí©ëë¤.<br>ì¶ê° ì ë³´ë¥¼ ìë ¥í´ì£¼ì¸ì.`;
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

        // íìê°ì í¼ ì ì¶ ì²ë¦¬
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
                        alert('íìê°ìì´ ìë£ëììµëë¤.');
                        window.location.href = '/';
                    } else {
                        alert(result.message || 'íìê°ì ì¤ ì¤ë¥ê° ë°ìíìµëë¤.');
                    }
                })
                .catch(error => {
                    console.error('Registration Error:', error);
                    alert('íìê°ì ì²ë¦¬ ì¤ ì¤ë¥ê° ë°ìíìµëë¤.');
                });
        });




    </script>