<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
    <div class="findBtn">
    	<div class="images">
	    		<img src="image/logo.png" alt="logo1" title="logo" class="picture">
	    		<img src="image/LookForYourMovement.png" alt="moto" title="moto" class="picture">
	    </div>
	    
	    <!-- 뒤로가기 버튼 -->
	    <button id="cancelBtn" style="display: none;" onclick="showForm('findBtn')">
	    	<img src="image/icon_back.png" alt="뒤로가기">
	    </button>
	    
	    <div id="idPw-btn">
		    <div id="findIdPw">
				<button id="findId-btn" onclick="showForm('findId')">아이디 찾기</button>
				<button id="findPw-btn" onclick="showForm('findPw')">비밀번호 찾기</button>
			</div>
		</div>
		<div id="findIdForm" style="display: none;">
			<div class="form-container">
                <p>아이디 찾기</p>
                <!-- action 파일명 변경 필요 -->
                <form id="findIdForm2" action="findIdCertification.jsp" method="post"> 
                    <input id="findIdF"type="text" name="name" placeholder="이름을 입력해주세요">
                    <p id="PlzEmail">이메일 주소를 적어주세요</p>
                    <div class="email-input">
			            <input type="text" id="findIdEmailId" name="emailId">
			            <span>@</span>
			            <input type="text" id="findIdEmailDomain" name="emailDomain">
			        </div>
                    <button type="submit">다음으로</button>
                </form>
            </div>
        </div>
		<div id="findPwForm" style="display: none;">
			<div class="form-container">
                <p>비밀번호 찾기</p>
                <!-- action 파일명 변경 필요 -->
                <form id="findPwForm2" action="findPwCertification.jsp" method="post"> 
                    <input id="findPwF"type="text" name="name" placeholder="아이디를 입력해주세요">
                    <p id="PlzEmail">이메일 주소를 적어주세요</p>
                    <div class="email-input">
			            <input type="text" id="findPwEmailId" name="emailId">
			            <span>@</span>
			            <input type="text" id="findPwEmailDomain" name="emailDomain">
			        </div>
                    <button type="submit">다음으로</button>
				</form>
			</div>
		</div>
	</div>
    <button id="toLogin" onclick="location.reload();">로그인 화면</button>
</div>