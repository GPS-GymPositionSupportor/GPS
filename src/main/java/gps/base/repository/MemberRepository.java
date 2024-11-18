package gps.base.repository;

import gps.base.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // mId로 Member를 찾는 Method
    Optional<Member> findBymId(String mId);

    Optional<Member> findByEmail(String email);
}