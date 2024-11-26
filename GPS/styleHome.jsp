body {
	font-family: Roboto;
    margin: 0;
    padding: 0;
    background-color: #FFFBF3;
	scrollbar-width: none;
    -ms-overflow-style: none;
    max-width: 1000px;
    width: 100%;
    margin: 0 auto !important;
    overflow-x: hidden;
    max-height: 90%
}

body::-webkit-scrollbar {
    display: none;
}

#homePage {
	display: flex;
	flex-direction: column;
    align-items: center;
}

.picture {
	width:
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
	position: absolute;
    left: 1rem;
    z-index: 2;
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
}

.findGym {
	margin-left: 5rem;
}

#findGymP {
	color: #424242
}

#findGymBlock {
	position: absolute;
    right: 1rem;
}

#justDoItBlock {
	position: absolute;
    left: 2rem;
    top: 27rem;
}

#recommendFeedBlock {
	position: absolute;
    right: 1rem;
    top: 23.3rem;
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
}

#recommendFeedP {
	color: #3A1C16;
}

.recommendFeed {
	margin-left: 4.5rem;
}

#myReviewBlock {
	position: absolute;
	left: -1rem;
	top: 36rem;
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
}

#myReviewP {
	color: #DCC6BD;
	padding: 1rem 1rem 0 0;
}

.myReview {
	margin-right: 1rem;
}

#scrapBlock {
	position: absolute;
    right: -2rem;
    top: 34.5rem;
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
	position: absolute;
	bottom: 3rem;
	right: 0.5rem;
}