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

	<script>
		<%@ include file="gpsSc.jsp" %>
	</script>
	<div class="register-form">
		<div class="login-form">
		<p class="regist">회원가입</p>
		
		<form action="/api/register" method="post"> <!-- 회원가입 로직을 처리할 파일로 이동 -->
			<input type="hidden" name="provider" value="${provider != null ? provider : 'LOCAL'}">
			<input type="hidden" name="kakaoId" value="${kakaoId}">
			<input type="hidden" name="profileImage" value="${profileImage}">


	            <div class="form-group">
	                <label for="username">1. 아이디를 입력해주세요</label>
	                <input type="text" id="username" name="username" placeholder="예) wowns3082" required>
	                <button type="button" class="check-button" onclick="checkUsername()">중복확인</button>
	            </div>
	            <div class="form-group">
	                <label for="password">2. 비밀번호를 입력해주세요</label>
	                <input type="password" id="password" name="password" placeholder="영문, 숫자, 특수문자 혼합 8~16자리" required>
	                <input type="password" id="confirm-password" name="confirm-password" placeholder="비밀번호 재입력" required>
	            </div>
	            <div class="form-group">
	                <label for="name">3. 성명을 입력해주세요</label>
	                <input type="text" id="name" name="name" placeholder="예) 홍길동" required>
	            </div>
	            <div class="form-group">
	                <label for="email">4. 이메일 주소를 입력해주세요</label>
	                <input type="email" id="email" name="email" placeholder="예) example123@example.net" required>
	            </div>
	            <div class="form-group">
	                <label for="nickname">5. 닉네임을 입력해주세요</label>
	                <input type="text" id="nickname" name="nickname" placeholder="예) 레갈리엔" required>
	                <button type="button" class="check-button" onclick="checkNickname()">중복확인</button>
	            </div>
	            <div class="form-group">
	                <label for="birthdate">6. 생년월일을 입력해주세요</label>
	                <input type="text" id="birthdate" name="birthdate" placeholder="예) 950714" required>
	            </div>
	            <div class="form-group">
	                <label>7. 성별을 입력해주세요.</label>
	                <input type="radio" id="male" name="gender" value="male" required>
	                <label for="male">남성</label>
	                <input type="radio" id="female" name="gender" value="female" required>
	                <label for="female">여성</label>
	                <input type="radio" id="etc-gender" name="gender" value="etc-gender" required>
	                <label for="etc-gender">그 외</label>
	            </div>
	            <div class="form-group">
	                <input type="checkbox" id="privacy" name="privacy" required>
	                <label for="privacy">개인정보 및 위치 정보 수집에 동의합니다.(필수)</label>
	            </div>
	            <button type="submit">회원가입</button>
	        </form>
		
		</div>
	</div>

	<script>
		async function checkUsername() {
			const username = document.getElementById('username').value;

			if (!username) {
				alert("아이디를 입력해주세요.");
				return;
			}

			try {
				const response = await fetch(`/api/check-id`, {
					method: 'POST',  // POST 요청으로 변경
					headers: {
						'Content-Type': 'application/json'  // JSON 형식으로 데이터 전송
					},
					body: JSON.stringify({ username: username })  // JSON 형식으로 요청 본문에 username 전달
				});

				const data = await response.json();

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
			const nickname = document.getElementById('nickname').value;

			if (!nickname) {
				alert("닉네임을 입력해주세요.");
				return;
			}

			try {
				const response = await fetch(`/api/check-nickname`, {  // URL에서 쿼리 파라미터 대신 POST 요청으로 수정
					method: 'POST',  // POST 요청 사용
					headers: {
						'Content-Type': 'application/json'  // JSON 형식으로 전송
					},
					body: JSON.stringify({ nickname: nickname })  // JSON 형식으로 요청 본문에 nickname 전달
				});

				const data = await response.json();

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

	</script>

</body>
</html>