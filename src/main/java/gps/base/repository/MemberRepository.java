package gps.base.repository;

import gps.base.model.Member;
import gps.base.model.ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // mId로 Member를 찾는 Method
    Optional<Member> findBymId(String mId);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByProviderIdAndProviderType(ProviderType providerType, String providerId);

    // exists 관련 메소드 추가
    boolean existsBymId(String mId);
    boolean existsByName(String name);
    boolean existsByNickname(String nickname);

    Optional<Member> findByProviderTypeAndProviderId(ProviderType kakao, String providerId);
}
