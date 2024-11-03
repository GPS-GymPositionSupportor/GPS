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

# MySQL DB 연결 설정
db = pymysql.connect(
    host='localhost',
    user='root',
    password='2741',
    database='gpsdb',
    charset='utf8mb4'
)

cursor = db.cursor()

# 주소 Nominatim 객체 생성
geo_local = Nominatim(user_agent='South Korea', timeout=10)

# 위도/경도 반환 함수 (재시도 로직 추가)
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
    cursor.execute("SELECT COUNT(*) FROM gym WHERE address1 = %s AND g_name = %s", (address1, g_name))
    count = cursor.fetchone()[0]
    return count > 0

# 크롬 드라이버 설정
service = Service('C:/chromedriver-win64/chromedriver.exe')
driver = webdriver.Chrome(service=service)

# 카카오맵 열기
URL = "https://map.kakao.com/"
driver.get(URL)

# 레이어 버튼 클릭
ico_coach = driver.find_element(By.XPATH, '//div[@class="view_coach"]/span[@class="ico_coach"]')
ico_coach.click()
sleep(1)

# 카테고리 지정
category_list = {
    "water" : ["수영장"],
    "ball" : ["농구장", "축구장", "족구장", "탁구장", "테니스장", "배드민턴장", "풋살장", "배구장"],
    "physics" : ["헬스장", "요가", "크로스핏", "클라이밍"],
    "battle" : ["태권도", "특공무술", "합기도", "유도", "주짓수"]
}
# "서울", "경기", "강원", "충남", "충북", "경북", "경남", "전북", "전남", "대전", "세종", "대구", "울산", "부산", "제주"
region_list = ["충북", "경북", "경남", "전북", "전남", "대전", "세종", "대구", "울산", "부산", "제주"]

search_click = False

for region in region_list:
    # 검색창 찾기
    search = driver.find_element(By.XPATH, '//*[@id="search.keyword.query"]')
    cate = "ball"
    if(search_click):
        search.clear()
    word = f"{region} 농구장"
    search.send_keys(word)
    search.send_keys(Keys.RETURN)
    search_click = True
    sleep(2)

    # 요소 최대 개수
    count_all_text = driver.find_element(By.XPATH, '//*[@id="info.search.place.cnt"]').text
    count_all = int(count_all_text.replace(",", ""))

    remain = count_all % 15
    page_max = min((count_all // 15 + (1 if remain else 0)), 34)  # 최대 페이지 수를 34로 제한

    # ul 요소에서 li 태그를 모두 선택
    ul_xpath = '//*[@id="info.search.place.list"]'
    li_elements = driver.find_elements(By.XPATH, f'{ul_xpath}/li')

    # li 요소 개수
    li_count = len(li_elements)

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
        print("")
    main_window = driver.current_window_handle

    # 개수 세기
    cnt = 0

    # 저장된 개수 세기
    insert_cnt = 0

    # 누락된 값 저장
    miss_cnt = 0

    # 중복된 값 개수 세기
    reinsert_cnt = 0

    page_cnt=1

    # 페이지를 넘기는 로직 및 헬스장 정보 크롤링
    for page_cnt in range(1, page_max + 1):
        print(f"{page_cnt} 페이지로 이동 완료")
        items_per_page = 15 if page_cnt < page_max else remain
        for i in range(1, items_per_page + 1):
            try:
                cnt += 1

                sleep(2)
                # 헬스장 제목 가져오기
                title_xpath = f'//*[@id="info.search.place.list"]/li[{i}]/div[3]/strong/a[2]'
                title = driver.find_element(By.XPATH, title_xpath).text

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
                    sleep(2)
                
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
                    try:
                        image_element = WebDriverWait(driver, 2).until(
                            EC.presence_of_element_located((By.XPATH, '//*[@class="bg_present"]'))
                        )
                        style_attribute = image_element.get_attribute("style")
                        image_url_match = re.search(r'url\((\'|\")?(.*?)(\'|\")?\)', style_attribute)
                        image_url = image_url_match.group(2) if image_url_match else "https://lh3.google.com/u/0/d/1LTWk1r6ZWMkjWoYBSNS3JII2t6qjp3hY=w1920-h922-iv1"
                    except (NoSuchElementException, TimeoutException):
                        image_url = "https://lh3.google.com/u/0/d/1LTWk1r6ZWMkjWoYBSNS3JII2t6qjp3hY=w1920-h922-iv1"

                except (NoSuchWindowException, TimeoutException, WebDriverException) as e:
                    print(f"운동시설 {cnt} 상세 정보 창 접근 중 오류 발생: {e}")
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

                # DB에 image 데이터 저장
                cursor.execute('''
                    INSERT INTO image (caption, added_at, ImageUrl, gym_id)
                    VALUES (%s, NOW(), %s, %s)
                ''', (f"{word} 이미지", image_url, gym_id))
                db.commit()

                # 드라이버 닫고 원래 창으로 전환
                if len(driver.window_handles) > 1:
                    driver.close()
                if main_window in driver.window_handles:
                    driver.switch_to.window(main_window)
                sleep(2)

                print(f'{word}{cnt} : {title} 저장완료 ')

            except Exception as e:
                print(f"운동시설 {cnt} 정보를 가져오는 중 오류 발생: {e}")
                if main_window in driver.window_handles:
                    driver.switch_to.window(main_window)
                continue

        # 페이지를 넘기는 로직
        try:
            if(page_cnt >= page_max+1 and items_per_page == remain):
                print(f'현재 {page_cnt} 페이지의 {items_per_page}까지 모두 추출 하였습니다')
            elif page_cnt % 5 == 0:
                next_button = WebDriverWait(driver, 2).until(
                    EC.element_to_be_clickable((By.XPATH, '//*[@id="info.search.page.next"]'))
                )
                next_button.click()
            else:
                page_button = WebDriverWait(driver, 2).until(
                    EC.element_to_be_clickable((By.XPATH, f'//*[@id="info.search.page.no{(page_cnt % 5)}"]'))
                )
                page_button.click()
            sleep(2)  # 페이지 로딩 시간을 위해 대기

        except TimeoutException:
            print(f"{page_cnt} 페이지로 이동하는 중 오류 발생: TimeoutException")
            break

    print(f'{word} 전체 {count_all}개 중에 {insert_cnt}개의 헬스장 저장 {reinsert_cnt}개 중복, {miss_cnt}개 위도 경도 누락')

# 드라이버 종료
driver.quit()
db.close()
