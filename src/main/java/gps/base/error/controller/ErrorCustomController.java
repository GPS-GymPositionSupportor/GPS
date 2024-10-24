package gps.base.error.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
@Slf4j
public class ErrorCustomController implements ErrorController {

    @GetMapping("/500")
    public String handleServerError(Model model) {
        model.addAttribute("errorMessage", "서버 내부 오류가 발생했습니다.");
        model.addAttribute("errorCode", "500");
        return "error/serverError";
    }

    @GetMapping("/503")
    public String handleServiceUnavailable(Model model) {
        model.addAttribute("errorMessage", "서비스가 일시적으로 중단되었습니다.");
        model.addAttribute("errorCode", "503");
        return "error/serviceUnavailable";
    }

    @GetMapping("/security")
    public String handleSecurityError(Model model) {
        model.addAttribute("errorMessage", "보안 관련 오류가 발생했습니다.");
        model.addAttribute("errorCode", "600");
        return "error/securityError";
    }

    @GetMapping("/404")
    public String handleNotFound(Model model) {
        model.addAttribute("errorMessage", "페이지를 찾을 수 없습니다.");
        model.addAttribute("errorCode", "404");
        return "error/notFound";
    }

    @GetMapping("/403")
    public String handleForbidden(Model model) {
        model.addAttribute("errorMessage", "해당 페이지에 대한 접근 권한이 없습니다.");
        model.addAttribute("errorCode", "403");
        return "error/forbidden";
    }
}
