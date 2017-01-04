
package com.talentwalker.game.md.application.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.core.service.gameworld.CDKeyService;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: CDKeyController
 * @Description: 兑换码
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年11月28日 下午5:40:16
 */
@Controller
@RequestMapping(method = RequestMethod.POST)
public class CDKeyController extends GameSupport {
    @Resource
    private CDKeyService cdKeyService;

    /**
     * @Description:使用兑换码兑换
     * @param key ： 兑换码
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("game/cdKey/exchange/{key}")
    public Object exchange(@PathVariable String key) {
        cdKeyService.exchange(key);
        return this.gameModel;
    }
}
