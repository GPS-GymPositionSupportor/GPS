<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8">
    <title>Register</title>
    <style>
        <%@ include file="style.jsp" %>
    </style>
</head>
<body>
	<div>
		<a href="index.jsp" class="exit-button" title="BackToIndex"></a>
	</div>

	<%@ include file="gpsSc.jsp" %>
		
	<div class="register-form">
		<div class="login-form">
		<p class="regist">회원가입</p>
		
		<form action="/api/register" method="post"> <!-- 회원가입 로직을 처리할 파일로 이동 -->
			<!-- 공통 hidden 필드 -->
			<input type="hidden" name="provider" value="${provider != null ? provider : 'LOCAL'}">
			<input type="hidden" name="profileImage" value="${profileImage}">

			<input type="hidden" name="kakaoId" value="${kakaoId}">
			<input type="hidden" name="googleId" value="${googleId}">

			<script>
				console.log("googleId = {}", ${googleId});
			</script>

			<!-- ID/PW 필드 (소셜 로그인시 숨김) -->
				<div class="form-group">
					<label for="username">1. 아이디를 입력해주세요</label>
					<div id="id-form-group">
						<input type="text" id="username" name="username" placeholder="예) wowns3082">
						<%
				    	String idError = (String) session.getAttribute("idError");
					    %>
					    <!-- 고정된 오류 메시지 공간 -->
					    <div id="error-message-id">
					        <%
					        if (idError != null) {
					            out.print(idError.replace("<br>", "<br/>"));
					            session.removeAttribute("idError");
					        }
					        %>
					    </div>
						<button type="button" id="duplicationCheck" onclick="checkUsername()">중복확인</button>
						</div>
					</div>
				<div class="form-group">
					<label for="password">2. 비밀번호를 입력해주세요</label>
					<div class="password-container">
					<input type="password" id="password" name="password" placeholder="영문, 숫자, 특수문자 혼합 8~16자리">
					<button type="button" id="togglePassword" class="eye-btn">
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

	            <div class="form-group">
	                <label for="name">3. 성명을 입력해주세요</label>
	                <input type="text" id="name" name="name" placeholder="예) 홍길동">
	            </div>
	            <div class="form-group">
	                <label for="nickname">4. 닉네임을 입력해주세요</label>
	                <div id="nickname-form-group">
		                <input type="text" id="nickname" name="nickname" placeholder="예) 레갈리엔">
		                <%
				    	String nicknameError = (String) session.getAttribute("nicknameError");
					    %>
					    <!-- 고정된 오류 메시지 공간 -->
					    <div id="error-message-nickname">
					        <%
					        if (nicknameError != null) {
					            out.print(nicknameError.replace("<br>", "<br/>"));
					            session.removeAttribute("nicknameError");
					        }
					        %>
					    </div>
		                <button type="button" id="duplicationCheck" onclick="checkNickname()">중복확인</button>
	            	</div>
	            </div>
				<div class="form-group">
					<label for="email">5. 이메일 주소를 입력해주세요</label>
					<!--  
					<c:choose>
						<c:when test="${provider == 'GOOGLE'}">
							<input type="email" id="email" name="email" value="${email}" readonly>
						</c:when>
						<c:otherwise>
							<input type="email" id="email" name="email" placeholder="예) example123@example.net" required>
						</c:otherwise>
					</c:choose> -->
					<div id="email-form-group">
						<div id="regist-email">
				            <input type="text" id="registerEmail" name="emailId">
				            <span>@</span>
				            <select id="registerEmailDomain" name="registerEmailDomain" onchange="toggleCustomDomainInput('findIdEmailDomain', 'customDomainInput')">
				            	<option value="google.com">google.com</option>
	                            <option value="naver.com">naver.com</option>
	                            <option value="daum.net">daum.net</option>
				            </select>
				        </div>
			        	<button type="button" id="duplicationCheckEmail" onclick="checkEmail()">인증하기</button>
					</div>
				</div>
				<%
				String emailError = (String) session.getAttribute("emailError");
				%>
				<!-- 고정된 오류 메시지 공간 -->
				<div id="error-message-email">
					<%
						if (emailError != null) {
						out.print(emailError.replace("<br>", "<br/>"));
						session.removeAttribute("emailError");
						}
					%>
				</div>
	            <div class="form-group">
	                <label for="birthdate">6. 생년월일을 입력해주세요</label>
	                <div id="birthdate-form-group">
				        <select id="birthYear" name="birthYear">
				        	<option disabled selected>0000</option>
				            <% 
				                int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
				                for (int year = currentYear; year >= currentYear - 100; year--) {
				            %>
				                <option value="<%= year %>"><%= year %></option>
				            <% } %>
				        </select>
						<p class="birthP">년</p>
				        <select id="birthMonth" name="birthMonth">
				        	<option disabled selected>0</option>
				            <% for (int month = 1; month <= 12; month++) { %>
				                <option value="<%= month %>"><%= month %></option>
				            <% } %>
				        </select>
						<p class="birthP">월</p>
				        <select id="birthDay" name="birthDay">
				        <option disabled selected>00</option>
				        </select>
				        <p class="birthP">일</p>
				        
				    </div>
	            </div>
	            <div id="gender">
	                <label>7. 성별을 입력해주세요.</label>
	                <div id="gender-form-group">
		                <input type="radio" id="male" name="gender" value="male">
		                <label for="male">남성</label>
		                <input type="radio" id="female" name="gender" value="female">
		                <label for="female">여성</label>
		                <input type="radio" id="etc-gender" name="gender" value="etc-gender">
		                <label for="etc-gender">그 외</label>
	                </div>
	            </div>
	            <div id="checkbox">
	                <input type="checkbox" id="privacy" name="privacy">
	                <label for="privacy" id="privacyLabel">개인정보 및 위치 정보 수집에 동의합니다. (필수)</label>
	            </div>
	            <button type="submit" id="registBtn">가입하기</button>
	        </form>
		
		</div>
	</div>
	
	<div id="modalBack" style="display: none;">
	    <div id="modal-content">
	        <p>이대로 나가시겠습니까?<br>입력된 사항은 저장되지 않습니다.</p>
	        <div id="modal-buttons">
		        <button id="cancelBack">취소</button>
		        <button id="confirmBack">나가기</button>
			</div>
	    </div>
	</div>
	
	<div id="modalRegist" style="display: none;">
	    <div id="modal-content">
	        <p>회원가입 되었습니다.</p>
	        <div id="modal-buttons">
		        <button id="registComplete">확인</button>
			</div>
	    </div>
	</div>
	
	<script>
		async function checkUsername() {
			var username = document.getElementById('username').value;

			if (!username) {
				alert("아이디를 입력해주세요.");
				return;
			}

			try {
				var response = await fetch(`/api/check-id`, {
					method: 'POST',  // POST 요청으로 변경
					headers: {
						'Content-Type': 'application/json'  // JSON 형식으로 데이터 전송
					},
					body: JSON.stringify({ username: username })  // JSON 형식으로 요청 본문에 username 전달
				});

				var data = await response.json();

				if (data.isDuplicate) {
					alert("이미 사용 중인 아이디입니다.");
				} else {
					alert("사용 가능한 아이디입니다.");
				}
			} catch (error) {
				console.error('Error:', error);
				alert("중복 확인 중 오류가 발생했습니다.");
			}
		}


		async function checkNickname() {
			var nickname = document.getElementById('nickname').value;

			if (!nickname) {
				alert("닉네임을 입력해주세요.");
				return;
			}

			try {
				var response = await fetch(`/api/check-nickname`, {  // URL에서 쿼리 파라미터 대신 POST 요청으로 수정
					method: 'POST',  // POST 요청 사용
					headers: {
						'Content-Type': 'application/json'  // JSON 형식으로 전송
					},
					body: JSON.stringify({ nickname: nickname })  // JSON 형식으로 요청 본문에 nickname 전달
				});

				var data = await response.json();

				if (data.isDuplicate) {
					alert("이미 사용 중인 닉네임입니다.");
				} else {
					alert("사용 가능한 닉네임입니다.");
				}
			} catch (error) {
				console.error('Error:', error);
				alert("중복 확인 중 오류가 발생했습니다.");
			}
		}


		document.addEventListener('DOMContentLoaded', function() {
			var provider = '${provider}';

			if (provider) {
				// 소셜 로그인 정보 표시
				var infoDiv = document.createElement('div');
				infoDiv.className = `social-login-info ${provider.toLowerCase()}-login-info`;
				infoDiv.textContent = `${provider} 계정으로 회원가입을 진행합니다.`;
				document.querySelector('form').prepend(infoDiv);

				// 이메일 필드가 readonly일 때 스타일 적용
				var emailInput = document.getElementById('email');
				if (emailInput.readOnly) {
					emailInput.classList.add('readonly');
				}
			}
		});

		// 회원가입 폼 연월에 따른 일 수
        document.getElementById('birthMonth').addEventListener('change', function() {
		    var month = parseInt(this.value);
		    var year = parseInt(document.getElementById('birthYear').value); // 년도 값 가져오기
		    var daySelect = document.getElementById('birthDay');
		
		    // 일 옵션 초기화
		    daySelect.innerHTML = '<option value="">00</option>';
		
		    if (!month || !year) return; // 월이나 년도가 선택되지 않았다면 종료
		
		    let daysInMonth;
		    if (month === 2) {
		        // 2월의 경우, 윤년 고려
		        if (year % 4 === 0 && (year % 100 !== 0 || year % 400 === 0)) {
		            daysInMonth = 29; // 윤년
		        } else {
		            daysInMonth = 28; // 비윤년
		        }
		    } else if ([4, 6, 9, 11].includes(month)) {
		        daysInMonth = 30; // 30일인 월
		    } else {
		        daysInMonth = 31; // 31일인 월
		    }
		
		    // 일 옵션 추가
		    for (let day = 1; day <= daysInMonth; day++) {
		        var option = document.createElement('option');
		        option.value = day;
		        option.textContent = day;
		        daySelect.appendChild(option);
		    }
		});
		
		
		// 비밀번호 눈 아이콘
        var togglePassword = document.getElementById('togglePassword');
        var togglePasswordRegist = document.getElementById('togglePasswordRegist');
	    var passwordInput = document.getElementById('password');
	    var confirmPasswordInput = document.getElementById('confirmPassword');
	    var eyeIcon1 = document.getElementById('eyeIcon1');
	    var eyeIcon2 = document.getElementById('eyeIcon2');

	    togglePassword.addEventListener('click', function() {
	        var type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
	        passwordInput.setAttribute('type', type);
	        
	        eyeIcon1.src = type === 'password' ? 'image/closed_eyes.svg' : 'image/open_eyes.svg';
	    });
	    
	    togglePasswordRegist.addEventListener('click', function() {
	    	var type = confirmPasswordInput.getAttribute('type') === 'password' ? 'text' : 'password';
	    	confirmPasswordInput.setAttribute('type', type);
	    	
	    	eyeIcon2.src = type === 'password' ? 'image/closed_eyes.svg' : 'image/open_eyes.svg';
	    });
	    
	    // 뒤로가기 모달창
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
		        window.history.back(); // 이전 페이지로 이동
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