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
	
	<p id="profileP">프로필 정보</p>
	
	<div id="editContainerDisplay" style="display: none;">
		<form action="/api/" method="post" id="editMyPageForm"> <!-- 프로필 변경 -->
			<div id="profile">
				<div id="profileStyle">
					<p id="editProfilePicture">프로필 사진</p>
					<img
				        src="<%= session.getAttribute("profile_img") != null ? session.getAttribute("profile_img") : "image/myPage_image.svg" %>"
				        alt="edit_myPage_image" title="edit_myPage_image" id="editMyPageImage">
				        <div id="editProfileBtn">
				        	<input type="file" id="profileImageInput" accept="image/*" style="display: none;" />
							<button type="button" id="changeProfilePicture">변경하기</button>
							<button type="button" id="cancelProfilePicture">삭제하기</button>
						</div>
				</div>
			</div>
			
			<div id="editProfileId">
				<p class="pTitle">아이디</p>
				<p id="userId"><%= session.getAttribute("m_id") %></p>
			</div>
			
			<div id="editProfileName">
				<p class="pTitle">성명</p>
				<input type="text" id="userNameInput" value="<%= session.getAttribute("name") %>">
			</div>
			
			<div id="editProfileNickname">
				<p class="pTitle">닉네임</p>
				<input type="text" id="userNicknameInput" value="<%= session.getAttribute("nickname") %>">
				<button type="button" id="checkNickname">중복확인</button>
					<%
			    	String checkNname = (String) session.getAttribute("checkNname");
				    %>
				    <!-- 고정된 오류 메시지 공간 -->
				    <div id="checkNname">
				        <%
				        if (checkNname != null) {
				            out.print(checkNname.replace("<br>", "<br/>"));
				            session.removeAttribute("checkNname");
				        }
				        %>
				    </div>
			</div>
			
			<div id="editProfileEmail">
				<p class="pTitle">이메일</p>
					<div class="email-input">
			            <input type="text" id="editIdEmailId" name="emailId">
			            <span id="emailSpan">@</span>
			            <!-- 직접 입력 폼 제작 필 -->
			            <select id="editIdEmailDomain" name="emailDomain" onchange="toggleCustomDomainInput('editIdEmailDomain', 'customDomainInput')">
			            	<option value="google.com">google.com</option>
                            <option value="naver.com">naver.com</option>
                            <option value="daum.net">daum.net</option>
			            </select>
			        </div>
			</div>
			
			<div id="editProfileGender">
				<p class="pTitle">성별</p>
					<!-- 기존 성별 가지고와서 입력하는거 필요함 -->
					<div id="gender-form-group">
		                <input type="radio" id="male" name="gender" value="male"
		                	<%= "M".equals(session.getAttribute("gender")) ? "checked" : "" %>>
		                <label for="male">남성</label>
		                <input type="radio" id="female" name="gender" value="female"
		                	<%= "F".equals(session.getAttribute("gender")) ? "checked" : "" %>>
		                <label for="female">여성</label>
		                <input type="radio" id="etc-gender" name="gender" value="etc-gender"
		                	<%= "etc".equals(session.getAttribute("gender")) ? "checked" : "" %>>
		                <label for="etc-gender">그 외</label>
	                </div>
			</div>
			
			<div id="changePassword">
									<!-- changePassword.jsp -->
				<button id="changePasswordBtn" onclick="/api/">비밀번호 변경</button>
			</div>
									<!-- 내 정보 수정 데이터 저장 -->
			<div id="save">
				<button id="saveBtn2" onclick="/api/">저장하기</button>
			</div>
			
			<div id="unsubscribe">
										<!-- 계정 탈퇴 -->
				<button id="unsubscribeBtn2" onclick="/api/">탈퇴하기</button>
			</div>
		</form>
	</div>
    
    
    <div id="profileContainerDisplay" style="display: block;">
		<div id="profileContainer">
			
			<div id="profile">
				<button id="profileBtn" onclick="toggleEditMode()">
					<img
				        src="<%= session.getAttribute("profile_img") != null ? session.getAttribute("profile_img") : "image/myPage_image.svg" %>"
				        alt="myPage_image" title="myPage_image" id="myPageImage">
				    <p id="profilePicture">프로필 사진</p>
				    <p id="tapToEdit">칸을 눌러 수정할 수 있습니다.</p>
				</button>
			</div>
			
			<div id="profileId">
				<p class="pTitle">아이디</p>
				<p id="userId"><%= session.getAttribute("m_id") %></p>
			</div>
			
			<div id="profileName">
				<p class="pTitle">성명</p>
				<p id="userName"><%= session.getAttribute("name") %></p>
			</div>
			
			<div id="profileNickname">
				<p class="pTitle">닉네임</p>
				<p id="userNickname"><%= session.getAttribute("nickname") %></p>
			</div>
			
			<div id="profileEmail">
				<p class="pTitle">이메일</p>
				<p id="userEmail"><%= session.getAttribute("email") %></p>
			</div>
			
			<div id="profileGender">
				<p class="pTitle">성별</p>
				<p id="userGender"><%= session.getAttribute("gender") %></p>
			</div>
			
			<div id="changePassword">
									<!-- changePassword.jsp -->
				<button id="changePasswordBtn" onclick="/api/">비밀번호 변경</button> 
			</div>
			
			<div id="save">
									<!-- 내 정보 수정 데이터 저장 -->
				<button id="saveBtn1" onclick="/api/">저장하기</button>
			</div>
			
			<div id="unsubscribe">
										<!-- 계정 탈퇴 -->
				<button id="unsubscribeBtn1" onclick="/api/">탈퇴하기</button>
			</div>
		</div>
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
	
	<div id="modalDelete" style="display: none;">
	    <div id="modal-content">
	        <p>정말 탈퇴하시겠습니까?<br>계정은 삭제되며, 복구되지 않습니다.</p>
	        <div id="modal-buttons">
		        <button id="confirmDelete">탈퇴</button>
		        <button id="cancelDelete">취소</button>
			</div>
	    </div>
	</div>
	
	<div id="modalEdit" style="display: none;">
	    <div id="modal-content">
	        <p>변경사항이 저장되었습니다.</p>
	        <div id="modal-buttons">
		        <button id="editComplete">확인</button>
			</div>
	    </div>
	</div>
	
