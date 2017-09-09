package org.scau.mimi.gson;

/**
 * Created by 10313 on 2017/9/8.
 */

public class Comment {

    public static final int TYPE_CONTENT = 0;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_FOOTER = 2;
    public static final int TYPE_NO_MORE = 3;

    public int cid;

    public int mid;

    public String rcid;

    public int commentVal;

    public LoginInfo.Content.User user;

    public String content;

    public long tmCreated;

    public int likeCount;

    public boolean isFake;

    public String fakeName;

    public int type = 0;

}

