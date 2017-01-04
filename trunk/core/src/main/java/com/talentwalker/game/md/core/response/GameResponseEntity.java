/**
 * @Title: ResponseEntity.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月16日  占志灵
 */

package com.talentwalker.game.md.core.response;

import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.talentwalker.game.md.core.exception.ErrorCode;

/**
 * @ClassName: ResponseEntity
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年5月16日 下午2:25:18
 */

public class GameResponseEntity<T> {
    protected static ObjectMapper mapper = new ObjectMapper();

    public static final GameResponseEntity<?> EMPTY = new GameResponseEntity<Object>();

    public static final Integer DEFAULT_CODE = ErrorCode.SUCCESS;

    private Integer code = DEFAULT_CODE;

    private Long serverTime;

    private final String message;

    private final T body;

    protected GameResponseEntity() {
        this(DEFAULT_CODE, null, null);
    }

    public GameResponseEntity(Integer code) {
        this(code, null, null);
    }

    public GameResponseEntity(String message) {
        this(DEFAULT_CODE, message, null);
    }

    public GameResponseEntity(T body) {
        this(DEFAULT_CODE, null, body);
    }

    public GameResponseEntity(Integer code, T body) {
        this(code, null, body);
    }

    public GameResponseEntity(Integer code, String message) {
        this(code, message, null);
    }

    public GameResponseEntity(String message, T body) {
        this(DEFAULT_CODE, message, body);
    }

    public GameResponseEntity(Integer code, String message, T body) {
        this.code = code;
        this.message = message;
        this.body = body;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public T getBody() {
        return this.body;
    }

    /**
     * Indicates whether this entity has a body.
     */
    public boolean hasBody() {
        return (this.body != null);
    }

    public Long getServerTime() {
        return serverTime;
    }

    public void setServerTime(Long serverTime) {
        this.serverTime = serverTime;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        GameResponseEntity<?> otherEntity = (GameResponseEntity<?>) other;
        return (ObjectUtils.nullSafeEquals(this.code, otherEntity.code)
                && ObjectUtils.nullSafeEquals(this.message, otherEntity.message)
                && ObjectUtils.nullSafeEquals(this.body, otherEntity.body));
    }

    @Override
    public int hashCode() {
        return (ObjectUtils.nullSafeHashCode(this.code) * 29 + ObjectUtils.nullSafeHashCode(this.message) * 40
                + ObjectUtils.nullSafeHashCode(this.body));
    }

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
