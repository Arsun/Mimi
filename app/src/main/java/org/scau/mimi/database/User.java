package org.scau.mimi.database;

import org.litepal.crud.DataSupport;

/**
 * Created by 10313 on 2017/8/29.
 */

public class User extends DataSupport{

    private String username;

    private String nickname;

    private String secret;

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

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
