package org.scau.mimi.gson;

import java.util.List;

/**
 * Created by 10313 on 2017/9/5.
 */

public class CommentsInfo {

    public int code;

    public String message;

    public Content content;

    public class Content {
        public List<Comment> commentList ;

        public long tmBefore;

        public int numThisPage;

        public int numPerPage;

        public MessagesInfo.Content.Message message;


    }
}
