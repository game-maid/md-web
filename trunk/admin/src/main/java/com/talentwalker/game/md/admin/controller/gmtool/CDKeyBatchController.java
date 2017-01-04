
package com.talentwalker.game.md.admin.controller.gmtool;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.service.gmtool.CDKeyBatchService;
import com.talentwalker.game.md.core.domain.gameworld.CDKeyBatch;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: CDKeyBatchController
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年11月29日 下午5:35:46
 */
@Controller
@RequestMapping(value = "cDKeyBatch", method = RequestMethod.POST)
public class CDKeyBatchController extends BaseController {
    @Resource
    private CDKeyBatchService batchService;

    @GameResponse
    @RequestMapping(value = "list")
    public List<CDKeyBatch> list() {
        return batchService.findAll();
    }

    /**
     * @Description:生成CDK兑换码
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping("add")
    public Object add(CDKeyBatch batch) {
        batchService.add(batch);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "exportCDKey", method = RequestMethod.GET)
    public Object exportCDKey(String batchId, String cdkId, HttpServletRequest request, HttpServletResponse response) {
        batchService.exportCDKey(batchId, cdkId, request, response);
        return null;
    }
}
