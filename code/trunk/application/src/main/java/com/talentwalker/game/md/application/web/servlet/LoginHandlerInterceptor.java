/**
 * @Title: LoginHandlerInterceptor.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月13日  占志灵
 */

package com.talentwalker.game.md.application.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.talentwalker.game.md.core.domain.GameUser;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.service.IGameUserServiceRemote;
import com.talentwalker.game.md.core.util.GameExceptionUtils;

/**
 * @ClassName: LoginHandlerInterceptor
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月13日 上午11:23:03
 */

public class LoginHandlerInterceptor extends HandlerInterceptorAdapter {

    private IGameUserServiceRemote gameUserServiceRemote;

    public LoginHandlerInterceptor(IGameUserServiceRemote gameUserServiceRemote) {
        this.gameUserServiceRemote = gameUserServiceRemote;
    }

    /**.
     * <p>Title: preHandle</p>
     * <p>Description: </p>
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI();
        if (uri != null && uri.startsWith("/remote")) {
            return true;
        }
        String sessionId = request.getHeader("sessionId");
        if (sessionId == null) {
            GameExceptionUtils.throwException(GameErrorCode.PORTAL_ERROR_10004);
        }
        GameUser gameUser = gameUserServiceRemote.findGameUserCache(sessionId);
        if (gameUser == null) {
            GameExceptionUtils.throwException(GameErrorCode.PORTAL_ERROR_10005);
        }
        return true;
    }
}
