package gps.base.search.ElasticService;


import gps.base.search.ElasticConfig.Document.GymDocument;
import gps.base.search.ElasticEntity.Gym;
import gps.base.search.ElasticEntity.Member;
import gps.base.search.ElasticRepository.GymRepository;
import gps.base.search.ElasticRepository.GymSearchRepository;
import gps.base.search.ElasticRepository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminSearchService {
    private MemberRepository memberRepository;
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