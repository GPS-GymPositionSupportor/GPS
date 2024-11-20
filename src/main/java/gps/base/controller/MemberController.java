package gps.base.controller;

import gps.base.DTO.MemberDTO;
import gps.base.error.ErrorCode;
import gps.base.error.exception.CustomException;
import gps.base.model.Authority;
import gps.base.model.Member;
import gps.base.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    // 모든 회원 가져오기
    @GetMapping
    public ResponseEntity<Page<MemberDTO>> getAllMembers(
            @PageableDefault(size = 12, sort = "mCreatedAt", direction = Sort.Direction.DESC) Pageable pageable,
            HttpSession session) {

        // 관리자 권한 체크
        Authority authority = (Authority) session.getAttribute("authority");
        if (authority != Authority.ADMIN) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        Page<MemberDTO> members = memberService.getAllMembers(pageable);
        return ResponseEntity.ok(members);
    }


    // 회원 삭제
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(
            @PathVariable Long memberId,
            HttpSession session) {

        // 관리자 권한 체크
        Authority authority = (Authority) session.getAttribute("authority");
        if (authority != Authority.ADMIN) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }

    // 회원 권한 변경
    @PatchMapping("/{userId}/authority")
    public ResponseEntity<Void> updateUserAuthority(
            @PathVariable Long userId,
            @RequestParam Authority authority,  // RequestBody 대신 RequestParam 사용
            HttpSession session) {

        // 관리자 권한 체크
        Authority currentAuthority = (Authority) session.getAttribute("authority");
        if (currentAuthority != Authority.ADMIN) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        memberService.updateAuthority(userId, authority);
        return ResponseEntity.ok().build();
    }
    
    // 회원 수정
    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateMember(
            @PathVariable Long userId,
            @RequestBody MemberDTO memberDTO,
            HttpSession session
    ) {
        Authority authority = (Authority) session.getAttribute("authority");
        if (authority != Authority.ADMIN) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        memberService.updateMember(userId, memberDTO);
        return ResponseEntity.ok().build();
    }
}
