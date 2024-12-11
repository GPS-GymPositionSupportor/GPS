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
}

.exit-button {
	display: block;
	margin: 2%;
	background: url(image/icon_back.png) no-repeat center center;
	width: 24px;
	height: 24px;
	background-size: cover;
}

#profileP {
	font-size: 24px;
	color: #000000;
	margin-left: 2.5rem;
}

#profileContainer, #editMyPageForm {
	display: flex;
	flex-direction: column;
	align-items: center;
}

#profile {
	margin-bottom: 1.5rem;
}

#profileBtn {
	box-shadow: 0px 0px 1px 0px #0000001A,
				0px 1px 1px 0px #00000017,
				0px 3px 2px 0px #0000000D,
				0px 5px 2px 0px #00000003,
				0px 7px 2px 0px #00000000;
	border: initial;
    border-radius: 20px;
    background-color: initial;
    display: grid;
    grid-template-columns: 1fr 2fr;
    grid-template-rows: auto auto;
    justify-items: center;
    align-items: center;
    height: 11.5vh;
    width: 23rem;
    align-content: center;
}

#profileStyle {
	box-shadow: 0px 0px 1px 0px #0000001A,
				0px 1px 1px 0px #00000017,
				0px 3px 2px 0px #0000000D,
				0px 5px 2px 0px #00000003,
				0px 7px 2px 0px #00000000;
	border: initial;
    border-radius: 20px;
    background-color: initial;
    display: flex;
    justify-items: center;
    align-items: center;
    height: 35vh;
    width: 23rem;
    align-content: center;
    justify-content: center;
    flex-direction: column;
}

#editProfilePicture {
	font-size: 16px !important;
	color: #212121 !important;
	background-color: initial !important;
	box-shadow: none !important;
	width: auto !important;
    height: auto !important;
    align-self: start;
    margin-left: 7%;
}

#editMyPageImage {
	min-width: 7rem;
    max-width: 7rem;
    margin: 10% 0%;
}

#editProfileBtn {
	width: 90%;
	display: flex;
}

#changeProfilePicture {
	width: 100%;
	height: 4.5vh;
    padding: 10px;
    background-color: #C79126;
    color: white;
    border: none;
    border-radius: 12px;
    cursor: pointer;
    font-size: 14px;
    box-shadow: 0px 1px 1px 0px #00000017,
			    0px 1px 1px 0px #0000000D,
			    0px 2px 1px 0px #00000003,
			    0px 3px 1px 0px #00000000;
	margin: 0% 9%;
    padding: 0% 7%;
}

#changeProfilePicture:hover {
	background-color: #A02400;
}

#changeProfilePicture:active {
	background-color: #3A1C16;
}

#cancelProfilePicture {
	width: 100%;
	height: 4.5vh;
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
	margin: 0% 9%;
    padding: 0% 7%;
}

#cancelProfilePicture:hover {
	background-color: #B35F00;
}

#cancelProfilePicture:active {
	background-color: #C79126;
}

#myPageImage {
	grid-column: 1;
	grid-row: 1 / span 2;
	min-width: 5rem;
    max-width: 5rem;
}

#profilePicture {
	grid-column: 2;
	grid-row: 1;
	font-size: 12px;
	color: #212121;
	margin-bottom: 0rem;
    justify-self: left;
}

#tapToEdit {
	grid-column: 2;
	grid-row: 2;
	font-size: 14px;
	color: #616161;
	margin-top: 0rem;
    justify-self: left;
}

#profileId {
	box-shadow: 0px 0px 1px 0px #0000001A,
				0px 1px 1px 0px #00000017,
				0px 3px 2px 0px #0000000D,
				0px 5px 2px 0px #00000003,
				0px 7px 2px 0px #00000000;
	border: initial;
    border-radius: 20px;
    background-color: initial;
    width: 23rem;
    height: 11.5vh;
    margin-bottom: 1.5rem;
}

