/**
 * @Title: ExceptionHandler.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月10日 闫昆
 */

package com.talentwalker.game.md.admin.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.shiro.ShiroException;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;

import com.talentwalker.game.md.admin.exception.AdminErrorCode;
import com.talentwalker.game.md.core.response.GameResponseEntity;
import com.talentwalker.game.md.core.util.ServletUtils;
import com.talentwalker.game.md.core.web.servlet.mvc.support.GameDefaultHandlerExceptionResolver;

/**
 * @ClassName: ExceptionHandler
 * @Description: 全局异常处理
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月10日 下午3:55:41
 */

public class AdminHandlerExceptionResolver extends GameDefaultHandlerExceptionResolver {

    /**
     * .
     * <p>
     * Title: doResolveExceptionCustom
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
     * @see com.talentwalker.game.framework.web.servlet.mvc.support.GameDefaultHandlerExceptionResolver#doResolveExceptionCustom(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    protected ModelAndView doResolveExceptionCustom(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) {
        if (ServletUtils.isAjaxRequest(request)) {
            return ajaxException(request, response, ex);
        } else {
            return customException(request, response, ex);
        }
    }

    private ModelAndView ajaxException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        ModelAndView model = new ModelAndView();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setHeader("Cache-Control", "no-store");
        GameResponseEntity<Object> gameResponseEntity = null;
        if (ex instanceof ShiroException) {
            gameResponseEntity = new GameResponseEntity<>(AdminErrorCode.FAIL_DEFAULT, "权限不足，无法操作");
        } else {
            gameResponseEntity = new GameResponseEntity<>(AdminErrorCode.FAIL_DEFAULT, ex.getMessage());
        }
        try {
            PrintWriter pw = response.getWriter();
            pw.write(gameResponseEntity.toString());
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.clear();
        return model;
    }

    private ModelAndView customException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        ModelAndView model = new ModelAndView("sys/500");
        model.addObject("error", ExceptionUtils.getStackTrace(ex));
        return model;
    }

}
