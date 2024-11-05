<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kakao 지도 시작하기</title>
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
<div id="map"></div>

<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=f419f845f436bdd78e7684080e263b3c&libraries=services"></script>

<script>
    var container = document.getElementById('map');
    var options = {
        center: new kakao.maps.LatLng(37.46369169, 126.6502972), // 지도 중심 좌표
        level: 4
    };

    var map = new kakao.maps.Map(container, options); // 지도 생성

 // 마커 초기화
   var marker = new kakao.maps.Marker({
          map: map,
          position: options.center
   });

    // 실시간 위치 추적 시작
    function startTracking() {
        if (navigator.geolocation) {
            navigator.geolocation.watchPosition(function(position) {
                var lat = position.coords.latitude;
                var lon = position.coords.longitude;
                var locPosition = new kakao.maps.LatLng(lat, lon);

                // 마커 위치 업데이트
                marker.setPosition(locPosition);

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

    // 초기화 시 지도의 크기를 조정
    window.onload = function() {
        map.relayout();
    };
</script>

</body>
</html>