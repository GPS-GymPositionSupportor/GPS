import pymysql
import json
from datetime import datetime

# JSON 직렬화할 때 datetime 객체 처리 함수
def default_serializer(obj):
    if isinstance(obj, datetime):
        return obj.isoformat()
    raise TypeError(f"Object of type {obj.__class__.__name__} is not JSON serializable")

# MySQL DB 연결 설정
connection = pymysql.connect(
    host='localhost',
    user='root',         #DB 아이디 입력
    password='비밀번호', #DB 비밀번호 입력
    db='gpsdb',          #DB 이름 입력
    charset='utf8mb4'
)

Path = 'C:/jsonData/gym_data.json' # 실제 경로 입력

try:
    with connection.cursor() as cursor:
        # gym 데이터 삽입
        with open(Path, 'r', encoding='utf-8') as gym_file:
            gym_data = json.load(gym_file)
            gym_sql = """
                INSERT INTO gym (
                    address1, address2, g_name, g_longitude, g_latitude, 
                    opening_hours, homepage, phone_number, hashtag, avg_rating, 
                    g_created_by, g_deleted_by, g_created_at, g_deleted_at
                ) VALUES (
                    %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s
                )
            """
            for gym in gym_data:
                cursor.execute(gym_sql, (
                    gym['address1'], gym['address2'], gym['g_name'],
                    gym['g_longitude'], gym['g_latitude'], gym.get('opening_hours'),
                    gym.get('homepage'), gym.get('phone_number'), gym.get('hashtag'),
                    gym.get('avg_rating'), gym.get('g_created_by'), gym.get('g_deleted_by'),
                    gym.get('g_created_at'), gym.get('g_deleted_at')
                ))

        # image 데이터 삽입
        with open(Path, 'r', encoding='utf-8') as image_file:
            image_data = json.load(image_file)
            image_sql = """
                INSERT INTO image (
                    caption, added_at, ImageUrl, user_id, gym_id, r_id
                ) VALUES (
                    %s, %s, %s, %s, %s, %s
                )
            """
            for image in image_data:
                cursor.execute(image_sql, (
                    image['caption'], image['added_at'], image['ImageUrl'],
                    image.get('user_id'), image.get('gym_id'), image.get('r_id')
                ))

        # 변경 사항 커밋
        connection.commit()

    print("gym 및 image 데이터를 MySQL에 삽입했습니다.")
finally:
    connection.close()
