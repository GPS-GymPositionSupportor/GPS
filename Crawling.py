import pymysql
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException, NoSuchElementException, NoSuchWindowException, WebDriverException
from geopy.geocoders import Nominatim
from time import sleep
import re
import json
import os
from dotenv import load_dotenv


# 환경 변수 가져오기
load_dotenv()

# .env
host = os.getenv('DB_host')
user = os.getenv('DB_user')
password = os.getenv('DB_password')
database = os.getenv('DB_name')

# MySQL DB 연결 설정
db = pymysql.connect(
    host=host,
    user=user,
    password=password,
    database=database,
    charset='utf8mb4'
)

cursor = db.cursor()

# 주소 Nominatim 객체 생성
geo_local = Nominatim(user_agent='South Korea', timeout=10)

# 위도/경도 반환 함수
def geocoding(address):
    try:
        geo = geo_local.geocode(address)
        if geo:
            x_y = [geo.latitude, geo.longitude]
            return x_y
    except:
        pass
    sleep(1)
    return [0, 0]

# 중복 체크 함수
def is_duplicate(address1,title):
    cursor.execute("SELECT COUNT(*) FROM gym WHERE address1 = %s AND g_name = %s", (address1, title))
    count = cursor.fetchone()[0]
    return count > 0

# 크롬 드라이버 경로 설정
service = Service('C:/chromedriver-win64/chromedriver.exe')

# 크롤링 옵션 생성
options = webdriver.ChromeOptions()

# 백그라운드 실행 옵션 추가
options.add_argument("headless")

# 크롬 드라이버 실행
driver = webdriver.Chrome(service=service,options=options)

# 카카오맵 열기
URL = "https://map.kakao.com/"
driver.get(URL)

# 레이어 버튼 클릭
ico_coach = driver.find_element(By.XPATH, '//div[@class="view_coach"]/span[@class="ico_coach"]')
ico_coach.click()
sleep(1)

# 순회할 지역 리스트
region_list = ["서울","경기", "인천", "강원", "충남", "충북", "전남", "전북", "경남", "경북", "대구", "대전", "부산", "세종", "울산", "광주", "제주"]

# 서울특별시 구 리스트
seoul_list = ['서울 마포구','서울 서대문구','서울 은평구','서울 종로구','서울 중구','서울 용산구','서울 성동구','서울 광진구',
              '서울 동대문구','서울 성북구','서울 강북구','서울 도봉구','서울 노원구','서울 중랑구','서울 강동구','서울 송파구',
              '서울 강남구','서울 서초구','서울 관악구','서울 동작구','서울 영등포구','서울 금천구','서울 구로구','서울 양천구',
              '서울 강서구']

# 경기도 시군 리스트
gyeonggi_list = ['경기 수원시','경기 용인시','경기 성남시','경기 부천시','경기 화성시','경기 안산시','경기 안양시',
                 '경기 평택시','경기 시흥시','경기 김포시','경기 광주시','경기 광명시','경기 군포시','경기 하남시',
                 '경기 오산시','경기 이천시','경기 안성시','경기 의왕시','경기 양평군','경기 여주시','경기 과천시',
                 '경기 고양시','경기 남양주시','경기 파주시','경기 의정부시','경기 양주시','경기 구리시','경기 포천시',
                 '경기 동두천시', '경기 가평군','경기 연천군']

# 강원도 시군 리스트
gangwon_list = ["강원 춘천시","강원 원주시", "강원 강릉시", "강원 동해시", "강원 태백시", "강원 속초시", "강원 삼척시",
                "강원 홍천군", "강원 횡성군", "강원 영월군", "강원 평창군", "강원 정선군", "강원 철원군", "강원 화천군",
                "강원 양구군", "강원 인제군", "강원 고성군", "강원 양양군"]

# 충청북도 시군 리스트
chungbuk_list = ["충북 청주시", "충북 충주시", "충북 제천시", "충북 보은군", "충북 옥천군", "충북 영동군", "충북 증평군",
                 "충북 진천군", "충북 괴산군", "충북 음성군", "충북 단양군"]

