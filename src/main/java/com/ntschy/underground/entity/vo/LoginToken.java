package com.ntschy.underground.entity.vo;

import lombok.Data;

@Data
public class LoginToken {

    private String tokenId;

    private String userId;

    private String token;

    private String expiresTime;

    private int status;

    private String createTime;

    public LoginToken() {
    }

    public LoginToken(String userId, String token, String expiresTime, int status, String createTime) {
        this.userId = userId;
        this.token = token;
        this.expiresTime = expiresTime;
        this.status = status;
        this.createTime = createTime;
    }
}
