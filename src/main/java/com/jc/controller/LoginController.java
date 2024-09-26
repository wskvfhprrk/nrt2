package com.jc.controller;

import com.jc.config.Result;
import com.jc.entity.LoginResult;
import com.jc.entity.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @PostMapping("login")
    public Result login(@RequestBody User user){
        LoginResult loginResult = new LoginResult();
        loginResult.setToken("123abcd");
        user.setPassword(null);
        user.setId(1L);
        loginResult.setUser(user);
        return Result.success(loginResult);
//        return Result.error(500,"用户名密码不正确");
    }
}
