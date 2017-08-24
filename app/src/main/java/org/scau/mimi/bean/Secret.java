package org.scau.mimi.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by 10313 on 2017/8/23.
 */

public class Secret extends DataSupport{

    private String secret;

    private int createdTime;

    private int expiredTime;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public int getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(int createdTime) {
        this.createdTime = createdTime;
    }

    public int getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(int expiredTime) {
        this.expiredTime = expiredTime;
    }
}
