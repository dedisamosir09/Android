package com.dedi.finalprojectdedi.models.user;

import com.google.gson.annotations.SerializedName;

public class AuthLogin {
    @SerializedName("user")
    public User user;

    @SerializedName("accessToken")
    public String accessToken;

    @SerializedName("tokenType")
    public String tokenType;

    public AuthLogin() {
    }

    public AuthLogin(User user, String accessToken, String tokenType) {
        this.user = user;
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
