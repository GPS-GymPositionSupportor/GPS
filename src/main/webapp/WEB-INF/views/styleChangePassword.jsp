body {
	font-family: Roboto;
    margin: 0;
    padding: 0;
    background-color: #FFF;
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

.exit-button {
	display: block;
	margin: 2%;
	background: url(../image/icon_back.png) no-repeat center center;
	width: 24px;
	height: 24px;
	background-size: cover;
}

#changeP {
	font-size: 24px;
	color: #000000;
	margin-left: 2.5rem;
}

#passwordForm, #ChangePasswordForm {
	display: flex;
    flex-direction: column;
    margin-top: 3rem;
    align-self: center;
}

.password-container {
	position: relative;
    display: flex;
    align-items: center;
    margin: 0% 9%;
}

.password-container input {
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
	height: 6vh;
}

.password-container input.error {
	color: #FF3B30 !important;
    box-shadow: 0px 1px 1px 0px #FF3B30 !important;
}

.password-container input.error::placeholder {
	color: #FF3B30
}

.eye-btn {
	position: absolute;
    width: 3rem ;
    right: 0.11rem;
    background: none;
    background-color: #00000000 !important;
    border: none;
}

#inputPassword {
	margin: 0% 9.5%;
}

#next, #change {
	width: 80%;
	position: absolute;
    bottom: 1rem;
    align-self: center;
}

#nextBtn, #changeBtn {
	width: 100%;
	padding: 10px;
	background-color: #402E32;
	color: white;
	border: none;
	border-radius: 12px;
	cursor: pointer;
	font-size: 14px;
	box-shadow: 0px 1px 1px 0px #00000017,
	0px 1px 1px 0px #0000000D,
	0px 2px 1px 0px #00000003,
	0px 3px 1px 0px #00000000;
	height: 3.5rem;
	margin-bottom: 2rem;
}

#nextBtn:hover, #changeBtn:hover {
	background-color: #B35F00; 
}

#nextBtn:active, #changeBtn:active {
	background-color: #C79126; 
}

#checkPassword, #error-message-pw {
	 color: #FF3B30;
	 font-size: 12px;
	 margin-left: 2.3rem;
}

#modalBack, #modalEdit {
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

#cancelBack, #registComplete {
	border: none;
    background-color: initial;
    font-size: 14px;
    color: #bdbdbd;
}

#confirmBack, #editComplete {
	border: none;
    background-color: initial;
    font-size: 14px;
}

#modal-buttons {
    display: flex;
    justify-content: flex-end;
    margin-top: 2rem;
}
