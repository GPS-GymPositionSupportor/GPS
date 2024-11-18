package gps.base.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "whalsrnr2741@naver.com";
    public static int number;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    // 랜덤으로 숫자 생성
    public static void createNumber() {
        number = (int) (Math.random() * (90000)) + 100000;
    }

    public MimeMessage createMail(String mail) {
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(senderEmail);
            helper.setTo(mail);
            helper.setSubject("인증코드");

            String body = "";
            body += "<h3>요청하신 인증 번호입니다.</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>감사합니다.</h3>";

            helper.setText(body, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    public int sendMail(String mail) {
        MimeMessage message = createMail(mail);
        javaMailSender.send(message);
        return number;
    }
}
