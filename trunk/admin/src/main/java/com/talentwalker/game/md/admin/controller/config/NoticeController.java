
/**
 * @Title: NoticeController.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年3月2日  张福涛
 */

package com.talentwalker.game.md.admin.controller.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.service.config.NoticeService;
import com.talentwalker.game.md.core.domain.config.NoticeConfig;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: NoticeController
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年3月2日 下午2:19:37
 */
@Controller
@RequestMapping(value = "notice", method = RequestMethod.GET)
public class NoticeController extends BaseController {

    @Autowired
    private NoticeService noticeService;

    /**
     * @Description:页面跳转
     * @return
     * @throws
     */
    @RequestMapping(value = "list")
    public String list() {
        return "config/notice";
    }

    /**
     * @Description:
     * @return
     * @throws
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @GameResponse
    public Object add(NoticeConfig noticeConfig) {
        noticeService.add(noticeConfig);
        return null;
    }

    /**
     * @Description:分页查询
     * @return
     * @throws
     */
    @RequestMapping(value = "findList", method = RequestMethod.POST)
    @GameResponse
    public Page<NoticeConfig> findList() {
        return noticeService.findList();
    }

    /**
     * @Description:根据id删除
     * @param id
     * @return
     * @throws
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @GameResponse
    public Object deleteById(String id) {
        noticeService.deleteById(id);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Object update(String pk, String name, String value) {
        noticeService.update(pk, name, value);
        return null;
    }
}
