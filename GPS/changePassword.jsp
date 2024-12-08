<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
	
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8">
    <title>changePassword</title>
	<style>
		<%@ include file="styleChangePassword.jsp" %>
	</style>
</head>
<body>
	<div>
		<a href="myPage.jsp" class="exit-button" title="BackToHome"></a>
	</div>
	
	<p id="changeP">비밀번호 변경</p>
	
	<div id="passwordInput" style="display: block;">
		<form id="passwordForm" method="post" action="">
			<label for="password" id="inputPassword">기존 비밀번호를 입력해주세요</label>
						<div class="password-container">
						<input type="password" id="password" name="password" placeholder="비밀번호 입력">
						<button type="button" id="togglePassword" class="eye-btn">
				        	<img src="image/closed_eyes.svg" alt="Toggle Password" id="eyeIcon1">
				        </button>
			</div>
				<%
					String checkPassword = (String) session.getAttribute("checkPassword");
				%>
				<!-- 고정된 오류 메시지 공간 -->
				<div id="checkPassword">
				<%
					if (checkPassword != null) {
						out.print(checkPassword.replace("<br>", "<br/>"));
					    session.removeAttribute("checkPassword");
					}
				%>
			</div>
		
			<div id="next">
						<button type="submit" id="nextBtn">다음으로</button>
			</div>
		</form>
	</div>
	
	<div id="passwordChangeInput" style="display: none;">
		<form id="ChangePasswordForm">
			<div class="form-group">
					<label for="password" id="inputPassword">변경할 비밀번호를 입력해주세요</label>
					<div class="password-container">
					<input type="password" id="password2" name="password2" placeholder="영문, 숫자, 특수문자 혼합 8~16자리">
					<button type="button" id="togglePassword2" class="eye-btn">
			        	<img src="image/closed_eyes.svg" alt="Toggle Password" id="eyeIcon1">
			        </button>
			        </div>
			        <div class="password-container">
					<input type="password" id="confirmPassword" name="confirmPassword" placeholder="비밀번호 재입력">
					<button type="button" id="togglePasswordRegist" class="eye-btn">
			        	<img src="image/closed_eyes.svg" alt="Toggle Password" id="eyeIcon2">
			        </button>
					</div>
					<%
			    	String pwError = (String) session.getAttribute("pwError");
				    %>
				    <!-- 고정된 오류 메시지 공간 -->
				    <div id="error-message-pw">
				        <%
				        if (pwError != null) {
				            out.print(pwError.replace("<br>", "<br/>"));
				            session.removeAttribute("pwError");
				        }
				        %>
				    </div>
				</div>
		
			<div id="change">
						<button type="submit" id="changeBtn" onclick="">변경하기</button>
			</div>
		</form>
	</div>
	
	<div id="modalBack" style="display: none;">
	    <div id="modal-content">
	        <p>이대로 나가시겠습니까?<br>수정된 사항은 저장되지 않습니다.</p>
	        <div id="modal-buttons">
		        <button id="cancelBack">취소</button>
		        <button id="confirmBack">나가기</button>
			</div>
	    </div>
	</div>
	
	<div id="modalEdit" style="display: none;">
	    <div id="modal-content">
	        <p>비밀번호가 새로 변경되었습니다.</p>
	        <div id="modal-buttons">
		        <button id="editComplete">확인</button>
			</div>
	    </div>
	</div>
	
	<script>
	
		var togglePassword = document.getElementById('togglePassword');
		var togglePassword2 = document.getElementById('togglePassword2');
	    var togglePasswordRegist = document.getElementById('togglePasswordRegist');
	    var passwordInput = document.getElementById('password');
	    var passwordInput2 = document.getElementById('password2');
	    var confirmPasswordInput = document.getElementById('confirmPassword');
	    var eyeIcon1 = document.getElementById('eyeIcon1');
	    var eyeIcon2 = document.getElementById('eyeIcon2');
	
	    togglePassword.addEventListener('click', function() {
	        var type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
	        passwordInput.setAttribute('type', type);
	        
	        eyeIcon1.src = type === 'password' ? 'image/closed_eyes.svg' : 'image/open_eyes.svg';
	    });
	    
	    togglePassword2.addEventListener('click', function() {
	        var type = passwordInput2.getAttribute('type') === 'password' ? 'text' : 'password';
	        passwordInput2.setAttribute('type', type);
	        
	        eyeIcon1.src = type === 'password' ? 'image/closed_eyes.svg' : 'image/open_eyes.svg';
	    });
	    
	    togglePasswordRegist.addEventListener('click', function() {
	    	var type = confirmPasswordInput.getAttribute('type') === 'password' ? 'text' : 'password';
	    	confirmPasswordInput.setAttribute('type', type);
	    	
	    	eyeIcon2.src = type === 'password' ? 'image/closed_eyes.svg' : 'image/open_eyes.svg';
	    });
	    
		document.addEventListener('DOMContentLoaded', function() {
		    
			var passwordForm = document.getElementById('passwordForm');
		    var password = passwordForm.querySelector('input[name="password"]');
		
		    passwordForm.addEventListener('submit', function(event) {
		        if (!validForm()) {
		            event.preventDefault();
		        }
		    });
		
		 // 유효성 검사
		    function validForm() {
		        var isValid = true;

		        if (password.value.trim() === "") {
		            password.classList.add('error');
		            password.placeholder = "비밀번호를 입력하지 않았습니다."; // placeholder 변경
		            isValid = false;
		        } else {
		            password.classList.remove('error');
		            password.placeholder = "비밀번호 입력"; // 기본 placeholder로 복원
		        }

		        return isValid;
		    }

		    // 유효성 검사 이후 pw 입력 폼 클릭 시 error 제거
		    password.addEventListener('focus', function() {
		        password.classList.remove('error');
		        password.placeholder = "비밀번호 입력"; // 기본 placeholder로 복원
		    });
		});
		
		document.addEventListener('DOMContentLoaded', function() {
		    // 비밀번호 변경 폼 요소 가져오기
		    var changePasswordForm = document.getElementById('ChangePasswordForm');
		    var password2 = changePasswordForm.querySelector('input[name="password2"]');
		    var confirmPasswordInput = changePasswordForm.querySelector('input[name="confirmPassword"]');
		    var errorMessageDiv = document.getElementById('error-message-pw');

		    // 비밀번호 변경 폼 제출 이벤트
		    changePasswordForm.addEventListener('submit', function(event) {
		        if (!validForm()) {
		            event.preventDefault(); // 기본 제출 방지
		        }
		    });

		    // 유효성 검사 함수
		    function validForm() {
		        var isValid = true;

		        // 비밀번호 변경 입력값 검사
		        if (password2.value.trim() === "") {
		            password2.classList.add('error');
		            password2.placeholder = "비밀번호를 입력하지 않았습니다."; // placeholder 변경
		            isValid = false;
		        } else {
		            password2.classList.remove('error');
		            password2.placeholder = "영문, 숫자, 특수문자 혼합 8~16자리"; // 기본 placeholder로 복원
		        }

		        // 비밀번호 확인 필드 검사
		        if (confirmPasswordInput.value.trim() === "") {
		            confirmPasswordInput.classList.add('error');
		            isValid = false;
		        } else {
		            confirmPasswordInput.classList.remove('error');
		        }

		        // 비밀번호와 비밀번호 확인 일치 검사
		        if (password2.value !== confirmPasswordInput.value) {
		            isValid = false;
		            confirmPasswordInput.classList.add('error');
		            errorMessageDiv.innerHTML = "비밀번호가 일치하지 않습니다."; // 오류 메시지 표시
		        } else {
		            errorMessageDiv.innerHTML = ""; // 오류 메시지 초기화
		        }

		        return isValid;
		    }

		    // 유효성 검사 이후 pw 입력 폼 클릭 시 error 제거
		    password2.addEventListener('focus', function() {
		        password2.classList.remove('error');
		        password2.placeholder = "영문, 숫자, 특수문자 혼합 8~16자리"; // 기본 placeholder로 복원
		    });

		    confirmPasswordInput.addEventListener('focus', function() {
		        confirmPasswordInput.classList.remove('error');
		        confirmPasswordInput.placeholder = "비밀번호 재입력"; // 기본 placeholder로 복원
		    });
		});
		
		document.addEventListener('DOMContentLoaded', function() {
		    var passwordForm = document.getElementById('passwordForm');
		    var passwordInput = document.getElementById('password');
		    var passwordChangeInput = document.getElementById('passwordChangeInput');

		    passwordForm.addEventListener('submit', function(event) {
		        event.preventDefault(); // 기본 제출 방지

		        // 기존 비밀번호 입력값 가져오기
		        var inputPassword = passwordInput.value.trim();

		        // AJAX 요청을 통해 비밀번호 확인 (여기서는 예시로 직접 비교)
		        // 실제로는 서버에서 비밀번호 확인을 해야 함
		        var storedPassword = '<%= session.getAttribute("userPassword") %>'; // JSP에서 세션의 비밀번호 가져오기

		        if (inputPassword === storedPassword) {
		            // 비밀번호가 일치하면 비밀번호 변경 폼 표시
		            passwordInput.closest('div').style.display = 'none'; // 기존 비밀번호 입력 폼 숨기기
		            passwordChangeInput.style.display = 'block'; // 비밀번호 변경 폼 표시
		        } else {
		            // 비밀번호가 일치하지 않을 경우 (오류 메시지 표시)
		            var checkPasswordDiv = document.getElementById('checkPassword');
		            checkPasswordDiv.innerHTML = "비밀번호가 일치하지 않습니다."; // 오류 메시지
		        }
		    });
		});
		
		// 변경 완료 모달창
		document.addEventListener('DOMContentLoaded', function() {
		    var changeBtn = document.getElementById('changeBtn');
		    var modal = document.getElementById('modalEdit');
		    var editComplete = document.getElementById('editComplete');

		    changeBtn.addEventListener('click', function(event) {
		        event.preventDefault();
		        modal.style.display = 'block';
		    });

		    editComplete.addEventListener('click', function() {
		    	event.preventDefault();
		        modal.style.display = 'none';
		    });
		    
		    window.addEventListener('click', function(event) {
		        if (event.target === modal) {
		            modal.style.display = 'none';
		        }
		    });
		    
		    window.addEventListener('beforeunload', function(event) {
		        event.preventDefault();
		        modal.style.display = 'block';
		        return '';
		    });
		});
		
		// 뒤로가기 버튼 모달창
		document.addEventListener('DOMContentLoaded', function() {
		    var backButton = document.querySelector('.exit-button');
		    var modal = document.getElementById('modalBack');
		    var confirmBack = document.getElementById('confirmBack');
		    var cancelBack = document.getElementById('cancelBack');
		
		    // 뒤로가기 버튼 클릭 시 모달 열기
		    backButton.addEventListener('click', function(event) {
		        event.preventDefault(); // 기본 동작 방지
		        modal.style.display = 'block'; // 모달 표시
		    });
		
		    // 확인 버튼 클릭 시 뒤로가기
		    confirmBack.addEventListener('click', function() {
		    	window.location.href = 'myPage.jsp'
		    });
		
		    // 취소 버튼 클릭 시 모달 닫기
		    cancelBack.addEventListener('click', function() {
		        modal.style.display = 'none'; // 모달 숨김
		    });
		
		    // 창 밖을 클릭하면 모달 닫기
		    window.addEventListener('click', function(event) {
		        if (event.target === modal) {
		            modal.style.display = 'none';
		        }
		    });
		    
		 	// 브라우저의 뒤로가기 버튼 클릭 시 모달 표시
		    window.addEventListener('beforeunload', function(event) {
		        event.preventDefault(); // 기본 동작 방지
		        modal.style.display = 'block'; // 모달 표시
		        return ''; // 모달을 표시하기 위해 빈 문자열 반환
		    });
		});
	</script>
</body>
</html>