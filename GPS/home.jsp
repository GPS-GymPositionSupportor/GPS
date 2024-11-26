<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
	
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8">
    <title>Home</title>
	<style>
		<%@ include file="styleHome.jsp" %>
	</style>
</head>

<body>
	<div id="homePage">
		<img src="image/homeLogo.svg" alt="logo" title="logo" class="picture">
		
		<div id="search-container">
	    <input type="text" id="searchInput" placeholder="찾으시는 운동시설을 검색해주세요">
	    <span id="searchIcon" class="search-icon">
        	<img src="image/icon_search2.svg" alt="검색" />
		</span>
		</div>
	</div>
	<button onclick="location.href='index.jsp'"> index.jsp </button>
	
	<script>
		document.getElementById('searchIcon').addEventListener('click', function() {
		    var searchQuery = document.getElementById('searchInput').value;
		    if (searchQuery) {
		        console.log('검색어:', searchQuery);
		        // 검색 결과를 표시하거나 다른 페이지로 이동할 수 있습니다.
		        // 예: window.location.href = '/searchResults.jsp?query=' + encodeURIComponent(searchQuery);
		    } else {
		        alert('검색어를 입력하세요.');
		    }
		});
	</script>
</body>
</html>