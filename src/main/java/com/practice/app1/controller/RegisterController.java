package com.practice.app1.controller;

import com.practice.app1.dao.UserDao;
import com.practice.app1.domain.User;
import com.practice.app1.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    UserDao userDao;
    final int FAIL = 0;

    @InitBinder
    public void toDate(WebDataBinder binder) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(df, false));

        binder.setValidator(new UserValidator());
    }
    @RequestMapping("/add")
    public String register() {
        return "registerForm";
    }

    @PostMapping("/add") // 윗 줄과 동일하고 간단하게 작성 가능
    public String save(@Valid User user, BindingResult result, Model m) throws Exception{
        // BindingResult result는 바인딩할 User user객체 바로 뒤에 와야한다.

        // User객체를 검증한 결과에 에러가 있으면, registerForm을 이용해서 에러를 보여줘야 함.
        if(!result.hasErrors()) {
            // 2. DB에 신규회원 정보를 저장
            int rowCnt = userDao.insert(user);

            if(rowCnt!=FAIL) {
                return "registerInfo";
            }
        }
        return "registerForm";
    }
}