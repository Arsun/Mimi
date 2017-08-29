package org.scau.mimi.gson;

import android.support.annotation.Nullable;

/**
 * Created by 10313 on 2017/8/25.
 */

public class ImagesInfo {

    public int code;

    public String message;

    public Content content;

    public class Content {
        public int imageid;

        public String webPath;

        @Nullable
        public String mid;
    }

}
