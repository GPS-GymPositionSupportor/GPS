body {
    font-family: Roboto;
    margin: 0;
    padding: 0;
    background-color: #F5F5F5;
    scrollbar-width: none;
    -ms-overflow-style: none;
    max-width: 800px;
    width: 100%;
    margin: 0 auto !important;
    overflow-x: hidden;
    max-height: 90%
}

body::-webkit-scrollbar {
    display: none;
}

.navbar {
    background-color: #402E32;
    display: flex;
    justify-content: space-around;
    align-items: center;
    padding: 1rem 0 0 0;
    width: 100%;
    position: -webkit-sticky;
    position: sticky;
    top: 0;
    z-index: 10;
}

.navbar button,
.navbar label {
    background-color: transparent;
    border: none;
    color: black;
    cursor: pointer;
    font-size: 14px;
    padding: 0.5rem 1rem;
    
    margin: 0 0.5rem;
    transition: all 0.3s;
    text-decoration: none;
    outline: none;
}

.navbar button:hover,
.navbar button:focus,
.navbar label:hover,
.navbar label:focus {
    color: #222;
}

#search-container {
	margin: 1rem;
    z-index: 2;
    position: relative;
}

#searchInput {
    padding: 0.5rem;
    font-size: 14px;
    border: 1px solid #fff;
    border-radius: 12px;
    width: 18rem;
    background: inherit;
    padding-left: 1rem;
    color: #fff;
}

#searchInput::placeholder {
    color: #fff;
}

.search-icon {
	position: absolute;
    right: 1rem;
    top: 55%;
    transform: translateY(-50%);
    cursor: pointer;
}

.search-icon img {
    width: 20px;
    height: auto;
}

.content-section {
    margin: 1rem;
}

.burbutton {
  position: absolute;
  left: 25px;
  cursor: pointer;
  display: none;
}

.burbutton span {
  display: block;
  background-color: #fff;
  height: 4px;
  width: 30px;
  margin-bottom: 5px; 
  border-radius: 2px;
  transition: all 0.35s;
}

.burbutton span.middle {
  width: 30px;
}

#nav-links {
   transition: transform .3s ease-in-out; 
   transform : translateX(0); 
}

#nav-links.active {
    transform: translateX(0%); 
}

.burbutton.active span:nth-of-type(1) {
	-webkit-transform: translateY (0.6rem) rotate (-45deg);
	transform: translateY(0.6rem) rotate(-45deg);
	transition: all 0.35s;
	background-color: #999999;
	
}

.burbutton.active span:nth-of-type(2) {
	opacity: 0;
	transition: all 0.35s;
	background-color: #999999;
}

.burbutton.active span:nth-of-type(3) {
	-webkit-transform: translateY(-0.5rem) rotate(45deg);
	transform: translateY(-0.55rem) rotate(45deg);
	transition: all 0.35s;
	background-color: #999999;
}

#error-message {
	 color: #FF3B30;
	 text-align: center;
	 font-size: 12px;
	 height: 2rem;
}
.login-overlay {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background-color: rgba(255, 255, 255, 0.73);
	display: flex;
	justify-content: center;
	align-items: center;
	z-index: 3;
	display: none;
}
        
.images {
	display: flex;
    flex-direction: column;
    align-items: center;
}

.images img {
	margin-bottom: 5%;
}

.login-form {
	width: 80%;
	align-items: center;
}
		
.login-form input {
	display: block;
	margin: 10px 0px 10px 0px;
	padding: 10px 15px 10px 15px;
	width: 100%;
	background-color: #f2f2f2;
	border: 0px solid;
	border-radius: 12px 12px 0 0;
	font-size: 16px;
	box-shadow: 0 1px 0px #999999;
	box-sizing: border-box;
}

.login-form button {
	width: 100%;
	padding: 10px;
	background-color: #402E32;
	color: white;
	border: none;
	border-radius: 12px;
	cursor: pointer;
	font-size: 16px;
	margin: 10px 0px 10px 0px;
	box-shadow: 0px 1px 1px 0px #00000017,
	0px 1px 1px 0px #0000000D,
	0px 2px 1px 0px #00000003,
	0px 3px 1px 0px #00000000;
}

.login-form input::placeholder {
    color: #999 !important;
}

.login-form input.error {
    color: #FF3B30 !important;
    box-shadow: 0px 1px 1px 0px #FF3B30 !important;
}

.login-form input.error::placeholder {
	color: #FF3B30 !important;
}

.password-container {
	position: relative;
    display: flex;
    align-items: center;
}

#togglePassword {
	box-shadow: none !important;
}

