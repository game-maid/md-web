/**
 * @Title: PackageController.java
 * @Copyright (C) 2016 太能沃可
 * @Description: 应用包管理
 * @Revision History:
 * @Revision 1.0 2016年5月24日  赵丽宝
 */

package com.talentwalker.game.md.admin.controller.gameconfig;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.talentwalker.game.md.core.domain.GamePackage;
import com.talentwalker.game.md.core.service.IGamePackageService;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: PackageController
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年5月24日 下午4:14:33
 */
@Controller
@RequestMapping(value = "package")
public class ApplyPackageController {

    @Resource(name = "applyPackageService")
    private IGamePackageService applyPackageService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String forward() {
        return "gameconfig/applyPackage";
    }

    @GameResponse
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public List<GamePackage> list() {
        return applyPackageService.findAll();
    }

    @GameResponse
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Object add(@Valid GamePackage applyPackage) {
        applyPackageService.save(applyPackage);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Object update(String name, String value, String pk) throws Exception {
        applyPackageService.update(name, value, pk);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "updataAll", method = RequestMethod.POST)
    public Object updata(@Valid GamePackage applyPackage) {
        applyPackageService.updata(applyPackage);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "updataWhite", method = RequestMethod.POST)
    public Object updataWhite(@RequestParam("whiteUidList") String whiteUidList, String id) {
        applyPackageService.updataWhite(id, whiteUidList);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "updataConfigSet", method = RequestMethod.POST)
    public Object updataConfigSet(@RequestParam("configs") String configs, String id) {
        applyPackageService.updataConfigSet(id, configs);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "deleteOneUid", method = RequestMethod.POST)
    public Object deleteOneUid(String uid, String id) {
        applyPackageService.deleteOneUid(id, uid);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "deleteOneConfig", method = RequestMethod.POST)
    public Object deleteOneConfig(String version, String id) {
        applyPackageService.deleteOneVersion(id, version);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Object delete(String id) {
        applyPackageService.delete(id);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "findOne", method = RequestMethod.POST)
    public GamePackage findOne(String id) {
        return applyPackageService.findOne(id);
    }

}
