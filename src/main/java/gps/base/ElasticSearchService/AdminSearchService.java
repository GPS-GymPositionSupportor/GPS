package gps.base.ElasticSearchService;

import gps.base.ElasticSearchEntity.Gym;
import gps.base.ElasticSearchEntity.Member;
import gps.base.config.Elastic.Document.GymDocument;
import gps.base.config.Elastic.GymSearchRepository;
import gps.base.repository.GymRepository;
import gps.base.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminSearchService {
    private final MemberRepository memberRepository;
    private final GymRepository gymRepository;
    private final GymSearchRepository gymSearchRepository;

    public Page<Member> searchMembers(String keyword, Pageable pageable) {
        return memberRepository.findByEmailContainingOrNicknameContaining(keyword, keyword, pageable);
    }

    public Page<Gym> searchGyms(String keyword, Pageable pageable) {
        // Elasticsearch에서 검색
        List<GymDocument> elasticResults = gymSearchRepository.findByNameContaining(keyword);

        // ID 리스트 추출
        List<Long> gymIds = elasticResults.stream()
                .map(doc -> Long.parseLong(doc.getId()))
                .collect(Collectors.toList());

        // 빈 결과 처리
        if (gymIds.isEmpty()) {
            return Page.empty(pageable);
        }

        // JPA로 데이터 조회
        return gymRepository.findByIdIn(gymIds, pageable);
    }
}