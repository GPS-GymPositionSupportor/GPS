body {
    font-family: Arial, sans-serif;
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
    background-color: #747474;
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
    color: white;
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
}

.burbutton.active span:nth-of-type(2) {
	opacity: 0;
	transition: all 0.35s;
}

.burbutton.active span:nth-of-type(3) {
	-webkit-transform: translateY(-0.5rem) rotate(45deg);
	transform: translateY(-0.55rem) rotate(45deg);
	transition: all 0.35s;
}

.login-overlay {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background-color: rgba(0, 0, 0, 0.5); /* 반투명 배경 */
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 3; /* 로그인 폼을 메인 콘텐츠 위에 배치 */
            display: none; /* 기본적으로 숨김 */
        }

        .login-form {
            background-color: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
        }

        .login-form input {
            display: block;
            margin: 10px 0;
            padding: 10px;
            width: 100%;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        .login-form button {
            padding: 10px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .login-form button:hover {
            background-color: #0056b3;
        }
        
        .social-buttons {
            display: flex;
            justify-content: space-between;
            margin-top: 10px;
        }

        .social-buttons button {
            flex: 1;
            margin: 5px;
            padding: 10px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .kakao-btn {
            background-color: #FEE500; /* 카카오톡 색상 */
            color: black;
        }

        .google-btn {
            background-color: #DB4437; /* 구글 색상 */
            color: white;
        }

        .register-btn {
            margin-top: 10px;
            background-color: #28a745; /* 회원가입 버튼 색상 */
            color: white;
        }
        
@media only screen and (max-width: 767px) {

    .burbutton {
        display:block;
        z-index: 10;
    }

	.burbutton.active {
		z-index: 10;
	}
	
	#nav-links {
		position: fixed;
	    right: 0;
	    top: 0;
	    width: 70%;
		height: 100%;
	    background-color: #5d5d5d;
	    transform: translateX(100%);
		transition: transform .3s ease-in-out;
		z-index: 1;
    	padding-top: 30%;
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
	}
	
	.navbar button,
	.navbar label {
		border-bottom: 1px solid #ffffff;
		padding-bottom: 1rem;
		padding-top: 1rem;
		font-size: 16px;
		font-family: 'NanumSquare', sans-serif;
		z-index: 3;
	}