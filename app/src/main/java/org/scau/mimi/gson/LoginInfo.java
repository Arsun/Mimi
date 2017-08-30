package org.scau.mimi.gson;

/**
 * Created by 10313 on 2017/8/23.
 */

public class LoginInfo {

    public int code;

    public String message;

    public Content content;


    public class Content {
        public String secret;

        public User user;

        public long tmCreated;

        public long tmExpire;

        public class User {
            public int uid;

            public String uname;

            public String nname;

            public long tmCreated;

        }

    }

}
