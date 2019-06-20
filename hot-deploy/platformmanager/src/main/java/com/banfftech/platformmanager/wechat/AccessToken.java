package com.banfftech.platformmanager.wechat;

/**
 * Created by S on 2017/11/14.
 */
public class AccessToken {
    // 获取到的凭证
    private String token;
    // 凭证有效时间、单位:S����
    private int expiresIn;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String toString() {
        return "AccessToken [token=" + token + ", expiresIn=" + expiresIn + "]";
    }

}
