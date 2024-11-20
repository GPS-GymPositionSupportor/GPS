package gps.base.repository;

import gps.base.model.Member;
import gps.base.model.ProviderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // mId로 Member를 찾는 Method
    Optional<Member> findBymId(String mId);
    Page<Member> findAll(Pageable pageable);

    // exists 관련 메소드 추가
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Member m WHERE LOWER(m.mId) = LOWER(:username)")
    boolean existsBymId(@Param("username") String username);

    boolean existsByName(String name);
    boolean existsByNickname(String nickname);

    Optional<Member> findByProviderTypeAndProviderId(ProviderType providerType, String providerId);
}
