package org.scau.mimi.util;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static String dateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(date);
    }

}
