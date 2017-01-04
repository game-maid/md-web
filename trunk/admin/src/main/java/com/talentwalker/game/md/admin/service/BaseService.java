/**
 * @Title: BaseService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年4月28日  闫昆
 */
 
package com.talentwalker.game.md.admin.service;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;


/**
 * @ClassName: BaseService
 * @Description: Service父类
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年4月28日 下午3:57:22
 */

public abstract class BaseService {

    private final static Logger logger = LoggerFactory.getLogger(BaseService.class);

    @Autowired
    private MessageSource messageSource;

    /**
     * 创建PageRequest对象
     * @param request
     * @return
     */
    protected PageRequest buildPageRequest(HttpServletRequest request) {
        int offset = Integer.parseInt(request.getParameter("offset"));
        int size = Integer.parseInt(request.getParameter("limit"));
        int page = offset / size;
        return new PageRequest(page, size);
    }

    /**
     * 获取国际化文字
     * @param code
     */
    protected String getMessage(String code, Object... objs) {
        try {
            return messageSource.getMessage(code, objs, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            logger.error("No message found under code '{}' for locale '{}'.", code, LocaleContextHolder.getLocale());
            return code;
        }
    }

}
