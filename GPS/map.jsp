<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>map</title>
    <style>
        /* 전체 화면을 채우도록 CSS 설정 */
        html, body {
            height: 100%;
            margin: 0;
            padding: 0;
        }
        #map {
            width: 100%; /* 너비를 100%로 설정 */
            height: 100vh; /* 높이를 100vh로 설정하여 화면을 가득 채움 */
            touch-action: pan-x pan-y;
        }
    </style>
</head>
<body>
<div id="map">
	<div>
		
    </div>
</div>

<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=f419f845f436bdd78e7684080e263b3c&libraries=services"></script>

<script>
	var container = document.getElementById('map');
	var options = {
	    center: new kakao.maps.LatLng(37.46369169, 126.6502972), // 지도 중심 좌표
	    level: 4
	};
	
	var map = new kakao.maps.Map(container, options); // 지도 생성
	
	// 사용자 위치 마커 설정
	var userImageSrc = 'image/userLocate.svg';
	var userImageSize = new kakao.maps.Size(32, 34);
	var userImageOption = { offset: new kakao.maps.Point(27, 69) };
	var userMarkerImage = new kakao.maps.MarkerImage(userImageSrc, userImageSize, userImageOption);
	var userMarker = new kakao.maps.Marker({
	    map: map,
	    position: options.center,
	    image: userMarkerImage
	});
	
	var locPosition;
	
	function startTracking() {
	    if (navigator.geolocation) {
	        navigator.geolocation.watchPosition(function(position) {
	            var lat = position.coords.latitude;
	            var lon = position.coords.longitude;
	            locPosition = new kakao.maps.LatLng(lat, lon);
	
	            userMarker.setPosition(locPosition);
	            map.setCenter(locPosition);
	            fetchGyms(lat, lon);
	        }, showError);
	    } else {
	        alert("이 브라우저는 Geolocation을 지원하지 않습니다.");
	    }
	}
	
	function showError(error) {
	    var errorTypes = {
	        0: "알 수 없는 에러가 발생했습니다.",
	        1: "위치 정보를 제공하지 않았습니다.",
	        2: "위치 정보를 사용할 수 없습니다.",
	        3: "위치 정보를 가져오는 데 시간이 초과되었습니다."
	    };
	    var errorMsg = errorTypes[error.code];
	    alert("에러 발생: " + errorMsg);
	}
	
	function moveToCurrentLocation() {
	    if (locPosition) {
	        map.setCenter(locPosition);
	    } else {
	        alert("현재 위치를 가져올 수 없습니다.");
	    }
	}
	
	// 확대/축소 버튼 추가
	var zoomControlDiv = document.createElement('div');
	zoomControlDiv.className = 'custom_zoomcontrol';
	zoomControlDiv.innerHTML = `
	    <span onclick="zoomIn()"><img src="image/zoom_in_icon.png" alt="확대"></span>
	    <span onclick="zoomOut()"><img src="image/zoom_out_icon.png" alt="축소"></span>
	`;
	container.appendChild(zoomControlDiv);
	
	function zoomIn() {
	    map.setLevel(map.getLevel() - 1);
	}
	
	function zoomOut() {
	    map.setLevel(map.getLevel() + 1);
	}
	
	// 현위치 버튼 추가
	var locationControl = document.createElement('div');
	locationControl.className = 'location-btn';
	locationControl.innerHTML = '<img src="image/location_search_button.svg" alt="현위치">';
	locationControl.onclick = moveToCurrentLocation;
	container.appendChild(locationControl);
	
	function fetchGyms(userLat, userLng) {
	    const xhr = new XMLHttpRequest();
	    xhr.open("GET", "fetchGyms.jsp?lat=" + userLat + "&lng=" + userLng, true);
	    xhr.onload = function() {
	        if (xhr.status === 200) {
	            const gyms = JSON.parse(xhr.responseText);
	            gyms.forEach(displayMarker);
	        }
	    };
	    xhr.send();
	}
	
	// 지도에 현위치 버튼 추가
	map.addControl(locationControl, kakao.maps.ControlPosition.RIGHT); // 버튼 위치 조정	
	
	// 지도 확대 축소를 제어할 수 있는  줌 컨트롤을 생성합니다
	var zoomControl = new kakao.maps.ZoomControl();
	map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);
	
	// 지도 확대, 축소 컨트롤에서 확대 버튼을 누르면 호출되어 지도를 확대하는 함수입니다
	function zoomIn() {
	    map.setLevel(map.getLevel() - 1);
	}
	
	// 지도 확대, 축소 컨트롤에서 축소 버튼을 누르면 호출되어 지도를 확대하는 함수입니다
	function zoomOut() {
	    map.setLevel(map.getLevel() + 1);
	}
	
	function displayMarker(gym) {
	    var markerPosition = new kakao.maps.LatLng(gym.g_latitude, gym.g_longitude);
	    var gymMarkerImage = new kakao.maps.MarkerImage('image/gymMark.svg', new kakao.maps.Size(32, 34), { offset: new kakao.maps.Point(27, 69) });
	    var marker = new kakao.maps.Marker({
	        map: map,
	        position: markerPosition,
	        image: gymMarkerImage
	    });
	
	    var iwContent = '<div style="padding:5px;">' + gym.g_name + 
        '<br><a href="https://map.kakao.com/link/map/' + gym.g_name + ',' + gym.g_latitude + ',' + gym.g_longitude + '" style="color:blue" target="_blank">큰지도보기</a> ' +
        '<a href="https://map.kakao.com/link/to/' + gym.g_name + ',' + gym.g_latitude + ',' + gym.g_longitude + '" style="color:blue" target="_blank">길찾기</a></div>';
	
	    var infowindow = new kakao.maps.InfoWindow({ content: iwContent });
	    kakao.maps.event.addListener(marker, 'click', function() {
	        infowindow.open(map, marker);
	    });
	    
	 // 지도 클릭 시 인포윈도우 닫기
	    kakao.maps.event.addListener(map, 'click', function() {
	        infowindow.close();
	    	});
	}

	window.onload = function() {
	    map.relayout();
	};
	
	startTracking();
</script>

</body>
</html>