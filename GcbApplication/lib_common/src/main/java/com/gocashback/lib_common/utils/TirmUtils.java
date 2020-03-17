package com.gocashback.lib_common.utils;

import android.text.TextUtils;

/**
 * @Author 55HAITAO
 * @Date 2020-02-21 17:27
 */
public class TirmUtils {

    public static String trimStart(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        //
        final char[] value = str.toCharArray();
        //
        int start = 0, last = 0 + str.length() - 1;
        int end = last;
        while ((start <= end) && (value[start] <= ' ')) {
            start++;
        }
        if (start == 0 && end == last) {
            return str;
        }
        if (start >= end) {
            return "";
        }
        return str.substring(start, end);
    }
}