<script>
	function toggleEditMode() {
	    var editContainerDisplay = document.getElementById("editContainerDisplay");
	    var profileContainerDisplay = document.getElementById("profileContainerDisplay");
	
	    var editing = editContainerDisplay.style.display === "block";
	
	    // 편집 모드 전환
	    if (editing) {
	    	editContainerDisplay.style.display = "none"; // 편집 컨테이너 숨김
	    	profileContainerDisplay.style.display = "block"; // 기본 프로필 정보 표시
	    } else {
	    	editContainerDisplay.style.display = "block"; // 편집 컨테이너 표시
	    	profileContainerDisplay.style.display = "none"; // 기본 프로필 정보 숨김
	    }
	}
	
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
	    	window.location.href = 'home.jsp' // 이전 페이지로 이동
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
	
	// 탈퇴하기 버튼 클릭 시 모달창
    document.addEventListener('DOMContentLoaded', function() {
	    var unsubscribeBtn1 = document.getElementById('unsubscribeBtn1');
	    var unsubscribeBtn2 = document.getElementById('unsubscribeBtn2');
	    var modal = document.getElementById('modalDelete');
	    var confirmDelete = document.getElementById('confirmDelete');
	    var cancelDelete = document.getElementById('cancelDelete');
	
	    unsubscribeBtn1.addEventListener('click', function(event) {
	        event.preventDefault(); // 기본 동작 방지
	        modal.style.display = 'block'; // 모달 표시
	    });
	    
	    unsubscribeBtn2.addEventListener('click', function(event) {
	        event.preventDefault(); // 기본 동작 방지
	        modal.style.display = 'block'; // 모달 표시
	    });
	
	    // 탈퇴 버튼 클릭 시 회원탈퇴
	    confirmDelete.addEventListener('click', function() {
	    	// 탈퇴하는 코드
	    });
	
	    // 취소 버튼 클릭 시 모달 닫기
	    cancelDelete.addEventListener('click', function() {
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
    
    document.addEventListener('DOMContentLoaded', function() {
	    var saveBtn1 = document.getElementById('saveBtn1');
	    var saveBtn2 = document.getElementById('saveBtn2');
	    var modal = document.getElementById('modalEdit');
	    var editComplete = document.getElementById('editComplete');

	    saveBtn1.addEventListener('click', function(event) {
	        event.preventDefault();
	        modal.style.display = 'block';
	    });
	    
	    saveBtn2.addEventListener('click', function(event) {
	        event.preventDefault();
	        modal.style.display = 'block';
	    });

	    editComplete.addEventListener('click', function() {
	    	event.preventDefault();
	        modal.style.display = 'none';
	        
	        var editContainerDisplay = document.getElementById("editContainerDisplay");
		    var profileContainerDisplay = document.getElementById("profileContainerDisplay");
		
		    var editing = editContainerDisplay.style.display === "block";
		
		    // 편집 모드 전환
		    if (editing) {
		    	editContainerDisplay.style.display = "none"; // 편집 컨테이너 숨김
		    	profileContainerDisplay.style.display = "block"; // 기본 프로필 정보 표시
		    } else {
		    	editContainerDisplay.style.display = "block"; // 편집 컨테이너 표시
		    	profileContainerDisplay.style.display = "none"; // 기본 프로필 정보 숨김
		    }
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
</script>

</body>
</html>