.eye-btn {
	position: absolute;
    width: 3rem !important;
    right: 0.11rem;
    background: none;
    background-color: #00000000 !important;
}

.find-regist-btn {
	display: flex;
    justify-content: space-between;
    flex-direction: row;
    align-items: center;
    margin: -1rem -1rem 0% -2rem;
}

.findIdPw-btn {
	width: 50% !important;
	color: #555 !important;
	font-size: 12px !important;
	background-color: #ffffff00 !important;
	box-shadow: none !important;
	text-decoration: underline;
	cursor: pointer;
}

.findIdPw-btn:hover, .register-btn:hover {
	color: #A02400 !important;
}

.findIdPw-btn:active, .register-btn:active {
	color: #3A1C16 !important;
}

#toLogin {
	color: #555 !important;
    font-size: 12px !important;
    background-color: #ffffff00 !important;
    box-shadow: none !important;
    position: absolute;
    bottom: 1rem;
    left: 0rem;
}

.register-btn {
	width: 25% !important;
	color: #555 !important;
	font-size: 12px !important;
	background-color: #ffffff00 !important;
	box-shadow: none !important;
	text-decoration: underline;
	cursor: pointer;
}

.kakao-btn {
	background-color: transparent !important;
    color: inherit !important;
	width: auto !important;
	align-self: center;
	box-shadow: none !important;
	padding: 0 !important;
    border-radius: 5px !important;
}

.google-btn {
	background-color: transparent !important;
    color: inherit !important;
	width: auto !important;
	align-self: center;
	box-shadow: none !important;
	padding: 0 !important;
    border-radius: 5px !important;
}

.login-form button:hover {
	background-color: #B35F00;
}

.login-form button:active {
	background-color: #C79126;
}

.social-buttons {
	display: flex;
	justify-content: space-between;
	margin-top: 10px;
	flex-direction: column;
}

.social-buttons button {
	flex: 1;
	display: inline-flex;
    justify-content: center;
    align-items: center;
    height: auto;
}

.sns {
	display: grid;
	text-align: center;
	font-size: 12px;
	font-weight: 400;
}

.divideLine {
	width: 50%;
	border: 1px solid #CCCCCC;
}

#cancelBtn {
	position: absolute;
    top: 2rem;
    left: 1.5rem;
    width: auto !important;
    padding: 0 !important;
    background-color: transparent !important;
    color: inherit !important;
    border: none !important;
    border-radius: 0 !important;
    cursor: pointer !important;
    font-size: inherit !important;
    margin: 0 !important;
    box-shadow: none !important;
}

.picture {
	max-width: 5rem;
	border-radius: 30px;
	width: 5rem;
}

.form-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 100%;
}

.form-container p {
    text-align: center;
}

#findIdForm2, #findPwForm2 {
	display: contents;
}

#findIdF, #findPwF{
	margin: 5% 0% 15% 0%;
}

#plzEmailId, #plzEmailPw {
	color: #616161;
}

#plzEmailId.error, #plzEmailPw.error {
	color: #FF3B30 !important;
}

#regist-email {
	display: flex;
    align-items: center;
    margin: 0 auto;
    width: 70%;
}

#findIdEmailDomain, #registerEmailDomain {
	border: none;
    outline: none;
    flex: 1;
    padding: 0;
    margin: 0;
    box-shadow: none;
    background-color: inherit;
    border-bottom: 1px solid #616161;
}

.email-input {
    display: flex;
    align-items: center;
    margin-bottom: 20%;
    width: 70%;
}

.email-input input, #regist-email input {
    border: none;
    outline: none;
    flex: 1;
    padding: 0 !important;
    margin: 0 !important;
    box-shadow: none !important;
    background-color: inherit;
    border-bottom: 1px solid #616161 !important;
}

.email-input span, #regist-email span {
    font-size: 14px;
}

#findCId, #findCPw {
	display: flex;
	flex-direction: column;
	align-items: center;
}

#findIdP, #findPwP {
	font-size: 24px;
	margin: 4.5rem 2rem 5rem 2rem;
	font-weight: 400;
	line-height: 17px;
	text-align: left;
}

#sendMail {
	text-align: center;
	color: #616161;
	font-size: 16px;
	margin: 2rem;
}

#certificationCodeId, #certificationCodePw {
	display: grid;
    width: 100%;
    justify-content: center;
}

#certificationCodeIdForm, #certificationCodePwForm {
	display: flex;
    flex-wrap: wrap;
    align-content: center;
    justify-content: flex-end;
}

