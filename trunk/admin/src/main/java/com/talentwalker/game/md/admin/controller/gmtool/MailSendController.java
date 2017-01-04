/**
 * @Title: MailSendController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月19日  赵丽宝
 */

package com.talentwalker.game.md.admin.controller.gmtool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.form.MailSendForm;
import com.talentwalker.game.md.admin.service.gmtool.MailSendService;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: MailSendController
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月19日 下午12:37:38
 */
@Controller
@RequestMapping(value = "mailSend")
public class MailSendController extends BaseController {
    @Autowired
    private MailSendService mailSendService;

    @RequestMapping(value = "send", method = RequestMethod.GET)
    public String forward(Model model) {
        return "gmtool/mailSend";
    }

    @GameResponse
    @RequestMapping(value = "send", method = RequestMethod.POST)
    public Object send(MailSendForm mailSendForm) {
        mailSendService.send(mailSendForm);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Page<MailSendForm> list() {
        return mailSendService.findAll();
    }

    @GameResponse
    @RequestMapping(value = "cancel", method = RequestMethod.POST)
    public Object cancel(String id) {
        mailSendService.cancel(id);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Object delete(String id) {
        mailSendService.delete(id);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "findByIds", method = RequestMethod.POST)
    public Object findByIds(@RequestParam(value = "ids[]") String[] ids) {
        return mailSendService.findByIds(ids);
    }
}
