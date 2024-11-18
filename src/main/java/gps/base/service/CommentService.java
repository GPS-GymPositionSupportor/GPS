package gps.base.service;

import gps.base.DTO.CommentDTO;
import gps.base.error.ErrorCode;
import gps.base.error.exception.CustomException;
import gps.base.model.Comment;
import gps.base.model.Member;
import gps.base.repository.CommentRepository;
import gps.base.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    public Page<CommentDTO> getAllComments(Pageable pageable) {
        logger.info("댓글 조회 서비스 시작");

        Page<Comment> comments = commentRepository.findAllWithUserOrderByAddedAtDesc(pageable);
        logger.info("DB에서 조회된 댓글 수: {}", comments.getContent().size());

        Page<CommentDTO> dtos = comments.map(comment -> {
            try {
                return convertToDTO(comment);
            } catch (Exception e) {
                logger.error("댓글 변환 중 에러 발생. CommentId: " + comment.getId(), e);
                throw e;
            }
        });

        logger.info("변환된 DTO 수: {}", dtos.getContent().size());
        return dtos;
    }

    private CommentDTO convertToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());          // cid -> id
        dto.setComment(comment.getComment());
        Member user = memberRepository.findById(comment.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        dto.setUserName(user.getName());
        dto.setCreatedAt(comment.getAddedAt()); // addedAt -> createdAt
        dto.setReviewId(comment.getReview().getRId());
        return dto;
    }

}
