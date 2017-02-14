/**
 * @Title: SystemController.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月11日 闫昆
 */

package com.talentwalker.game.md.admin.controller.sys;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.LocaleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.talentwalker.game.md.admin.controller.BaseController;

/**
 * @ClassName: SystemController
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月11日 下午10:56:22
 */

@Controller
public class SystemController extends BaseController {

    @Autowired
    private SessionLocaleResolver resolver;

    @RequestMapping(value = "locale/{language}", method = RequestMethod.GET)
    public String locale(@PathVariable("language") String language, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            Locale locale = LocaleUtils.toLocale(language);
            resolver.setLocale(request, response, locale);
        } catch (Exception e) {
            System.out.println("设置失败了，但是我不想告诉你");
        }
        return redirect("main");
    }

}
