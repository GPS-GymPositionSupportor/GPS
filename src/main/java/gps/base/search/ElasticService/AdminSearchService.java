package gps.base.search.ElasticService;


import gps.base.search.ElasticConfig.Document.GymDocument;
import gps.base.search.ElasticEntity.ElasticGym;
import gps.base.search.ElasticEntity.ElasticMember;
import gps.base.search.ElasticRepository.ElasticGymRepository;
import gps.base.search.ElasticRepository.GymSearchRepository;
import gps.base.search.ElasticRepository.ElasticMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminSearchService {
    private ElasticMemberRepository elasticMemberRepository;
    private final ElasticGymRepository elasticGymRepository;
    private final GymSearchRepository gymSearchRepository;

    public Page<ElasticMember> searchMembers(String keyword, Pageable pageable) {
        return elasticMemberRepository.findByEmailContainingOrNicknameContaining(keyword, keyword, pageable);
    }

    public Page<ElasticGym> searchGyms(String keyword, Pageable pageable) {
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
        return elasticGymRepository.findByIdIn(gymIds, pageable);
    }
}