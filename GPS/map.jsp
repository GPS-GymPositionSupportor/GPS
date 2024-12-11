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
        }
    </style>
</head>
<body>
<div id="map">
	<div>
		
		<div class="custom_zoomcontrol radius_border"> 
	        <span onclick="zoomIn()"><img src="" alt="확대"></span>  
	        <span onclick="zoomOut()"><img src="" alt="축소"></span>
	    </div>
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

 // 마커 초기화
	var userImageSrc = 'image/userLocate.svg'; // 사용자 위치 마커 이미지 URL
	var userImageSize = new kakao.maps.Size(32, 34); // 사용자 위치 마커 이미지 크기
	var userImageOption = { offset: new kakao.maps.Point(27, 69) }; // 사용자 위치 마커 이미지 옵션
	
	var userMarkerImage = new kakao.maps.MarkerImage(userImageSrc, userImageSize, userImageOption);
	var userMarker = new kakao.maps.Marker({
	    map: map,
	    position: options.center,
	    image: userMarkerImage // 사용자 위치 마커 이미지 설정
	});
   
   var locPosition;
   
    // 실시간 위치 추적 시작
    function startTracking() {
        if (navigator.geolocation) {
            navigator.geolocation.watchPosition(function(position) {
                var lat = position.coords.latitude;
                var lon = position.coords.longitude;
                locPosition = new kakao.maps.LatLng(lat, lon);

                // 마커 위치 업데이트
                userMarker.setPosition(locPosition);

                // 지도 중심을 현재 위치로 이동
                map.setCenter(locPosition);
            }, showError, {
                enableHighAccuracy: true,
                maximumAge: 0,
                timeout: 5000
            });
        } else {
            alert("이 브라우저는 Geolocation을 지원하지 않습니다.");
        }
    }

    // 오류 처리 함수
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
    
 	// 현재 위치로 이동하는 함수
    function moveToCurrentLocation() {
        if (locPosition) {
            map.setCenter(locPosition); // 현재 위치로 지도 중심 이동
        } else {
            alert("현재 위치를 가져올 수 없습니다.");
        }
    }

    // 실시간 위치 추적 시작
    startTracking();
    
    var ps = new kakao.maps.services.Places(); // 장소 검색하는 객체 생성
    ps.keywordSearch('헬스장', placesSearchCB); // 키워드로 장소 검색

    function placesSearchCB(data, status, pagination) {
        if (status === kakao.maps.services.Status.OK) {
            console.log('검색 결과:', data);
            for (var i = 0; i < data.length; i++) {
                displayMarker(data[i]);
            }
        } else {
            console.log('검색 실패:', status);
        }
    }

    function displayMarker(place) {
        var marker = new kakao.maps.Marker({
            map: map,
            position: new kakao.maps.LatLng(place.y, place.x)
        });

        kakao.maps.event.addListener(marker, 'click', function() {
            alert(place.place_name);
        });
    }
    
	// 현재 위치 버튼을 생성합니다
    var locationControl = document.createElement('div');
    locationControl.className = 'location-btn';
    locationControl.innerHTML = '<img src="image/location_search_button.svg" alt="현위치" style="width: 30px; height: 30px;">';
    locationControl.onclick = moveToCurrentLocation;

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
	
    // 초기화 시 지도의 크기를 조정
    window.onload = function() {
        map.relayout();
    };
    
    function displayMarker(place) {
        // 헬스장 마커 이미지 설정
        var gymImageSrc = 'image/gymMark.svg'; // 헬스장 마커 이미지 URL
        var gymImageSize = new kakao.maps.Size(32, 34); // 헬스장 마커 이미지 크기
        var gymImageOption = { offset: new kakao.maps.Point(27, 69) }; // 헬스장 마커 이미지 옵션

        var gymMarkerImage = new kakao.maps.MarkerImage(gymImageSrc, gymImageSize, gymImageOption);
        var marker = new kakao.maps.Marker({
            map: map,
            position: new kakao.maps.LatLng(place.y, place.x),
            image: gymMarkerImage // 헬스장 마커 이미지 설정
        });

        kakao.maps.event.addListener(marker, 'click', function() {
            alert(place.place_name);
        });
    }
    
    
    gyms.forEach(function(gym) {
        var markerPosition = new kakao.maps.LatLng(gym.lat, gym.lng);
        var marker = new kakao.maps.Marker({
            position: markerPosition
        });
        marker.setMap(map);
    });
    
    
    
</script>

</body>
</html>