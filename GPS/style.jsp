body {
    font-family: Roboto;
    margin: 0;
    padding: 0;
    background-color: #F5F5F5;
    scrollbar-width: none;
    -ms-overflow-style: none;
}

body::-webkit-scrollbar {
    display: none;
}

.navbar {
    background-color: #402E32;
    display: flex;
    justify-content: space-around;
    align-items: center;
    padding: 1rem 0;
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
  width: 20px;
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
	background-color: #0056b3;
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

#findIdForm2 {
	display: contents;
	
}

#findIdF {
	margin: 5% 0% 15% 0%;
}

#PlzEmail {
	color: #616161;
}

.email-input {
    display: flex;
    align-items: center;
    margin-bottom: 20%;
    width: 70%;
}

.email-input input {
    border: none;
    outline: none;
    flex: 1;
    padding: 0 !important;
    margin: 0 !important;
    box-shadow: none !important;
    background-color: inherit;
    border-bottom: 1px solid #616161 !important;
}

.email-input span {
    font-size: 14px;
}

@media only screen and (max-width: 767px) {

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