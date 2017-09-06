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

        public class Comment {
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

        }
    }
}
