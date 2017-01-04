/**
 * @Title: ItemSend.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月15日  赵丽宝
 */

package com.talentwalker.game.md.admin.controller.gmtool;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.domain.log.Log;
import com.talentwalker.game.md.admin.service.gmtool.ItemSendService;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: ItemSend
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年6月15日 上午11:58:14
 */
@Controller
@RequestMapping(value = "itemSend")
public class ItemSendController extends BaseController {
    @Autowired
    private ItemSendService itemSendService;

    @RequestMapping(value = "send", method = RequestMethod.GET)
    public String forward(Model model) {
        return "gmtool/itemSend";
    }

    @GameResponse
    @RequestMapping(value = "item", method = RequestMethod.GET)
    public List<Map<String, String>> findItem() {
        return itemSendService.findItem();
    }

    @GameResponse
    @RequestMapping(value = "lord", method = RequestMethod.POST)
    public Lord findLord(String id) {
        return itemSendService.findLord(id);
    }

    @GameResponse
    @RequestMapping(value = "send", method = RequestMethod.POST)
    public Object send(String items, String lord, String comment) {
        itemSendService.send(items, lord, comment);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Page<Log> list() {
        return itemSendService.findAll();
    }
}
