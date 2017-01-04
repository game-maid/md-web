/**
 * @Title: DefaultHandlerExceptionResolver.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月16日  占志灵
 */

package com.talentwalker.game.md.core.web.servlet.mvc.support;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import com.talentwalker.game.md.core.exception.GameException;
import com.talentwalker.game.md.core.response.GameResponseEntity;

/**
 * @ClassName: DefaultHandlerExceptionResolver
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年5月16日 下午4:16:37
 */

public class GameDefaultHandlerExceptionResolver extends DefaultHandlerExceptionResolver {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected ModelAndView doResolveExceptionCustom(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) {
        return null;
    }

    /**
     * .
     * <p>
     * Title: doResolveException
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return
     * @see org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver#doResolveException(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        ModelAndView model = doResolveExceptionCustom(request, response, handler, ex);
        if (model == null) {
            model = new ModelAndView();
            GameResponseEntity<Object> gameResponseEntity = null;
            response.setCharacterEncoding("UTF-8");
            if (ex instanceof GameException) {
                GameException gameException = (GameException) ex;
                gameResponseEntity = new GameResponseEntity<>(gameException.getErrCode(), ex.getMessage());
            } else {
                gameResponseEntity = new GameResponseEntity<>(9999, ex.getMessage());
            }
            try {
                PrintWriter pw = response.getWriter();
                pw.write(gameResponseEntity.toString());
                pw.flush();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        return model;
    }

    /**
     * .
     * <p>
     * Title: logException
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param ex
     * @param request
     * @see org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver#logException(java.lang.Exception,
     *      javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected void logException(Exception ex, HttpServletRequest request) {
        logger.error(ex.getMessage(), ex);
    }
}
