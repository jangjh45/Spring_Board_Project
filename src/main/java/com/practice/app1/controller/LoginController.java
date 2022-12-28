package com.practice.app1.controller;

import com.practice.app1.dao.UserDao;
import com.practice.app1.domain.User;
import com.practice.app1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(String id, String psw, String toURL, boolean rememberId,
                        HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 1. id와 pwd를 확인
        if(!loginCheck(id, psw)) {
            // 2-1 일치하지 않으면, loginForm으로 이동
            String msg = URLEncoder.encode("id 또는 pwd가 일치하지 않습니다.", "utf-8");
            return "redirect:/login/login?msg="+msg;
        }

        // 2-2. id와 pwd가 일치하면,
        // 세션 객체를 얻어오기
        HttpSession session = request.getSession();
        // 세션 객체에 id를 저장
        session.setAttribute("id", id);

        if(rememberId) {
            // 1-1. 쿠키를 생성
            Cookie cookie = new Cookie("id", id);
            // 1-2. 응답에 저장
            response.addCookie(cookie);
        } else {
            // 2-1. 쿠키를 삭제
            Cookie cookie = new Cookie("id", id);
            // 2-2. 쿠키를 삭제
            cookie.setMaxAge(0);
            // 2-3. 응답에 저장
            response.addCookie(cookie);
        }
        // 3. 홈으로 이동
        toURL = toURL==null || toURL.equals("") ? "/" : toURL;

        return "redirect:"+toURL;
    }

    private boolean loginCheck(String id, String psw) throws Exception {
        User user = userService.UserSelect(id);

        if(user==null)
            return false;

        return user.getPsw().equals(psw);
    }
}