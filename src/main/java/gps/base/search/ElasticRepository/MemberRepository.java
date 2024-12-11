package gps.base.search.ElasticRepository;


import gps.base.search.ElasticEntity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT m FROM Member m WHERE m.email LIKE %:keyword% OR m.nickname LIKE %:keyword%")
    Page<Member> searchByKeyword(String keyword, Pageable pageable);

    Page<Member> findByEmailContainingOrNicknameContaining(String keyword, String keyword1, Pageable pageable);
}