#certificationCodeIdForm input, #certificationCodePwForm input {
	display: block;
	margin: 10px 0px 10px 0px;
	padding: 10px 15px 10px 15px;
	width: 100%;
	background-color: #f2f2f2;
	border: 0px solid;
	border-radius: 12px 12px 0 0;
	font-size: 16px;
	box-shadow: 0 1px 0px #999999;
	box-sizing: border-box;
	height: 3.5rem;
}

#certificationCodeIdForm button, #certificationCodePwForm button {
	width: 32%;
	padding: 10px;
	background-color: #402E32;
	color: white;
	border: none;
	border-radius: 12px;
	cursor: pointer;
	font-size: 16px;
	margin: 10px 0px 10px 0px;
	box-shadow: 0px 1px 1px 0px #00000017,
	0px 1px 1px 0px #0000000D,
	0px 2px 1px 0px #00000003,
	0px 3px 1px 0px #00000000;
}

#toLoginFindId, #toLoginFindPw {
	color: #555 !important;
    font-size: 12px !important;
    background-color: #ffffff00;
    position: absolute;
    bottom: 2rem;
    border-color: initial;
    border: none;
    text-decoration: underline;
}

#cCodeError {
	color: #FF3B30;
    text-align: center;
    font-size: 12px;
}

#certificationCodeIdForm input.error, #certificationCodePwForm input.error {
    color: #FF3B30 !important;
    box-shadow: 0px 1px 1px 0px #FF3B30 !important;
}

#certificationCodeIdForm input.error::placeholder,
#certificationCodePwForm input.error::placeholder {
	color: #FF3B30 !important;
}

#certificationCodeIdForm input::placeholder,
#certificationCodePwForm input::placeholder {
    color: #ccc !important;
}

#findId-btn {
	background-color: #B35F00 !important;
}

#findId-btn:hover {
	background-color: #D7AF66 !important;
}

#findId-btn:active {
	background-color: #9F7E70 !important;
}

#inputCCode:hover {
	background-color: #B35F00;
}

#inputCCode:active {
	background-color: #C79126;
}

#toFindPw {
	width: 80%;
	height: 3.5rem;
	padding: 10px;
	background-color: #402E32;
	color: white;
	border: none;
	border-radius: 12px;
	cursor: pointer;
	font-size: 16px;
	margin: 10px 0px 10px 0px;
	box-shadow: 0px 1px 1px 0px #00000017,
	0px 1px 1px 0px #0000000D,
	0px 2px 1px 0px #00000003,
	0px 3px 1px 0px #00000000;
	bottom: 5rem;
    position: absolute;
}

#toFindPw:hover {
	background-color: #B35F00; 
}

#toFindPw:active {
	background-color: #C79126;
}

#findIdComplete, #findPwComplete {
	display: grid;
    justify-items: center;
    text-align: center;
    margin: 10rem 0rem 0rem 0rem;
}

#error-message-id, #error-message-pw, #error-message-nickname, #error-message-email {
	 color: #FF3B30;
	 text-align: center;
	 font-size: 12px;
}

#duplicationCheck {
	width: 30%;
	padding: 0.8rem;
	background-color: #402E32;
	color: white;
	border: none;
	border-radius: 12px;
	cursor: pointer;
	font-size: 14px;
	margin: 2% 0% 2% 0%;
}

#duplicationCheck:hover, #duplicationCheckEmail:hover {
	background-color: #B35F00;
}

#duplicationCheck:active, #duplicationCheckEmail:active {
	background-color: #C79126;
}

#duplicationCheckEmail {
	width: 30%;
	padding: 0.8rem;
	background-color: #402E32;
	color: white;
	border: none;
	border-radius: 12px;
	cursor: pointer;
	font-size: 14px;
	margin: 2% 0% 2% 0%;
}

#id-form-group, #nickname-form-group {
	display: flex;
    flex-direction: column;
    align-items: flex-end;
    align-content: space-between;
}

#password {
	margin: 1rem 0 1rem 0;
}

#confirmPassword {
	margin: 0 0 2rem 0;
}

#togglePasswordRegist {
	box-shadow: none !important;
	margin: 0 0 2rem 0;
}

#email-form-group {
	display: flex;
}

#birthdate-form-group {
    display: flex;
    align-items: center;
}

#birthYear, #birthMonth, #birthDay {
	border: none;
    outline: none;
    padding: 0;
    margin: 0;
    box-shadow: none;
    background-color: inherit;
    border-bottom: 1px solid #616161;
}

.birthP {
	margin-right: 1rem;
}

#privacy {
    box-shadow: none;
    width: 2rem;
    height: 1rem;
}

