/**
 * @Title: PlatformController.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月24日 闫昆
 */

package com.talentwalker.game.md.admin.controller.gameconfig;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.core.domain.Platform;
import com.talentwalker.game.md.core.service.IPlatformService;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: PlatformController
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月24日 下午4:09:32
 */

@Controller
@RequestMapping("platform")
public class PlatformController extends BaseController {

    @Resource(name = "platformService")
    private IPlatformService platformService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String forword() {
        return "gameconfig/platform";
    }

    @GameResponse
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public List<Platform> list() {
        return platformService.findAll();
    }

    @GameResponse
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Object save(@Valid Platform platform) {
        platformService.save(platform);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Object update(@Valid Platform platform) {
        platformService.update(platform);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "view", method = RequestMethod.POST)
    public List<Platform> view() {
        return platformService.showView();
    }

    @GameResponse
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Object delete(String id) {
        try {
            platformService.delete(id);
        } catch (Exception e) {
            GameExceptionUtils.throwException(getMessage(e.getMessage()));
        }
        return null;
    }

    @GameResponse
    @RequestMapping(value = "findOne", method = RequestMethod.POST)
    public Platform findOne(String id) {
        return platformService.findOne(id);
    }

}