#editProfileId {
	box-shadow: 0px 0px 1px 0px #0000001A,
				0px 1px 1px 0px #00000017,
				0px 3px 2px 0px #0000000D,
				0px 5px 2px 0px #00000003,
				0px 7px 2px 0px #00000000;
	border: initial;
    border-radius: 20px;
    background-color: initial;
    width: 23rem;
    height: 11.5vh;
    margin-bottom: 1.5rem;
}

.pTitle {
	font-size: 12px;
	color: #000;
	margin-left: 2rem;
}

#userId {
	font-size: 16px;
	color: #9E9E9E;
	margin-left: 4.4rem;
}

#profileName {
	box-shadow: 0px 0px 1px 0px #0000001A,
				0px 1px 1px 0px #00000017,
				0px 3px 2px 0px #0000000D,
				0px 5px 2px 0px #00000003,
				0px 7px 2px 0px #00000000;
	border: initial;
    border-radius: 20px;
    background-color: initial;
    width: 23rem;
    height: 11.5vh;
    margin-bottom: 1.5rem;
}

#editProfileName {
	box-shadow: 0px 0px 1px 0px #0000001A,
				0px 1px 1px 0px #00000017,
				0px 3px 2px 0px #0000000D,
				0px 5px 2px 0px #00000003,
				0px 7px 2px 0px #00000000;
	border: initial;
    border-radius: 20px;
    background-color: initial;
    width: 23rem;
    height: 15.5vh;
    margin-bottom: 1.5rem;
}

#userName {
	font-size: 16px;
	color: #212121;
	margin-left: 4.4rem;
}

#userNameInput {
	font-size: 16px;
    color: #9E9E9E;
    border: none;
    border-bottom: 1px solid #9E9E9E;
    margin-left: 2rem;
    margin-top: 2rem;
    margin-right: 7rem;
    width: 25%;
    background-color: initial;
}

#profileNickname {
	box-shadow: 0px 0px 1px 0px #0000001A,
				0px 1px 1px 0px #00000017,
				0px 3px 2px 0px #0000000D,
				0px 5px 2px 0px #00000003,
				0px 7px 2px 0px #00000000;
	border: initial;
    border-radius: 20px;
    background-color: initial;
    width: 23rem;
    height: 11.5vh;
    margin-bottom: 1.5rem;
}

#userNickname {
	font-size: 16px;
	color: #212121;
	margin-left: 4.4rem;
}

#editProfileNickname {
	box-shadow: 0px 0px 1px 0px #0000001A,
				0px 1px 1px 0px #00000017,
				0px 3px 2px 0px #0000000D,
				0px 5px 2px 0px #00000003,
				0px 7px 2px 0px #00000000;
	border: initial;
    border-radius: 20px;
    background-color: initial;
    width: 23rem;
    height: 15.5vh;
    margin-bottom: 1.5rem;
}

#userNicknameInput {
	font-size: 16px;
    color: #9E9E9E;
    border: none;
    border-bottom: 1px solid #9E9E9E;
    margin-left: 2rem;
    margin-top: 2rem;
    margin-right: 7rem;
    width: 25%;
    background-color: initial;
}

#checkNickname {
	width: 26%;
	height: 4.5vh;
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
}

#checkNickname:hover {
	background-color: #B35F00;
}

#checkNickname:active {
	background-color: #C79126;
}

#profileEmail {
	box-shadow: 0px 0px 1px 0px #0000001A,
				0px 1px 1px 0px #00000017,
				0px 3px 2px 0px #0000000D,
				0px 5px 2px 0px #00000003,
				0px 7px 2px 0px #00000000;
	border: initial;
    border-radius: 20px;
    background-color: initial;
    width: 23rem;
    height: 11.5vh;
    margin-bottom: 1.5rem;
}

#userEmail {
	font-size: 16px;
	color: #212121;
	margin-left: 4.4rem;
}

