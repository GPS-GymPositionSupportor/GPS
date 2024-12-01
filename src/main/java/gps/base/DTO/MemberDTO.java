package gps.base.DTO;


import gps.base.model.Authority;
import gps.base.model.Member;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MemberDTO {
    private Long userId;

    private String nickname;
    private String email;
    private Integer age;


    private String mId;
    private String name;
    private LocalDateTime birth;
    private String gender;
    private LocalDateTime mCreatedAt;
    private LocalDateTime lastLogin;
    private Authority authority;

    // 정적 팩토리 메서드
    public static MemberDTO from(Member member) {
        MemberDTO dto = new MemberDTO();
        dto.mId = member.getMId();
        dto.name = member.getName();
        dto.nickname = member.getNickname();
        dto.email = member.getEmail();
        dto.birth = member.getBirth();
        dto.gender = member.getGender().toString();
        dto.mCreatedAt = member.getMCreatedAt();
        dto.lastLogin = member.getLastLogin();
        dto.authority = member.getAuthority();
        dto.userId = member.getUserId();
        return dto;
    }

}