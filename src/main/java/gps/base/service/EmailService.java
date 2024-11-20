package gps.base.service;

import gps.base.model.Member;
import gps.base.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final MemberRepository memberRepository;
    private static final String senderEmail = "whalsrnr2741@naver.com";
    public static int number;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, MemberRepository memberRepository) {
        this.javaMailSender = javaMailSender;
        this.memberRepository = memberRepository;
    }

    // 랜덤으로 숫자 생성
    public static void createNumber() {
        number = (int) (Math.random() * (90000)) + 100000;
    }

    public MimeMessage createMail(String mail) {
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        Optional<Member> member = memberRepository.findByEmail(mail);
        if (member.isPresent()) {
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
        } else {
            throw new IllegalArgumentException("등록된 이메일이 아닙니다.");
        }
        return message;
    }

    @Async
    public CompletableFuture<Integer> sendMailAsync(String mail) {
        MimeMessage message = createMail(mail);
        javaMailSender.send(message);
        return CompletableFuture.completedFuture(number);
    }

    @Async
    public void sendUserIdAsync(String mail) {
        Optional<Member> member = memberRepository.findByEmail(mail);
        if (member.isPresent()) {
            String userId = member.get().getMId();
            MimeMessage message = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(senderEmail);
                helper.setTo(mail);
                helper.setSubject("아이디 찾기");

                String body = "";
                body += "<h3>요청하신 아이디입니다.</h3>";
                body += "<h1>" + userId + "</h1>";
                body += "<h3>감사합니다.</h3>";

                helper.setText(body, true);
                javaMailSender.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("등록된 이메일이 아닙니다.");
        }
    }

    public boolean validateNameAndEmail(String name, String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.isPresent() && member.get().getName().equals(name);
    }
}
