package gps.base.repository;

import gps.base.ElasticSearchEntity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 사용자 ID로 검색
    Optional<Member> findBymId(String mId);
}