#privacyLabel {
	font-size: 12px;
	color: #555555;
}

#checkbox {
	display: flex;
	align-items: center;
}

#birthYear {
	width: 20%;
}

#birthMonth, #birthDay {
	width: 14%;
}

#gender input {
	background-color: inherit;
    width: 5%;
    box-shadow: none;
}

#gender-form-group {
	display: flex;
	align-items: center;
}

#gender input[type="radio"] {
    margin-left: 3rem;
}

#registBtn {
	height: 3.5rem;
}

#modalBack {
    display: none;
    position: fixed;
    z-index: 1000;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    overflow: auto;
    background-color: rgba(0,0,0,0.4);
}

#modal-content {
    background-color: #fff;
    margin: 15% auto;
    padding: 1rem;
    width: 80%;
    border-radius: 0.5rem;
}

#modal-content p {
	font-size: 14px;
	color: #757575;
}

#cancelBack {
	border: none;
    background-color: initial;
    font-size: 14px;
    color: #bdbdbd;
}

#confirmBack {
	border: none;
    background-color: initial;
    font-size: 14px;
}

#modal-buttons {
    display: flex;
    justify-content: flex-end;
    margin-top: 2rem;
}

@media only screen and (max-width: 767px) {
	body {
		max-height: 100%;
		height: 100%;
	}
	
    .burbutton {
        display:block;
        z-index: 10;
    }

	.burbutton.active {
		z-index: 10;
	}
	
	.navbar {
	    background-color: #402E32;
	    display: flex;
	    justify-content: space-around;
	    align-items: center;
	    width: 100%;
	    position: -webkit-sticky;
	    position: sticky;
	    top: 0;
	    z-index: 10;
	    display: flex;
        justify-content: flex-end;
	}

	#nav-links {
		position: fixed;
	    left: 0;
	    top: 0;
	    width: 70%;
		height: 100%;
	    background-color: #ffffff;
	    transform: translateX(-100%);
		transition: transform .3s ease-in-out;
		z-index: 1;
    	padding-top: 2rem;
    	opacity: 95%;
		
	}
	
	#nav-links.active {
		transform : translateX(0);
		display:block;
		z-index: 1;
		
	}
	
	#navForm {
		display:flex !important; 
		flex-direction: column;
		align-items: flex-start;
	}
	
	.navbar button,
	.navbar label {
		border-bottom: 1px solid #ffffff;
		padding-bottom: 1rem;
		padding-top: 1rem;
		font-size: 16px;
		font-family: Roboto;
		z-index: 3;
	}
	
	.images {
		display: flex;
	    flex-direction: column;
	    align-items: center;
	}
	
	.images img {
		margin-bottom: 10%;
	}
	
	.login-form {
		width: 80%;
		height: 40rem;
	}
	
	.login-form input {
		display: block;
		margin: 5% 0% 5% 0%;
		padding: 5% 5% 5% 5%;
		border: 0px solid;
		border-radius: 12px 12px 0 0;
	}
	
	.login-form button {
		width: 100%;
		padding: 0.8rem;
		background-color: #402E32;
		color: white;
		border: none;
		border-radius: 12px;
		cursor: pointer;
		font-size: 14px;
		margin: 2% 0% 2% 0%;
	}
	
	#togglePassword {
		box-shadow: none !important;
	}

	.exit-button {
		display: block;
		margin: 2%;
		background: url('image/icon_back.png') no-repeat center center; /* 이미지 경로 */
		width: 24px;
		height: 24px;
		background-size: cover;
	}
	
	.register-form {
		display: flex;
		justify-content: center; /* 수평 중앙 정렬 */
		align-items: center; /* 수직 중앙 정렬 */
		flex-direction: column;
	}
	
	.regist {
		font-size: 24px;
		font-weight: 400;
		line-height: 17px;
	}
	
	#logoutButton {
		display: flex;
		align-items: center;
		font-size: 12px;
	}
	
	.myPage {
    	display: grid;
	}
	
	.user-info {
	    display: flex;
	    align-items: center;
	}
	
	.greeting {
	    margin-left: 1rem;
	}
	
	.hello {
		font-size: 16px;
	}
	
	.mrUser {
		margin-left: 1rem;
		margin-top: 0.5rem;
	}
	
	.mrUserName {
		font-family: 'Roboto';
		font-style: normal;
		font-weight: 500;
		font-size: 20px;
		line-height: 23px;
	}
	
	.mr {
		font-size: 14px;
	}
	
	#logoutContainer {
		position: absolute;
	    bottom: 3rem;
	    right: -0.5rem;
	}