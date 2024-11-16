create definer = root@localhost trigger gps.remove_admin_on_update
    after update
    on gps.member
    for each row
BEGIN
    -- 권한이 'ADMIN'에서 'USER'로 변경된 경우에만 Admin 테이블에서 삭제
    IF OLD.authority = 'ADMIN' AND NEW.authority = 'USER' THEN
        DELETE FROM Admin WHERE user_id = NEW.user_id;
    END IF;
END;

