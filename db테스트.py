import pymysql
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException, NoSuchElementException
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
def geocoding(address, retries=3):
    for attempt in range(retries):
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
def is_duplicate(address1):
    cursor.execute("SELECT COUNT(*) FROM gym WHERE address1 = %s", (address1,))
    count = cursor.fetchone()[0]
    return count > 0

# 크롬 드라이버 설정
service = Service('C:/chromedriver-win64/chromedriver.exe')
driver = webdriver.Chrome(service=service)

# 카카오맵 열기
URL = "https://map.kakao.com/"
driver.get(URL)

#레이어 버튼 클릭
ico_coach = driver.find_element(By.XPATH, '//div[@class="view_coach"]/span[@class="ico_coach"]')
ico_coach.click()
sleep(1)

# 검색창 찾기
search = driver.find_element(By.XPATH, '//*[@id="search.keyword.query"]')
search.send_keys("인천 제물포 헬스장")
search.send_keys(Keys.RETURN)
sleep(2)

# 최대 페이지 계산
count_all = int(driver.find_element(By.XPATH, '//*[@id="info.search.place.cnt"]').text)
remain = count_all % 15
page_max = count_all // 15 + (1 if remain else 0)

main_window = driver.current_window_handle

#개수 세기
cnt = 0

#저장된 개수 세기
insert_cnt = 0

#누락된 값 저장
miss_list = []

#중복된 값 개수 세기
reinsert_cnt = 0

