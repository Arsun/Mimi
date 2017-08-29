package org.scau.mimi.gson;

import java.util.List;

/**
 * Created by 10313 on 2017/8/24.
 */

public class LocationsInfo {

    public int code;

    public String message;

    public Content content;

    public class Content {

        public List<MessagesInfo.Content.Message.Location> locationList;

    }

}
