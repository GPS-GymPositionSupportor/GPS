<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<script>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		
		document.addEventListener('DOMContentLoaded', function() {

            var loginForm = document.getElementById('loginForm');
            var username = loginForm.querySelector('input[name="username"]');
            var password = loginForm.querySelector('input[name="password"]');

            loginForm.addEventListener('submit', function (event) {
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
            username.addEventListener('focus', function () {
                username.classList.remove('error');
            });

            password.addEventListener('focus', function () {
                password.classList.remove('error');
            });

            // pw 입력 폼 눈 아이콘
            var togglePassword = document.getElementById('togglePassword');
            var passwordInput = document.getElementById('password');
            var eyeIcon = document.getElementById('eyeIcon');

            togglePassword.addEventListener('click', function () {
                var type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
                passwordInput.setAttribute('type', type);

                eyeIcon.src = type === 'password' ? '../image/closed_eyes.svg' : '../image/open_eyes.svg';
            });

            <%-- 햄버거 버튼 --%>
            var burButton = document.querySelector('.burbutton');
            var navLinks = document.getElementById('nav-links');
            if (burButton && navLinks) { // 두 요소가 모두 존재할 때만 실행
                burButton.addEventListener('click', (event) => {
                    event.currentTarget.classList.toggle('active');
                    navLinks.classList.toggle('active');
                });
            }

            /* document.getElementById('findIdPwBtn').addEventListener('click', function () {
                fetch('findIdPw.jsp')
                    .then(response => response.text())
                    .then(data => {
                        // 가져온 데이터를 login-form 요소에 삽입
                        document.querySelector('.login-form').innerHTML = data;

                        // id 찾기 폼 가져옴
                        var findIdForm = document.getElementById('findIdForm2');
                        if (findIdForm) {
                            findIdForm.addEventListener('submit', function (event) {
                                if (!validFindIdForm()) {
                                    event.preventDefault(); // 유효성 검사 실패 시 폼 제출 방지
                                    console.log("ID validation failed, form submission prevented.");
                                }
                            });
                        }

                        // pw 찾기 폼 가져옴
                        var findPwForm = document.getElementById('findPwForm2');
                        if (findPwForm) {
                            findPwForm.addEventListener('submit', function (event) {
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
            document.addEventListener('DOMContentLoaded', function () {
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
            document.addEventListener('DOMContentLoaded', function () {
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
            document.addEventListener('DOMContentLoaded', function () {
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

        }); */


    </script>