package org.scau.mimi.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by 10313 on 2017/8/23.
 */

public class UserInfo extends DataSupport{

    private int userId;

    private String username;

    private String nickname;

    private int accountCreatedTime;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAccountCreatedTime() {
        return accountCreatedTime;
    }

    public void setAccountCreatedTime(int accountCreatedTime) {
        this.accountCreatedTime = accountCreatedTime;
    }

}
