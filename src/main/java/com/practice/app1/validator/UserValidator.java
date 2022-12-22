package com.practice.app1.validator;

import com.practice.app1.domain.User;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> cla) {
        return User.class.isAssignableFrom(cla); // clazz가 User 또는 그 자손인지 확인
    }

    @Override
    public void validate(Object target, Errors errors) {
        System.out.println("UserValidator.validate() is called");

        User user = (User)target;

        String id = user.getId();

//		if(id==null || "".equals(id.trim())) {
//			errors.rejectValue("id", "required");
//		}

        // 비었거나 공백이면  위 3줄과 같음
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id",  "required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "psw", "required");

        if(id==null || id.length() <  5 || id.length() > 12) {
            errors.rejectValue("id", "invalidLength", new String[]{"", "5", "12"}, null);
        }
    }
}