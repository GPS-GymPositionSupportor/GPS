create definer = root@localhost trigger gps.after_insert_authority_to_admin
    after insert
    on gps.member
    for each row
BEGIN
    -- authority가 'ADMIN'일 때 admin 테이블에 추가
    IF NEW.authority = 'ADMIN' THEN
        -- 해당 유저가 이미 admin 테이블에 없을 때만 삽입
        IF (SELECT COUNT(*) FROM admin WHERE user_id = NEW.user_id) = 0 THEN
            INSERT INTO admin (user_id, last_login)
            VALUES (NEW.user_id, NULL);
        END IF;
    END IF;
END;

