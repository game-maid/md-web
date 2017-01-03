
package com.talentwalker.game.md.admin.controller.statistics;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.admin.service.statistics.ActiveAndPersistenceExcelService;
import com.talentwalker.game.md.core.service.statistics.ActiveAndPersistenceService;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

/**
 * @ClassName: ActiveAndPersistenceController
 * @Description: 区服活跃和存留
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年12月20日 上午11:05:29
 */
@Controller
@RequestMapping("active")
public class ActiveAndPersistenceController extends BaseController {

    @Resource
    private ActiveAndPersistenceService activeAndPersistenceService;

    @Resource
    private ActiveAndPersistenceExcelService activeAndPersistenceExcelService;

    @RequestMapping(value = "base", method = RequestMethod.GET)
    public String base() {
        return "statistics/activeBase";
    }

    @RequestMapping(value = "pay", method = RequestMethod.GET)
    public String pay() {
        return "statistics/activePay";
    }

    @RequestMapping(value = "persistence", method = RequestMethod.GET)
    public String persistence() {
        return "statistics/activePersistence";
    }

    @RequestMapping(value = "ltv", method = RequestMethod.GET)
    public String ltv() {
        return "statistics/activeLTV";
    }

    /**
     * @Description:基础数据
     * @param dateType
     * @param date
     * @param zoneArr
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "base/select", method = RequestMethod.POST)
    public Object baseSelect(String dateType, String date, String[] zoneArr) {
        return activeAndPersistenceService.baseSelect(dateType, date, zoneArr);
    }

    /**
     * @Description:详细付费数据
     * @param dateType
     * @param date
     * @param zoneArr
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "pay/select", method = RequestMethod.POST)
    public Object paySelect(String dateType, String date, String[] zoneArr) {
        return activeAndPersistenceService.paySelect(dateType, date, zoneArr);
    }

    /**
     * @Description:详细存留数据
     * @param dateType
     * @param date
     * @param zoneArr
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "persistence/select", method = RequestMethod.POST)
    public Object persistenceSelect(String dateType, String date, String[] zoneArr) {
        return activeAndPersistenceService.persistenceSelect(dateType, date, zoneArr);
    }

    /**
     * @Description:详细LTV数据
     * @param dateType
     * @param date
     * @param zoneArr
     * @return
     * @throws
     */
    @GameResponse
    @RequestMapping(value = "ltv/select", method = RequestMethod.POST)
    public Object ltvSelect(String dateType, String date, String[] zoneArr) {
        return activeAndPersistenceService.ltvSelect(dateType, date, zoneArr);
    }

    @GameResponse
    @RequestMapping(value = "base/export", method = RequestMethod.GET)
    public Object exportBase(String dateType, String date, String[] zoneArr, Integer isDetail,
            HttpServletRequest request, HttpServletResponse response) {
        activeAndPersistenceExcelService.baseExport(dateType, date, zoneArr, isDetail, request, response);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "pay/export", method = RequestMethod.GET)
    public Object exportPay(String dateType, String date, String[] zoneArr, Integer isDetail,
            HttpServletRequest request, HttpServletResponse response) {
        activeAndPersistenceExcelService.payExport(dateType, date, zoneArr, isDetail, request, response);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "persistence/export", method = RequestMethod.GET)
    public Object exportPersistence(String dateType, String date, String[] zoneArr, Integer isDetail,
            HttpServletRequest request, HttpServletResponse response) {
        activeAndPersistenceExcelService.persistenceExport(dateType, date, zoneArr, isDetail, request, response);
        return null;
    }

    @GameResponse
    @RequestMapping(value = "ltv/export", method = RequestMethod.GET)
    public Object exportLtv(String dateType, String date, String[] zoneArr, Integer isDetail,
            HttpServletRequest request, HttpServletResponse response) {
        activeAndPersistenceExcelService.ltvExport(dateType, date, zoneArr, isDetail, request, response);
        return null;
    }
}
