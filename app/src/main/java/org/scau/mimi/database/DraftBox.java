package org.scau.mimi.database;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by 10313 on 2017/8/29.
 */

public class DraftBox extends DataSupport{

    private String content;

    private Date time;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
