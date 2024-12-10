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
	background: url(image/icon_back.png) no-repeat center center;
	width: 24px;
	height: 24px;
	background-size: cover;
}

#profileP {
	font-size: 24px;
	color: #000000;
}

#reviewContainer {
	width: 90%;
	margin-bottom: 2rem;
}

#review {
	display: flex;
    flex-direction: column;
    align-items: center;
}

#reviewP {
	font-size: 24px;
}

#review-item {
	display: grid;
    grid-template-columns: 0fr 2fr 4rem;
    grid-template-rows: 1fr 1fr;
}

#editReviewImage {
    min-width: 7rem;
    max-width: 7rem;
    grid-column: 1;
    grid-row: 1 / span 2;
    border-radius: 8px;
    align-self: center;
}

#gymName {
    color: #000;
    font-size: 16px;
    grid-column: 2;
    grid-row: 1;
    align-self: start;
}

#gymAddress {
	grid-column: 2;
    grid-row: 2;
    align-self: flex-start;
    font-size: 14px;
    color: #3A1C16;
}

#bookMark {
	grid-column: 4;
    grid-row: 1;
    height: 2rem;
    border: initial;
    background-color: initial;
}

#gymReadMore {
	grid-column: 3 / span4;
    grid-row: 3;
    border: initial;
    background-color: initial;
    font-size: 12px;
    color: #9E9E9E;
    cursor: pointer;
}