package com.ntschy.underground.entity.base;

import lombok.Data;

@Data
public class TokenInfo {
    private String userID;

    private String adminUserID;

    private String password;

    private String loginName;

    public TokenInfo() {

    }

    public TokenInfo(String userID, String adminUserID, String password) {
        this.userID = userID;
        this.adminUserID = adminUserID;
        this.password = password;
    }
}
