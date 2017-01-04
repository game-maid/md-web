/**
 * @Title: StringUtils.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月14日  占志灵
 */

package com.talentwalker.game.md.core.util;

import java.util.Random;

/**
 * @ClassName: StringUtils
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月14日 下午6:58:38
 */

public class StringUtils {
    /**
     * 获取随机字符串
     * @param len 随机字符串长度
     * @return 随机数
     */
    public static String getRandomInt(int len) {

        String[] baseString = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
        Random random = new Random();
        int length = baseString.length;
        String randomString = "";
        for (int i = 0; i < length; i++) {
            randomString += baseString[random.nextInt(length)];
        }
        random = new Random(System.currentTimeMillis());
        String resultStr = "";
        for (int i = 0; i < len; i++) {
            resultStr += randomString.charAt(random.nextInt(randomString.length() - 1));
        }
        return resultStr;
    }

    /**
     * 获取随机字符串
     * @param len 随机长度
     * @return
     */
    public static String getRandomStr(int len) {
        String str = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            sb.append(str.charAt(random.nextInt(str.length())));
        }
        return sb.toString();
    }

    /**
     * 获取随机小写字符
     * @param len 随机长度
     * @return
     */
    public static String getRandomLowerStr(int len) {
        String str = "0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            sb.append(str.charAt(random.nextInt(str.length())));
        }
        return sb.toString();
    }

    /**
     * 校验一组字符串是否包含null或者""
     * @param strings 需要校验的字符串
     * @return true：参数中存在null或者""；false：不存在
     */
    public static boolean isPrarmsEmpty(String... strings) {
        boolean flag = false;
        if (null != strings && strings.length > 0) {
            for (int i = 0; i < strings.length; i++) {
                if (null == strings[i] || "".equals(strings[i].trim())) {
                    flag = true;
                    break;
                }
            }
        } else {
            flag = true;
        }
        return flag;
    }

    /** 
     * 使用java正则表达式去掉多余的.与0 
     * @param s 
     * @return  
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");// 去掉多余的0
            s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
        }
        return s;
    }

    public static String getMin(String... strs) {
        String r = null;
        for (String s : strs) {
            if (r == null) {
                r = s;
            } else {
                r = r.compareTo(s) > 0 ? s : r;
            }
        }
        return r;
    }

    public static String getMax(String... strs) {
        String r = null;
        for (String s : strs) {
            if (r == null) {
                r = s;
            } else {
                r = r.compareTo(s) > 0 ? r : s;
            }
        }
        return r;
    }

    public static String[] getMinMax(String... strs) {
        String min = null;
        String max = null;
        for (String s : strs) {
            if (min == null) {
                min = s;
            } else {
                min = min.compareTo(s) > 0 ? s : min;
            }
            if (max == null) {
                max = s;
            } else {
                max = max.compareTo(s) > 0 ? max : s;
            }
        }
        return new String[] {min, max };
    }
}
