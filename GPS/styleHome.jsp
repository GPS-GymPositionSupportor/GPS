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