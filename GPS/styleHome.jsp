html, body {
    height: 100%;
}

body {
	font-family: Roboto;
    margin: 0;
    padding: 0;
	scrollbar-width: none;
    -ms-overflow-style: none;
    max-width: 800px;
    width: 100%;
    margin: 0 auto !important;
    overflow-x: hidden;
}

#homePage {
	display: flex;
	flex-direction: column;
    align-items: center;
}

#homeDiv {
	background-color: #FFFBF3;
	height: 100%;
}

.picture {
	max-width: 3.5rem;
	border-radius: 30px;
}

#search-container {
	margin: 1rem;
    z-index: 2;
    position: relative;
    margin-top: -2rem;
    margin-bottom: 2rem;
}

#searchInput {
    padding: 0.5rem;
    font-size: 14px;
    border: 1px solid #734937;
    border-radius: 12px;
    width: 18rem;
    background: inherit;
    padding-left: 1rem;
    color: #734937;
}

#searchInput::placeholder {
    color: #734937;
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

#imgBtns {
	display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 1rem;
    justify-items: center;
}

#mrUserName {
	font-size: 14px !important;
	color: #212121;
}

#name {
	font-size: 12px !important;
	color: #757575;
	
}

#gender {
	font-size: 10px !important;
	color: #BDBDBD;
}

#imgBtns p{
	font-size: 16px;
}

#profileBtnBlock {

}

#profileBtn {
	display: flex;
    border-radius: 12px;
    border: none;
    background-color: #EDEDED;
	box-shadow: 0px 0px 1px 0px #0000001A,
				0px 2px 2px 0px #00000017,
				0px 4px 2px 0px #0000000D,
				0px 7px 3px 0px #00000003,
				0px 12px 3px 0px #00000000;
    width: 9rem;
    flex-direction: column;
    padding-bottom: 0.5rem;
    padding-top: 1.5rem;
    align-items: center;
}

#profileBtn:hover {
	background-color: #F5ECD9;
}

#profileBtn:active {
	background-color: #F6E8DF;
}

#findGym {
	display: flex;
    border-radius: 12px;
    border: none;
    background-color: #fff;
    box-shadow: 0px 0px 1px 0px #0000001A, 
    			0px 2px 2px 0px #00000017, 
    			0px 4px 2px 0px #0000000D, 
    			0px 7px 3px 0px #00000003, 
    			0px 11px 3px 0px #00000000;
    width: 12.5rem;
    flex-direction: column;
    padding-bottom: 0.5rem;
    padding-left: 1rem;
    transform: translateX(-1.5rem);
}

#findGym:hover {
	background-color: #E0E0E0;
}

#findGym:active {
	background-color: #BDBDBD;
}

.findGym {
	margin-left: 5rem;
}

#findGymP {
	color: #424242
}

#recommendFeed {
	display: flex;
    border-radius: 12px;
    border: none;
    background-color: #F5ECD9;
    box-shadow: 0px 0px 1px 0px #0000001A, 
    			0px 2px 2px 0px #00000017, 
    			0px 4px 2px 0px #0000000D, 
    			0px 7px 3px 0px #00000003, 
    			0px 11px 3px 0px #00000000;
    width: 12.5rem;
    flex-direction: column;
    padding-bottom: 0.5rem;
    padding-left: 1rem;
    transform: translate(-1.5rem, -3rem);
}

#recommendFeed:hover {
	background-color: #EDEDED;
}

#recommendFeed:active {
	background-color: #9F7E70;
}

#recommendFeedP {
	color: #3A1C16;
}

.recommendFeed {
	margin-left: 4.5rem;
}

#myReview {
	display: flex;
    border-radius: 12px;
    border: none;
    background-color: #402E32;
    box-shadow: 0px 0px 1px 0px #0000001A, 
    			0px 2px 2px 0px #00000017, 
    			0px 4px 2px 0px #0000000D, 
    			0px 7px 3px 0px #00000003, 
    			0px 11px 3px 0px #00000000;
    width: 14rem;
    flex-direction: column;
    padding-bottom: 1rem;
    padding-left: 1rem;
    align-items: flex-end;
    transform: translateY(-0.5rem);
}

#myReview:hover {
	background-color: #734937;
}

#myReview:active {
	background-color: #7F5B5E;
}

#myReviewP {
	color: #DCC6BD;
	padding: 1rem 1rem 0 0;
}

.myReview {
	margin-right: 1rem;
}

#justDoIt {
	transform: translate(-0.5rem, 1rem);
}

#scrap {
	display: flex;
    border-radius: 12px;
    border: none;
    background-color: #D7AF66;
    box-shadow: 0px 0px 1px 0px #0000001A, 
    			0px 2px 2px 0px #00000017, 
    			0px 4px 2px 0px #0000000D, 
    			0px 7px 3px 0px #00000003, 
    			0px 11px 3px 0px #00000000;
    width: 13rem;
    flex-direction: column;
    padding-bottom: 3rem;
    padding-left: 1rem;
    transform: translateY(-2.5rem);
}

#scrap:hover {
	background-color: #E7CFA2;
}

#scrap:active {
	background-color: #BBAFB0;
}

#scrapP {
	color: #3A1C16;
	padding-left: 1rem;
}

.scrap {
	margin-left: 1rem;
}

#logoutButton {
	display: flex;
    align-items: center;
    font-size: 12px;
    border: none;
    background-color: inherit;
}

#logoutContainer {
	display: grid;
    justify-content: end;
    margin-top: 1rem;
}