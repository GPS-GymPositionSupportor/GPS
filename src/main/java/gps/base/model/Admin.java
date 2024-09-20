package gps.base.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long adminId;

    @JoinColumn(name = "user_id")
    private Long userId;

    @Column(name = "a_id", nullable = false)
    private String aId;


    // 비밀번호 필드가 JSON 직렬화 과정에서 제외될 수 있게, JsonIgnore 어노테이션을 추가.
    @JsonIgnore
    @Column(name = "a_password", nullable = false)
    private String aPassword;

    // Constructors

    public Admin() {
    }

    public Admin(String aId, String aPassword) {
        this.aId = aId;
        this.aPassword = aPassword;
    }


    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", aId='" + aId + '\'' +
                ", aPassword='[PROTECTED]'"  +
                '}';
    }
}