#editProfileEmail {
	box-shadow: 0px 0px 1px 0px #0000001A,
				0px 1px 1px 0px #00000017,
				0px 3px 2px 0px #0000000D,
				0px 5px 2px 0px #00000003,
				0px 7px 2px 0px #00000000;
	border: initial;
    border-radius: 20px;
    background-color: initial;
    width: 23rem;
    height: 15.5vh;
    margin-bottom: 1.5rem;
}

#editIdEmailId, #editIdEmailDomain {
	font-size: 16px;
	color: #9E9E9E;
	border: none;
	border-bottom: 1px solid #9E9E9E;
    margin-left: 2rem;
    margin-top: 2rem;
    width: 30%;
    background-color: initial;
}

#editIdEmailDomain {
	margin-left: 0;
	margin-top: 0;
}

#profileGender {
	box-shadow: 0px 0px 1px 0px #0000001A,
				0px 1px 1px 0px #00000017,
				0px 3px 2px 0px #0000000D,
				0px 5px 2px 0px #00000003,
				0px 7px 2px 0px #00000000;
	border: initial;
    border-radius: 20px;
    background-color: initial;
    width: 23rem;
    height: 11.5vh;
    margin-bottom: 1.5rem;
}

#userGender {
	font-size: 16px;
	color: #212121;
	margin-left: 4.4rem;
}

#editProfileGender {
	box-shadow: 0px 0px 1px 0px #0000001A,
				0px 1px 1px 0px #00000017,
				0px 3px 2px 0px #0000000D,
				0px 5px 2px 0px #00000003,
				0px 7px 2px 0px #00000000;
	border: initial;
    border-radius: 20px;
    background-color: initial;
    width: 23rem;
    height: 12.5vh;
    margin-bottom: 1.5rem;
}

#editProfileGender input {
	background-color: inherit;
    width: 5%;
    box-shadow: none;
    background-color: initial;
}

#gender-form-group {
	display: flex;
	align-items: center;
	margin-top: 2rem;
}

#editProfileGender input[type="radio"] {
    margin-left: 3rem;
    background-color: initial;
}

#changePasswordBtn {
	box-shadow: 0px 0px 1px 0px #0000001A,
				0px 1px 1px 0px #00000017,
				0px 3px 2px 0px #0000000D,
				0px 5px 2px 0px #00000003,
				0px 7px 2px 0px #00000000;
	border: initial;
    border-radius: 20px;
    background-color: initial;
    width: 23rem;
    height: 5vh;
    margin-bottom: 1.5rem;
    font-size: 12px;
    color: #000000;
    text-align: left;
    padding-left: 1.5rem;
}

#save {
	width: 80%;
}

#saveBtn1, #saveBtn2 {
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

#saveBtn1:hover, #saveBtn2:hover {
	background-color: #B35F00; 
}

#saveBtn1:active, #saveBtn2:active {
	background-color: #C79126; 
}

#unsubscribeBtn1, #unsubscribeBtn2 {
	border: none;
    background-color: initial;
    color: #FF3B30;
    cursor: pointer;
    text-decoration: underline;
    margin-bottom: 2rem;
}

#editProfilePicture {
	width: 50%;
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
	height: 2rem;
}

#profileBtn:hover,
#editProfileId:hover,
#editProfileName:hover,
#editProfileNickname:hover,
#editProfileEmail:hover,
#editProfileGender:hover,
#changePasswordBtn:hover,
#profile:hover,
#profileId:hover,
#profileName:hover,
#profileNickname:hover,
#profileEmail:hover,
#profileGender:hover {
	background-color: #EEEEEE;
}

#modalBack, #modalDelete, #modalEdit {
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

#cancelBack, #confirmDelete, #registComplete {
	border: none;
    background-color: initial;
    font-size: 14px;
    color: #bdbdbd;
}

#confirmBack, #cancelDelete, #editComplete {
	border: none;
    background-color: initial;
    font-size: 14px;
}

#modal-buttons {
    display: flex;
    justify-content: flex-end;
    margin-top: 2rem;
}
