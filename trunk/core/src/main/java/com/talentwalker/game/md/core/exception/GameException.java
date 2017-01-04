/**
 * @Title: GameException.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月16日  占志灵
 */

package com.talentwalker.game.md.core.exception;

/**
 * @ClassName: GameException
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年5月16日 下午4:29:30
 */

public class GameException extends RuntimeException {

    /**
     * @Fields serialVersionUID : Description
     */

    private static final long serialVersionUID = 5208080261434053500L;

    public static final Integer DEFAULT_ERRCODE = ErrorCode.FAIL_DEFAULT;

    protected Integer errCode = DEFAULT_ERRCODE;

    public GameException() {
        super();
    }

    public GameException(Integer errCode) {
        super();
        this.errCode = errCode;
    }

    public GameException(String message) {
        super(message);
    }

    public GameException(Throwable rootCause) {
        super(rootCause);
    }

    public GameException(Integer errCode, String message) {
        super(message);
        this.errCode = errCode;
    }

    public GameException(Integer errCode, Throwable rootCause) {
        super(rootCause);
        this.errCode = errCode;
    }

    public GameException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    public GameException(Integer errCode, String message, Throwable rootCause) {
        super(message, rootCause);
        this.errCode = errCode;
    }

    public Throwable getRootCause() {
        return getCause();
    }

    public Integer getErrCode() {
        return this.errCode;
    }
}