# 충청남도 시군 리스트
chungnam_list = ["충남 천안시", "충남 공주시", "충남 보령시", "충남 아산시", "충남 서산시", "충남 논산시", "충남 계룡시",
                 "충남 당진시", "충남 금산군", "충남 부여군", "충남 서천군", "충남 청양군", "충남 홍성군", "충남 예산군",
                 "충남 태안군"]

# 전라북도 시군 리스트
jeonbuk_list = ["전북 전주시", "전북 군산시", "전북 익산시", "전북 정읍시", "전북 남원시", "전북 김제시", "전북 완주군",
                "전북 진안군", "전북 무주군", "전북 장수군", "전북 임실군", "전북 순창군", "전북 고창군", "전북 부안군"]

# 전라남도 시군 리스트
jeonnam_list = ["전남 목포시", "전남 여수시", "전남 순천시", "전남 나주시", "전남 광양시", "전남 담양군", "전남 곡성군",
                "전남 구례군", "전남 고흥군", "전남 보성군", "전남 화순군", "전남 장흥군", "전남 강진군", "전남 해남군",
                "전남 영암군", "전남 무안군", "전남 함평군", "전남 영광군", "전남 장성군", "전남 완도군", "전남 진도군",
                "전남 신안군"]

# 경상북도 시군 리스트
gyeongbuk_list = ["경북 포항시", "경북 경주시", "경북 김천시", "경북 안동시", "경북 구미시", "경북 영주시", "경북 영천시",
                  "경북 상주시", "경북 문경시", "경북 경산시", "경북 군위군", "경북 의성군", "경북 청송군", "경북 영양군",
                  "경북 영덕군", "경북 청도군", "경북 고령군", "경북 성주군", "경북 칠곡군", "경북 예천군", "경북 봉화군",
                  "경북 울진군", "경북 울릉군"]

# 경상남도 시군 리스트
gyeongnam_list = ["경남 창원시", "경남 진주시", "경남 통영시", "경남 사천시", "경남 김해시", "경남 밀양시", "경남 거제시",
                  "경남 양산시", "경남 의령군", "경남 함안군", "경남 창녕군", "경남 고성군", "경남 남해군", "경남 하동군",
                  "경남 산청군", "경남 함양군", "경남 거창군", "경남 합천군"]

# 인천광역시 구 리스트
incheon_list = ["인천 중구", "인천 동구", "인천 미추홀구", "인천 연수구", "인천 남동구", "인천 부평구", "인천 계양구", 
                "인천 서구", "인천 강화군", "인천 옹진군"]

# 대구광역시 구 리스트
daegu_list = ["대구 중구", "대구 동구", "대구 서구", "대구 남구", "대구 북구", "대구 수성구", "대구 달서구", "대구 달성군"]

# 대전광역시 구 리스트
daejeon_list = ["대전 동구", "대전 중구", "대전 서구", "대전 유성구", "대전 대덕구"]

# 부산광역시 구 리스트
busan_list = ["부산 중구", "부산 서구", "부산 동구", "부산 영도구", "부산 부산진구", "부산 동래구", "부산 남구", 
              "부산 북구", "부산 해운대구", "부산 사하구", "부산 금정구", "부산 강서구", "부산 연제구", "부산 수영구", 
              "부산 사상구", "부산 기장군"]

#CrawlAndSave(region_list)

