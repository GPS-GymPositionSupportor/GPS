package gps.base.search.ElasticRepository;


import gps.base.search.ElasticEntity.ElasticMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticMemberRepository extends JpaRepository<ElasticMember, Long> {
    @Query("SELECT m FROM ElasticMember m WHERE m.email LIKE %:keyword% OR m.nickname LIKE %:keyword%")
    Page<ElasticMember> searchByKeyword(String keyword, Pageable pageable);

    Page<ElasticMember> findByEmailContainingOrNicknameContaining(String keyword, String keyword1, Pageable pageable);
}