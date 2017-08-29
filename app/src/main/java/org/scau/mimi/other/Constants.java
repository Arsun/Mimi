package org.scau.mimi.other;

/**
 * Created by 10313 on 2017/8/22.
 */

public class Constants {

    //Test
    public static final String SECRET = "7bd66b0d2c494edf9326cfbfea7bff00";
    public static final int LID = 3;
    public static final String NICKNAME = "大壳儿的正版";

    private Constants() {

    }

    public static final String ADDRESS = "http://104.224.174.146:8080/Mimi-1.1";

    public static final String SIGN_UP = "/api/user/create";

    public static final String LOGIN = "/api/user/login";

    public static final String MESSAGE_LIST = "/api/message/page/";

    public static final String LOCATION_LIST = "/api/locale/list";

    public static final String SEND_MESSAGE = "/api/message/create";

    public static final String UPLOAD_IMAGE = "/api/image/create";

}
