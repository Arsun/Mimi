package org.scau.mimi.util;

/**
 * Created by 10313 on 2017/8/22.
 */

public class TextUtil {

    private TextUtil() {

    }

    public static boolean isTextVaild(String s) {
        if (s == null || s.equals(""))
            return false;

        return true;
    }

}
