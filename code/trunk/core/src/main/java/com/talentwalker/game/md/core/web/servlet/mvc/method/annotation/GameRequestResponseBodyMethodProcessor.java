/**
 * @Title: Game.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月13日 闫昆
 */

package com.talentwalker.game.md.core.web.servlet.mvc.method.annotation;

import java.io.IOException;
import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import com.talentwalker.game.md.core.response.GameModel;
import com.talentwalker.game.md.core.response.GameResponseEntity;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: Game
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月13日 下午2:38:05
 */

public class GameRequestResponseBodyMethodProcessor extends RequestResponseBodyMethodProcessor {

    /**
     * 创建一个新的实例 GameRequestResponseBodyMethodProcessor.
     * 
     * @param converters
     */
    public GameRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters) {
        super(converters);
        // TODO Auto-generated constructor stub
    }

    /**
     * @Title: supportsReturnType
     * @Description:
     * @param returnType
     * @return
     * @see org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor#supportsReturnType(org.springframework.core.MethodParameter)
     */
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getMethodAnnotation(GameResponse.class) != null;
    }

    /**
     * @Title: handleReturnValue
     * @Description:
     * @param returnValue
     * @param returnType
     * @param mavContainer
     * @param webRequest
     * @throws IOException
     * @throws HttpMediaTypeNotAcceptableException
     * @throws HttpMessageNotWritableException
     * @see org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor#handleReturnValue(java.lang.Object,
     *      org.springframework.core.MethodParameter, org.springframework.web.method.support.ModelAndViewContainer,
     *      org.springframework.web.context.request.NativeWebRequest)
     */
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest)
            throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
        Class<?> paramType = returnType.getParameterType();
        if (!paramType.equals(GameResponseEntity.class)) {
            if (returnValue instanceof GameModel) {
                returnValue = ((GameModel) returnValue).getModel();
            }
            returnValue = new GameResponseEntity<Object>(returnValue);
        }
        ((GameResponseEntity) returnValue).setServerTime(System.currentTimeMillis());
        super.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }

}
