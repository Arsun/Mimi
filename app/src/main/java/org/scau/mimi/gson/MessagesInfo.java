package org.scau.mimi.gson;

import java.util.List;

/**
 * Created by 10313 on 2017/8/23.
 */

public class MessagesInfo {

    public int code;

    public String message;

    public Content content;


    public class Content {

        public List<Message> messageList ;

        public Page page;

        public class Message {
            public int mid;

            public int uid;

            public User user;

            public String content;

            public int tmCreated;

            public List<MessageImageSet> messageImageSet ;

            public boolean isFake;

            public String fakeName;

            public int likeCount;

            public int commentCount;

            public int commentNextVal;

            public Location location;

            public class User {
                public int uid;

                public String uname;

                public String nname;

                public int tmCreated;
            }

            public class MessageImageSet {
                public int imageid;

                public String webPath;

                public String mid;

            }

            public class Location {
                public int lid;

                public String locale;

            }

        }

        public class Page {
            public int itemPerPage;

            public int pageNum;

            public int item;

            public int page;

            public int firstItem;

            public int itemInThisPage;

            public int first;
        }

    }

}
