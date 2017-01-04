
package com.talentwalker.game.md.admin.controller.gmtool;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.form.MailSendForm;
import com.talentwalker.game.md.admin.service.gmtool.FeedbackMailService;
import com.talentwalker.game.md.core.domain.gameworld.Feedback;
import com.talentwalker.game.md.core.service.gameworld.FeedbackService;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: FeedbackController
 * @Description: 客服反馈
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年11月23日 下午3:33:51
 */
@Controller
@RequestMapping("feedback")
public class FeedbackController extends BaseController {
    @Resource
    private FeedbackService feedbackService;

    @Resource
    private FeedbackMailService feedbackMailService;

    @RequestMapping(value = "feedback", method = RequestMethod.GET)
    public String forward() {
        return "gmtool/feedback";
    }

    @GameResponse
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Page<Feedback> list() {
        return feedbackService.findAll();
    }

    /**
     * @Description:
     * @param id ：玩家反馈的Id
     * @param serviceId :客服Id
     * @param icon ：邮件发送头像
     * @param validDay ：有效天数
     * @param title ：邮件标题
     * @param content ：邮件内容（客服反馈内容）
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "reply", method = RequestMethod.POST)
    public Object reply(MailSendForm mailForm, String feedbackId) {
        feedbackMailService.send(mailForm, feedbackId);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Object delete(String id) {
        feedbackService.deleteById(id);
        return null;
    }

}
