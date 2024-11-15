package gps.base;

import gps.base.service.MailService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class EmailValidationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MailService mailService; // 이메일 발송 Mock 처리

    @Test
    public void testMailConfirm() throws Exception {
        // 이메일 인증 코드 발송 테스트
        String email = "test@example.com";
        String mockCode = "123456";

        // Mock 메일 서비스 동작 설정
        Mockito.when(mailService.sendSimpleMessage(email)).thenReturn(mockCode);

        mockMvc.perform(post("/api/register/email-validate")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(content().string(mockCode)); // 반환값이 mockCode와 같은지 확인
    }

    @Test
    public void testVerifyCode_Success() throws Exception {
        // 인증 성공 테스트
        mockMvc.perform(post("/api/register/verifyCode")
                        .param("inputCode", "123456")
                        .param("sessionCode", "123456"))
                .andExpect(status().isOk())
                .andExpect(content().string("true")); // 반환값이 true인지 확인
    }

    @Test
    public void testVerifyCode_Fail() throws Exception {
        // 인증 실패 테스트
        mockMvc.perform(post("/api/register/verifyCode")
                        .param("inputCode", "654321")
                        .param("sessionCode", "123456"))
                .andExpect(status().is4xxClientError()); // 인증 실패로 예외 발생 확인
    }
}