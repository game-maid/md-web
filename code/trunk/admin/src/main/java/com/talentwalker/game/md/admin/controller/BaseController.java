/**
 * @Title: BaseController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年4月27日  闫昆
 */
 
package com.talentwalker.game.md.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @ClassName: BaseController
 * @Description: Controller父类，提供一些公共方法
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年4月27日 下午1:47:28
 */

public abstract class BaseController {

    private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private MessageSource messageSource;

    /**
     * 重定向
     * @param url
     * @return
     */
    protected String redirect(String url) {
        if (!url.startsWith("/") && !url.startsWith("http")) {
            url = "/" + url;
        }
        return "redirect:" + url;
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
