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

.register-btn {
	width: 25% !important;
	color: #555 !important;
	font-size: 12px !important;
	background-color: #ffffff00 !important;
	box-shadow: none !important;
}
.register-button {
	display: flex;
	justify-content: center;
}
.kakao-btn {
	background-color: #FEE500 !important; /* 카카오톡 색상 */
	color: #000!important;
	width: 60% !important;
	align-self: center;
	border-radius: 6px !important;
	box-shadow: 0 1px 0px #00000040 !important;
}

.google-btn {
	background-color: #fff !important; /* 구글 색상 */
	color: #000 !important;
	width: 60% !important;
	align-self: center;
	border-radius: 2px !important;
	box-shadow: 0 1px 0px #00000040 !important;
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
	margin: 5px;
	padding: 10px;
	border: none;
	border-radius: 5px;
	cursor: pointer;
}
        
.sns {
	display: grid;
	text-align: center;
	font-size: 12px;
	font-weight: 400;
}

.divideLine {
	width: 80%;
	border: 1px solid #CCCCCC;
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
	    background-color: #747474;
	    display: flex;
	    justify-content: space-around;
	    align-items: center;
	    padding: 0;
	    width: 100%;
	    position: -webkit-sticky;
	    position: sticky;
	    top: 0;
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
	
	.images {
		display: flex;
	    flex-direction: column;
	    align-items: center;
	    margin-bottom: 5%;
	    padding: 0% 0% 10% 0%;
	}
	
	.images img {
		margin-bottom: 10%;
	}
	
	.login-form {
		width: 80%;
		height: 80%;
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
		padding: 10px;
		background-color: #402E32;
		color: white;
		border: none;
		border-radius: 12px;
		cursor: pointer;
		font-size: 14px;
		margin: 2% 0% 2% 0%;
	}