def CrawlAndSave(list):
    sport = "헬스장"
    cate = "physics"
    for region in list:
        # 검색창 찾기
        sleep(1)
        search = driver.find_element(By.XPATH, '//*[@id="search.keyword.query"]')
        search.clear()
        word = f"{region} {sport}"
        search.send_keys(word)
        search.send_keys(Keys.RETURN)
        sleep(1)

         # 카카오맵에서 카테고리 하나로 분류돼 있는 종목의 카테고리 클릭하기
        try:
            if(sport == "수영장" or sport == "태권도장"):
                category_xpath = f'//*[@id="info.search.place.list"]/li[1]/div[3]/span'
                category = driver.find_element(By.XPATH, category_xpath)
                if(category.text == sport):
                    category.click()
        except NoSuchElementException:
            pass

        # 요소 최대 개수
        sleep(1)
        try:
            count_all_text = driver.find_element(By.XPATH, '//*[@id="info.search.place.cnt"]').text
            count_all = int(count_all_text.replace(",", ""))
        except (NameError,ValueError):
            print(f"{region} 검색결과 0개")
            continue
            pass
        if(count_all > 500):
            print(f"{region} 최대 개수가 500개 이상")
            if(region == "서울"):
                CrawlAndSave(seoul_list)
                continue
            elif(region == "경기"):
                CrawlAndSave(gyeonggi_list)
                continue
            elif(region == "강원"):
                CrawlAndSave(gangwon_list)
                continue
            elif(region == "인천"):
                CrawlAndSave(incheon_list)
                continue
            elif(region == "충남"):
                CrawlAndSave(chungnam_list)
                continue
            elif(region == "충북"):
                CrawlAndSave(chungbuk_list)
                continue
            elif(region == "경북"):
                CrawlAndSave(gyeongbuk_list)
                continue
            elif(region == "경남"):
                CrawlAndSave(gyeongnam_list)
                continue
            elif(region == "전북"):
                CrawlAndSave(jeonbuk_list)
                continue
            elif(region == "전남"):
                CrawlAndSave(jeonnam_list)
                continue
            elif(region == "대전"):
                CrawlAndSave(daejeon_list)
                continue
            elif(region == "부산"):
                CrawlAndSave(busan_list)
                continue
            elif(region == "대구"):
                CrawlAndSave(daegu_list)
            else:
                count_all=500
            


        # 페이지 수가 2개 이상일때
        if(count_all>15):

            remain = count_all % 15
            page_max = min((count_all // 15 + (1 if remain else 0)), 34)  # 최대 페이지 수를 34로 제한

            # 장소 더보기 클릭 로직 처리
            more = driver.find_element(By.XPATH, '//*[@id="info.search.place.more"]')
            more.click()
            sleep(2)

            # 현재 페이지가 2페이지라면 1페이지로 돌아가기
            try:
                page_2_xpath = '//*[@id="info.search.page.no2"]'
                page_2_element = driver.find_element(By.XPATH, page_2_xpath)
                page_class = page_2_element.get_attribute('class')
                if page_class == 'ACTIVE':
                    page_1_xpath = '//*[@id="info.search.page.no1"]'
                    page_1_element = driver.find_element(By.XPATH, page_1_xpath)
                    page_1_element.click()
                sleep(2)
            except NoSuchElementException:
                pass
        # 페이지 수가 1개 일때
        else:
            page_max = 2
            remain = 0
            
        main_window = driver.current_window_handle

        # 개수 세기
        cnt = 0

        # 저장된 개수 세기
        insert_cnt = 0

        # 누락된 값 저장
        miss_cnt = 0

        # 중복된 값 개수 세기
        reinsert_cnt = 0

        # 현재 페이지
        page_cnt=1

        # 페이지를 넘기는 로직 및 헬스장 정보 크롤링
        for page_cnt in range(1, page_max + 1):
            print(f"{page_cnt} 페이지로 이동 완료")
            items_per_page = 15 if page_cnt < page_max else remain
            for i in range(1, items_per_page + 1):
                try:
                    cnt += 1

                    tmp_sport = sport

                    tmp_cate = cate

                    sleep(1)

                    # 카카오맵 카테고리 예외처리
                    try:
                        category_xpath = f'//*[@id="info.search.place.list"]/li[{i}]/div[3]/span'
                        category = driver.find_element(By.XPATH, category_xpath)

                        if(category == "주차장" or category == "펜션" or category == "협회" or category == "화장실" or category == "호텔"):
                            print(f'{word}{cnt} 카테고리에서 예외처리')
                            miss_cnt+=1
                            continue
                    except NoSuchElementException:
                        pass

                    # 시설 이름 가져오기
                    try:
                        title_xpath = f'//*[@id="info.search.place.list"]/li[{i}]/div[3]/strong/a[2]'
                        title = driver.find_element(By.XPATH, title_xpath).text
                    except NoSuchElementException:
                        break

                    #battle 데이터 정제
                    if("주짓수" in title):
                        tmp_sport = "주짓수"
                        tmp_cate = "battle"
                    elif("복싱" in title):
                        tmp_sport = "복싱장"
                        tmp_cate = "battle"
                    elif("유도" in title):
                        tmp_sport = "유도"
                        tmp_cate = "battle"
                    elif("합기도" in title):
                        tmp_sport = "합기도"
                        tmp_cate = "battle"
                    elif("특공무술" in title):
                        tmp_sport = "특공무술"
                        tmp_cate = "battle"
                    elif("태권" in title):
                        tmp_sport = "태권도장"
                        tmp_cate = "battle"

                    #physics 데이터 정제
                    if("필라테스" in title):
                        tmp_sport = "필라테스"
                    elif("헬스장" in title):
                        tmp_sport = "헬스장"
                    elif("크로스핏" in title):
                        tmp_sport = "크로스핏"
                    elif("클라이밍" in title):
                        tmp_sport = "클라이밍"
                    elif("요가" in title):
                        tmp_sport = "요가"
                        

                    # 도로명 주소 가져오기
                    address1_xpath = f'//*[@id="info.search.place.list"]/li[{i}]/div[5]/div[2]/p[1]'
                    address1 = driver.find_element(By.XPATH, address1_xpath).text

                    if(is_duplicate(address1,title)):
                        reinsert_cnt += 1
                        print(f'{word}{cnt} : {title} 이미존재')
                        continue

                    # 지번 주소 가져오기
                    address2_xpath = f'//*[@id="info.search.place.list"]/li[{i}]/div[5]/div[2]/p[2]'
                    address2 = driver.find_element(By.XPATH, address2_xpath).text
                    address2 = address2.replace("(지번) ", "")

                    # 지번 주소가 없을시(실외 일때)
                    if(address2 == ""):
                        address2 = "정보 없음"
                        opening_hours = "정보 없음"
                        number = "정보 없음"
                        homepage = "정보 없음"

                    
                    # 지번 주소를 좌표로 변환
                    x_y = geocoding(address2)
                    longitude, latitude = x_y[1], x_y[0]

                    if(longitude+latitude==0):
                        # 도로명 주소를 좌표로 변환
                        x_y = geocoding(address1)
                        longitude, latitude = x_y[1], x_y[0]
                            
                    # x축 y축 누락된 값 삭제
                    if(longitude == 0 and latitude == 0):
                        miss_cnt+=1
                        print(f'{word}{cnt} : {title} 좌표 누락')
                        continue

                    else:
                    
                        # 영업 시간 가져오기
                        try:
                            opening_hours_xpath = f'//*[@id="info.search.place.list"]/li[{i}]/div[5]/div[3]/p/a'
                            opening_hours = driver.find_element(By.XPATH, opening_hours_xpath).text
                        except NoSuchElementException:
                            opening_hours = "정보 없음"

                        # 전화번호 가져오기
                        try:
                            number_xpath = f'//*[@id="info.search.place.list"]/li[{i}]/div[5]/div[4]/span[1]'
                            number = driver.find_element(By.XPATH, number_xpath).text
                        except NoSuchElementException:
                            number = "정보 없음"

                        # 홈페이지 가져오기
                        try:
                            homepage_xpath = f'//*[@id="info.search.place.list"]/li[{i}]/div[5]/div[4]/a[contains(@class, "homepage")]'
                            homepage = driver.find_element(By.XPATH, homepage_xpath).get_attribute('href')
                            if homepage == "https://map.kakao.com/#none":
                                homepage = "정보 없음"
                        except NoSuchElementException:
                            homepage = "정보 없음"
                        sleep(1)
                    
                    # 상세정보 창 누르기
                    try:
                        info_page_xpath = f'//*[@id="info.search.place.list"]/li[{i}]/div[5]/div[4]/a[1]'
                        info_page = WebDriverWait(driver, 2).until(
                            EC.element_to_be_clickable((By.XPATH, info_page_xpath))
                        )
                        info_page.click()

                        # 새 창으로 전환
                        WebDriverWait(driver, 2).until(EC.number_of_windows_to_be(2))
                        driver.switch_to.window(driver.window_handles[-1])
                        sleep(1)

                        # 이미지 URL 가져오기
                        image_url = "https://lh3.google.com/u/0/d/1LTWk1r6ZWMkjWoYBSNS3JII2t6qjp3hY=w1920-h922-iv1"
                        
                        try:
                            image_element = WebDriverWait(driver, 2).until(
                                EC.presence_of_element_located((By.XPATH, '//*[@class="bg_present"]'))
                            )
                            style_attribute = image_element.get_attribute("style")
                            image_url_match = re.search(r'url\((\'|\")?(.*?)(\'|\")?\)', style_attribute)
                            if image_url_match:
                                image_url = image_url_match.group(2)
                            else:
                                image_url = "https://lh3.google.com/u/0/d/1LTWk1r6ZWMkjWoYBSNS3JII2t6qjp3hY=w1920-h922-iv1"
                        except (NoSuchElementException, TimeoutException):
                            pass


                    except (NoSuchWindowException, TimeoutException, WebDriverException) as e:
                        print(f"운동시설{cnt} 상세 정보 창 접근 중 오류 발생: {e}")
                        if len(driver.window_handles) > 1:
                            driver.close()
                        if main_window in driver.window_handles:
                            driver.switch_to.window(main_window)
                        continue

                    # DB에 gym 데이터 저장
                    cursor.execute('''
                        INSERT INTO gym (g_name, category, address1, address2, opening_hours, homepage, phone_number, g_longitude, g_latitude)
                        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
                    ''', (title, cate, address1, address2, opening_hours, homepage, number, longitude, latitude))
                    db.commit()

                    insert_cnt += 1

                    # 최근 삽입된 gym_id 가져오기
                    cursor.execute("SELECT LAST_INSERT_ID()")
                    gym_id = cursor.fetchone()[0]

                    if(len(region)>2):
                        region = region[0:2]

                    r = address1[0:2]

                    # DB에 image 데이터 저장
                    cursor.execute('''
                        INSERT INTO image (caption, added_at, ImageUrl, gym_id)
                        VALUES (%s, NOW(), %s, %s)
                    ''', (f"{r} {tmp_sport} 이미지", image_url, gym_id))
                    db.commit()

                    # 드라이버 닫고 원래 창으로 전환
                    if len(driver.window_handles) > 1:
                        driver.close()
                    if main_window in driver.window_handles:
                        driver.switch_to.window(main_window)
                    sleep(2)

                    print(f'{word}{cnt} : {title} 저장완료 ')

                except Exception as e:
                    print(f"운동시설{cnt} 정보를 가져오는 중 오류 발생: {e}")
                    if main_window in driver.window_handles:
                        driver.switch_to.window(main_window)
                    continue

            # 페이지를 넘기는 로직
            try:
                if(page_cnt >= page_max+1):
                    print(f'현재 {page_cnt} 페이지의 {items_per_page}까지 모두 추출 하였습니다')
                elif page_cnt % 5 == 0:
                    next_button = WebDriverWait(driver, 2).until(
                        EC.element_to_be_clickable((By.XPATH, '//*[@id="info.search.page.next"]'))
                    )
                    next_button.click()
                else:
                    page_button = WebDriverWait(driver, 2).until(
                        EC.element_to_be_clickable((By.XPATH, f'//*[@id="info.search.page.no{(page_cnt % 5)+1}"]'))
                    )
                    page_button.click()
                sleep(2)  # 페이지 로딩 시간을 위해 대기

            except TimeoutException:
                break

        print(f'{word} 전체 {count_all}개 중에 {insert_cnt}개 저장 {reinsert_cnt}개 중복, {miss_cnt}개 위도 경도 누락')

CrawlAndSave(region_list)

print('데이터 수집 완료')
# 드라이버 종료
driver.quit()
db.close()