# 페이지를 넘기는 로직 및 헬스장 정보 크롤링
for page_cnt in range(1, page_max + 1):
    items_per_page = 15 if page_cnt < page_max else remain
    for i in range(1, items_per_page + 1):
        try:
            cnt += 1
                        
            # 헬스장 제목 가져오기
            title_xpath = f'//*[@id="info.search.place.list"]/li[{i}]/div[3]/strong/a[2]'
            title = WebDriverWait(driver, 10).until(
                EC.presence_of_element_located((By.XPATH, title_xpath))
            ).text

            # 주소 가져오기
            address1_xpath = f'//*[@id="info.search.place.list"]/li[{i}]/div[5]/div[2]/p[1]'
            address1 = WebDriverWait(driver, 10).until(
                EC.presence_of_element_located((By.XPATH, address1_xpath))
            ).text

            if(is_duplicate(address1)):
                reinsert_cnt+=1
                print(f'헬스장{cnt} : {title} 이미존재')
                continue

            # 상세정보 창 누르기
            info_page_xpath = f'//*[@id="info.search.place.list"]/li[{i}]/div[5]/div[4]/a[1]'
            info_page = WebDriverWait(driver, 10).until(
                EC.element_to_be_clickable((By.XPATH, info_page_xpath))
            )
            info_page.click()

            # 새 창으로 전환
            WebDriverWait(driver, 10).until(EC.number_of_windows_to_be(2))
            driver.switch_to.window(driver.window_handles[-1])
            sleep(2)

            # 지번 주소 가져오기
            try:
                address2_element = WebDriverWait(driver, 10).until(
                    EC.presence_of_element_located((By.XPATH, '//*[@class="location_detail"]/span[@class="txt_addrnum"]'))
                )
                address2 = address2_element.text
                address2 = re.sub(r"^지번\s*", "", address2)
                
            except (NoSuchElementException, TimeoutException):
                address2 = "정보 없음"
                
            # 기타 정보 수집
            div_cnt = 2
            try:
                opening_hours = WebDriverWait(driver, 10).until(
                    EC.presence_of_element_located((By.XPATH, '//*[@id="mArticle"]/div[1]/div[2]/div[2]/div/div[1]/div/ul'))
                ).text
                opening_hours = re.sub(r"\n더보기", "", address2)
            except (NoSuchElementException, TimeoutException):
                opening_hours = "정보 없음"

            if opening_hours != "정보 없음":
                div_cnt += 1

            try:
                homepage = WebDriverWait(driver, 10).until(
                    EC.presence_of_element_located((By.XPATH, '//*[@id="mArticle"]/div[1]/div[2]/div[3]/div/div[1]/a[1]'))
                ).text
            except (NoSuchElementException, TimeoutException):
                homepage = "정보 없음"

            if homepage != "정보 없음":
                div_cnt += 1

            try:
                number = WebDriverWait(driver, 10).until(
                    EC.presence_of_element_located((By.XPATH, '//*[@id="mArticle"]/div[1]/div[2]/div[4]/div/div/span/span[1]'))
                ).text
            except (NoSuchElementException, TimeoutException):
                number = "정보 없음"

            if number != "정보 없음":
                div_cnt += 1

            try:
                tag = WebDriverWait(driver, 10).until(
                    EC.presence_of_element_located((By.XPATH, '//*[@id="mArticle"]/div[1]/div[2]/div[5]/div/div/span'))
                ).text
            except (NoSuchElementException, TimeoutException):
                tag = "정보 없음"

            # 이미지 URL 가져오기
            try:
                image_element = WebDriverWait(driver, 10).until(
                    EC.presence_of_element_located((By.XPATH, '//*[@class="bg_present"]'))
                )
                style_attribute = image_element.get_attribute("style")
                image_url_match = re.search(r'url\((\'|\")?(.*?)(\'|\")?\)', style_attribute)
                image_url = image_url_match.group(2) if image_url_match else "C:/이미지/gps로고.png"
            except (NoSuchElementException, TimeoutException):
                image_url = "C:/이미지/gps로고.png"

            #지번 주소를 좌표로 변환
            sleep(1)
            x_y = geocoding(address2)
            longitude, latitude = x_y[1], x_y[0]
            
            # DB에 gym 데이터 저장
            cursor.execute('''
                INSERT INTO gym (address1, address2, g_name, g_longitude, g_latitude, opening_hours, homepage, phone_number, hashtag)
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
            ''', (address1, address2, title, longitude, latitude, opening_hours, homepage, number, tag))
            db.commit()

            insert_cnt+=1
            
             # 최근 삽입된 gym_id 가져오기
            cursor.execute("SELECT LAST_INSERT_ID()")
            gym_id = cursor.fetchone()[0]

                #x축 y축 누락된 값 id 저장
            if(longitude==0 and latitude==0):
                miss_list.append(gym_id)

            # DB에 image 데이터 저장
            cursor.execute('''
                INSERT INTO image (caption, added_at, ImageUrl, gym_id)
                VALUES (%s, NOW(), %s, %s)
            ''', ("헬스장 대표 이미지", image_url, gym_id))
            db.commit()

            # 드라이버 닫고 원래 창으로 전환
            driver.close()
            driver.switch_to.window(main_window)
            sleep(2)

            print(f'헬스장{cnt} : {title} 저장완료 ')

        except Exception as e:
            print(f"헬스장 {cnt} 정보를 가져오는 중 오류 발생: {e}")
            driver.switch_to.window(main_window)
            continue
        
    # 페이지를 넘기는 로직
    try:
        if page_cnt == 1:
            more_button = WebDriverWait(driver, 10).until(
                EC.element_to_be_clickable((By.XPATH, '//*[@id="info.search.place.more"]'))
            )
            more_button.click()
        elif page_cnt % 5 == 0:
            next_button = WebDriverWait(driver, 10).until(
                EC.element_to_be_clickable((By.XPATH, '//*[@id="info.search.page.next"]'))
            )
            next_button.click()
        else:
            page_button = WebDriverWait(driver, 10).until(
                EC.element_to_be_clickable((By.XPATH, f'//*[@id="info.search.page.no{(page_cnt % 5) + 1}"]'))
            )
            page_button.click()

        print(f"{page_cnt+1} 페이지로 이동 완료")
        sleep(2)  # 페이지 로딩 시간을 위해 대기

    except TimeoutException:
        print(f"{page_cnt} 페이지로 이동하는 중 오류 발생: TimeoutException")
        break

miss_cnt = len(miss_list)
print(f'전체 {count_all}개 중에 {reinsert_cnt}개 중복 {insert_cnt}개의 헬스장 저장 그 중 {miss_cnt}개 위도 경도 누락')

# 드라이버 종료
driver.quit()
db.close()
