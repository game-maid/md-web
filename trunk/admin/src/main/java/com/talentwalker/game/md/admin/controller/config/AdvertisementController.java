/**
 * @Title: AdvertisementController.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年3月2日  张福涛
 */

package com.talentwalker.game.md.admin.controller.config;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.core.domain.config.AdvertisementConfig;
import com.talentwalker.game.md.core.service.gameworld.AdvertisementService;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: AdvertisementController
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年3月2日 下午2:20:10
 */
@RequestMapping(value = "advertisement", method = RequestMethod.GET)
@Controller
public class AdvertisementController extends BaseController {

    @Resource
    private AdvertisementService advertisementService;

    /**
     * @Description:
     * @return
     * @throws
     */
    @RequestMapping(value = "list")
    public String list() {
        return "config/advertisement";
    }

    /**
     * @Description:
     * @return
     * @throws
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @GameResponse
    public Object add(AdvertisementConfig advertisementConfig) {
        advertisementService.add(advertisementConfig);
        return null;
    }

    /**
     * @Description:分页查询
     * @return
     * @throws
     */
    @RequestMapping(value = "findList", method = RequestMethod.POST)
    @GameResponse
    public Page<AdvertisementConfig> findList() {
        return advertisementService.findList();
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
        advertisementService.deleteById(id);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Object update(String pk, String name, String value) {
        advertisementService.update(pk, name, value);
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "stage", method = RequestMethod.POST)
    public Object stageConfig() {
        return advertisementService.stageConfig();
    }
}
