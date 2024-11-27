<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
	
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8">
    <title>myPage</title>
	<style>
		<%@ include file="styleMyPage.jsp" %>
	</style>
</head>
<body>
	<div>
		<a href="home.jsp" class="exit-button" title="BackToHome"></a>
	</div>
	
	<div id="profileContainer">
		<p id="profileP">프로필 정보</p>
		
		<div id="profilePicture">
			<button id="profileBtn" onclick="">
				<img
			        src="<%= session.getAttribute("profile_img") != null ? session.getAttribute("profile_img") : "image/myPage_image.svg" %>"
			        alt="myPage_image" title="myPage_image" class="picture">
			    <p>프로필 사진</p>
			    <p>칸을 눌러 수정할 수 있습니다.</p>
			</button>
		</div>
		
		<div id="profileId">
			<p>아이디</p>
			<p id="userId"><%= session.getAttribute("m_id") %></p>
		</div>
		
		<div id="profileName">
			<p>성명</p>
			<p id="userName"><%= session.getAttribute("name") %></p>
		</div>
		
		<div id="profileNickname">
			<p>닉네임</p>
			<p id="userNickname"><%= session.getAttribute("nickname") %></p>
		</div>
		
		<div id="profileEmail">
			<p>이메일</p>
			<p id="userEmail"><%= session.getAttribute("email") %></p>
		</div>
		
		<div id="profileGender">
			<p>성별</p>
			<p id="userGender"><%= session.getAttribute("gender") %></p>
		</div>
		
		<div id="changePassword">
			<button id="changePasswordBtn" onclick="location.href='changePassword.jsp'">비밀번호 변경</button>
		</div>
		
		<div id="save">
			<button id="saveBtn" onclick="">저장하기</button>
		</div>
		
		<div id="unsubscribe">
			<button id="unsubscribeBtn" onclick="">탈퇴하기</button>
		</div>
	</div>
	
	
	
<script>

</script>

</body>
</html>