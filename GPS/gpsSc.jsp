	<script>
		document.addEventListener('DOMContentLoaded', function() {
		    // 카카오 초기화
		    Kakao.init('f419f845f436bdd78e7684080e263b3c');
		});
		
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
		        
		        eyeIcon.src = type === 'password' ? 'image/closed_eyes.svg' : 'image/open_eyes.svg'; // ë¹ë°ë²í¸ê° ë³´ì¼ ë ìì´ì½ ë³ê²½
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
		                        <img src="image/myPage_image.png" alt="myPage_image" title="myPage_image" class="picture">
		                        <div class="greeting">
		                            <a class="hello">ìëíì¸ì</a>
		                            <div class="mrUser">
		                                <a class="mrUserName"><%= session.getAttribute("userID") %></a>
		                                <a class="mr">ë</a>
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
		                    <form action="logout.jsp" method="post" id="logoutForm">
		                        <button type="submit" id="logoutButton">ë¡ê·¸ìì <img src="image/Frame.png" alt="logout_icon" class="logout_icon"></button>
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
	                    <form action="logout.jsp" method="post">
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
		            
		            // id 찾기 폼
		            var findIdForm = document.getElementById('findIdForm2');
		    	    if (findIdForm) {
		    	        findIdForm.addEventListener('submit', function(event) {
		    	            if (!validFindIdForm()) {
		    	                event.preventDefault(); // 유효성 검사 실패 시 제출 방지
		    	                console.log("ID validation failed, form submission prevented.");
		    	            }
		    	        });
		    	    }
		    	
		    	    // 비밀번호 찾기 폼
		    	    var findPwForm = document.getElementById('findPwForm2');
		    	    if (findPwForm) {
		    	        findPwForm.addEventListener('submit', function(event) {
		    	            if (!validFindPwForm()) {
		    	                event.preventDefault(); // 유효성 검사 실패 시 제출 방지
		    	                console.log("Password validation failed, form submission prevented.");
		    	            }
		    	        });
		    	    }     
				})
		        .catch(error => console.error('Error loading findIdPw.jsp:', error));
		});
		
		//id찾기 유효성 검사 함수
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
		
		//pw찾기 유효성 검사 함수
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
		
		function googleLogin() {
            gapi.load('auth2', function() {
                var auth2 = gapi.auth2.init({
                    client_id: 'YOUR_GOOGLE_CLIENT_ID', //구글 api 코드
                });

                auth2.signIn().then(function(user) {
                    var profile = user.getBasicProfile();
                    var userData = {
                        email: profile.getEmail(),
                        name: profile.getName(),
                    };
                    fillRegisterFormWithGoogle(userData);
                });
            });
        }
		
        function kakaoLogin() {
            Kakao.Auth.login({
                success: function(authObj) {
                    Kakao.API.request({
                        url: '/v2/user/me',
                        success: function(response) {
                            var userData = {
                                nickname: response.properties.nickname, //데이터 이름 뭔지 찾아야함
                                profileImage: response.properties.profile_image,
                            };
                            fillRegisterFormWithKakao(userData);

                            // 회원가입 폼으로 이동
                            window.location.href = 'register.jsp';
                        },
                        fail: function(error) {
                            console.error(error);
                        }
                    });
                },
                fail: function(err) {
                    console.error(err);
                }
            });
        }

        function fillRegisterFormWithGoogle(data) {
            document.getElementById('email').value = data.email;
            document.getElementById('name').value = data.name;
        }

        function fillRegisterFormWithKakao(data) {
            document.getElementById('nickname').value = data.nickname;
        }

    </script>