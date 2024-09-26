package com.jc.entity;

import lombok.Data;

/**
 * 登陆返回值
 */
@Data
public class LoginResult {
    private String token;
    private User user;
}
