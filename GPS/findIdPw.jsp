<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
    <div class="findBtn">
    	<div class="images">
	    		<img src="image/logo.png" alt="logo1" title="logo" class="picture">
	    		<img src="image/LookForYourMovement.png" alt="moto" title="moto" class="picture">
	    </div>
	    
	    <!-- 뒤로가기 버튼 -->
	    <button id="cancelBtn" style="display: none;" onclick="showForm('findBtn')">
	    <span></span>
	    </button>
	    
	    <div id="idPw-btn">
		    <div id="findIdPw">
				<button id="findId-btn" onclick="showForm('findId')">아이디 찾기</button>
				<button id="findPw-btn" onclick="showForm('findPw')">비밀번호 찾기</button>
			</div>
		</div>
		<div id="findIdForm" style="display: none;">
                <a>아이디 찾기</a>
                <!-- action 파일명 변경 필요 -->
                <form action="findIdProcess.jsp" method="post"> 
                    <input type="text" name="name" placeholder="이름을 입력해주세요">
                    <p>이메일 주소를 적어주세요</p>
                    <input type="email" name="email">
                    <button type="submit">다음으로</button>
                </form>
            </div>
            
		<div id="findPwForm" style="display: none;">
                <h4>비밀번호 찾기</h4>
                <form action="findPwProcess.jsp" method="post">
                    <input type="text" name="username" placeholder="아이디를 입력해주세요">
                    <input type="email" name="email" placeholder="이메일을 입력해주세요">
                    <button type="submit">비밀번호 전송</button>
                </form>
                <button onclick="showForm('login')">취소</button>
		</div>
	</div>
    <button id="toLogin" onclick="location.reload();">로그인 화면</button>
</div>
