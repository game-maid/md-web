/**
 * @Title: GameExceptionUtils.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月16日  占志灵
 */

package com.talentwalker.game.md.core.util;

import java.util.Properties;

import com.talentwalker.game.md.core.exception.GameException;

/**
 * @ClassName: GameExceptionUtils
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年5月16日 下午4:48:52
 */

public class GameExceptionUtils {
    private static Properties message = new Properties();

    public static void throwException(Integer errCode) throws GameException {
        throw new GameException(errCode, message.getProperty(errCode + ""));
    }

    public static void throwException(String message) throws GameException {
        throw new GameException(message);
    }

    public static void throwException(Integer errCode, String message) throws GameException {
        throw new GameException(errCode, message);
    }

    public static void throwException(Integer errCode, Throwable rootCause) throws GameException {
        throw new GameException(errCode, rootCause);
    }

    public static void throwException(Integer errCode, String message, Throwable rootCause) throws GameException {
        throw new GameException(errCode, message, rootCause);
    }

    public static void setMessage(Properties message) {
        GameExceptionUtils.message = message;
    }
}
