package org.scau.mimi.gson;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 10313 on 2017/8/23.
 */

public class MessagesInfo {

    public int code;

    public String message;

    public Content content;


    public class Content {

        public long tmBefore;

        public long tmAfter;

        public List<Message> messageList ;

        public class Message implements Serializable {
            public int mid;

            public int uid;

            public LoginInfo.Content.User user;

            public String content;

            public long tmCreated;

            public List<MessageImageSet> messageImageSet ;

            public boolean isFake;

            public String fakeName;

            public int likeCount;

            public int commentCount;

            public int commentNextVal;

            public Location location;

            public boolean isLike;


            public class MessageImageSet implements Serializable{
                public int imageid;

                public String webPath;

                public String mid;

            }

            public class Location implements Serializable{
                public int lid;

                public String locale;

            }

        }

        public int numThisPage;

        public int numPerPage;


    }

}
