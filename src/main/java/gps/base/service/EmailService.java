package gps.base.service;

import gps.base.model.Member;
import gps.base.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
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
        number = (int) (Math.random() * 900000) + 100000;
    }

    @Async
    public CompletableFuture<Void> sendVerificationCodeId(String name, String email, HttpSession session) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent() && member.get().getName().equals(name)) {
            createNumber();
            MimeMessage message = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(senderEmail);
                helper.setTo(email);
                helper.setSubject("인증코드");

                String body = "";
                body += "<h3>요청하신 인증 번호입니다.</h3>";
                body += "<h1>" + number + "</h1>";
                body += "<h3>감사합니다.</h3>";

                helper.setText(body, true);
                javaMailSender.send(message);

                session.setAttribute("verificationCode", number); // 인증 코드를 세션에 저장
                session.setAttribute("email", email); // 이메일도 세션에 저장
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("등록된 이름과 이메일이 일치하지 않습니다.");
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> sendVerificationCodePassword(String mId, String email, HttpSession session) {
        Optional<Member> member1 = memberRepository.findBymId(mId);
        Optional<Member> member2 = memberRepository.findByEmail(email);
        if (member1.isPresent() && member2.isPresent() && member1.get().getEmail().equals(email)) {
            createNumber();
            MimeMessage message = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(senderEmail);
                helper.setTo(email);
                helper.setSubject("인증코드");

                String body = "";
                body += "<h3>요청하신 인증 번호입니다.</h3>";
                body += "<h1>" + number + "</h1>";
                body += "<h3>감사합니다.</h3>";

                helper.setText(body, true);
                javaMailSender.send(message);

                session.setAttribute("verificationCode", number); // 인증 코드를 세션에 저장
                session.setAttribute("email", email); // 이메일도 세션에 저장
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("등록된 ID와 이메일이 일치하지 않습니다.");
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public void sendTemporaryPassword(String email, HttpSession session) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            String tempPassword = generateTemporaryPassword();
            MimeMessage message = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(senderEmail);
                helper.setTo(email);
                helper.setSubject("임시 비밀번호");

                String body = "";
                body += "<h3>요청하신 임시 비밀번호입니다.</h3>";
                body += "<h1>" + tempPassword + "</h1>";
                body += "<h3>감사합니다.</h3>";

                helper.setText(body, true);
                javaMailSender.send(message);

                session.setAttribute("temporaryPassword", tempPassword); // 임시 비밀번호 세션에 저장
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("등록된 이메일이 아닙니다.");
        }
    }

    @Async
    public void sendUserId(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            String userId = member.get().getMId();
            MimeMessage message = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(senderEmail);
                helper.setTo(email);
                helper.setSubject("아이디 전송");

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

    public String generateTemporaryPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder tempPassword = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            tempPassword.append(chars.charAt(random.nextInt(chars.length())));
        }
        return tempPassword.toString();
    }
}
