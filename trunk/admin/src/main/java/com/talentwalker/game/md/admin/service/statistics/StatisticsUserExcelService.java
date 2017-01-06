/**
 * @Title: StatisticsUserExcelService.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年1月3日  张福涛
 */

package com.talentwalker.game.md.admin.service.statistics;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.talentwalker.game.md.admin.service.BaseService;
import com.talentwalker.game.md.core.domain.GamePackage;
import com.talentwalker.game.md.core.domain.GameZone;
import com.talentwalker.game.md.core.domain.statistics.Register;
import com.talentwalker.game.md.core.domain.statistics.UserNewExcel;
import com.talentwalker.game.md.core.domain.statistics.UserTotalExcel;
import com.talentwalker.game.md.core.service.IGamePackageService;
import com.talentwalker.game.md.core.service.IGameZoneService;
import com.talentwalker.game.md.core.service.statistics.UserStatisticsService;
import com.talentwalker.game.md.core.util.ExportExcel;

/**
 * @ClassName: StatisticsUserExcelService
 * @Description: 数据报表用户数，excel导出
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年1月3日 下午5:41:16
 */
@Service
public class StatisticsUserExcelService extends BaseService {
    @Resource
    private UserStatisticsService userStatisticsService;
    @Resource(name = "gameZoneService")
    private IGameZoneService gameZoneService;

    @Resource(name = "applyPackageService")
    private IGamePackageService applyPackageService;

    /**
    * @Description:总用户数导出
    * @param zoneArr
    * @throws
    */
    public void totalExport(String[] zoneArr, HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Object>> list = userStatisticsService.totalSelect(zoneArr);
        List<GameZone> zoneList = gameZoneService.findAll();
        Map<String, String> zoneMap = new HashMap<>();
        for (GameZone gameZone : zoneList) {
            zoneMap.put(gameZone.getId(), gameZone.getName());
        }
        List<GamePackage> packageList = applyPackageService.findAll();
        Map<String, String> packageMap = new HashMap<>();
        for (GamePackage gamePackage : packageList) {
            packageMap.put(gamePackage.getId(), gamePackage.getName());
        }
        // excel 内容
        List<UserTotalExcel> excelList = new ArrayList<>();
        int index = 0;
        for (Map<String, Object> map : list) {
            String zoneId = (String) map.get("zoneId");
            Integer zoneTotal = (Integer) map.get("zoneTotal");
            Map<String, Integer> packageData = (Map<String, Integer>) map.get("package");
            Set<Entry<String, Integer>> entrySet = packageData.entrySet();
            for (Entry<String, Integer> entry : entrySet) {
                UserTotalExcel userTotalExcel = new UserTotalExcel();
                String packageId = entry.getKey();
                Integer packageTotal = entry.getValue();
                userTotalExcel.setIndex(index++);
                userTotalExcel.setPackageId(packageId);
                userTotalExcel.setPackageTotal(packageTotal);
                userTotalExcel.setZoneId(zoneId);
                userTotalExcel.setZoneTotal(zoneTotal);
                userTotalExcel.setPackageName(packageMap.get(packageId));
                userTotalExcel.setZoneName(zoneMap.get(zoneId));
                excelList.add(userTotalExcel);
            }
        }
        // excel 头
        String[] header = {getMessage("sys.sequence"), getMessage("statistics.user.zone.id"),
                getMessage("statistics.user.zone.name"), getMessage("statistics.user.zone.total.num"),
                getMessage("statistics.user.package.id"), getMessage("statistics.user.package.name"),
                getMessage("statistics.user.package.num") };

        ExportExcel<UserTotalExcel> excel = new ExportExcel<>();
        String path = request.getServletContext().getRealPath("/");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String fileName = "User_total" + sdf.format(System.currentTimeMillis()) + ".xls";
        // 在服务器生成excel文件
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path + System.getProperty("file.separator") + fileName);
            excel.exportExcel(header, excelList, out, "yyy-MM-dd HH:mm:ss");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        download(path + fileName, response);
    }

    /**
     * @Description:下载文件
     * @param path 要下载的文件路径
     * @param response
     * @throws
     */
    private void download(String path, HttpServletResponse response) {
        File file = new File(path);
        String fileName = file.getName();
        InputStream is = null;
        OutputStream os = null;
        try {
            // 读文件
            is = new BufferedInputStream(new FileInputStream(path));
            byte[] bufferByte = new byte[is.available()];
            is.read(bufferByte);
            // 清空response
            response.reset();
            // 设置响应头
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Content-Length", file.length() + "");
            // 写文件
            os = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            os.write(bufferByte);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {// 关闭资源
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @Description:导出新增用户
     * @param date
     * @param zoneId
     * @throws
     */
    public void newExport(String date, String zoneId, HttpServletRequest request, HttpServletResponse response) {
        List<Register> list = userStatisticsService.newSelect(date, zoneId);
        List<GameZone> zoneList = gameZoneService.findAll();
        Map<String, String> zoneMap = new HashMap<>();
        for (GameZone gameZone : zoneList) {
            zoneMap.put(gameZone.getId(), gameZone.getName());
        }
        List<GamePackage> packageList = applyPackageService.findAll();
        Map<String, String> packageMap = new HashMap<>();
        for (GamePackage gamePackage : packageList) {
            packageMap.put(gamePackage.getId(), gamePackage.getName());
        }
        // excel 内容
        List<UserNewExcel> excelList = new ArrayList<>();
        int index = 1;
        for (Register register : list) {
            UserNewExcel userNewExcel = new UserNewExcel();
            userNewExcel.setIndex(index++);
            userNewExcel.setZoneMes(register.getZoneId() + "-" + zoneMap.get(register.getZoneId()));
            userNewExcel.setPackageMes(register.getPackageId() + "-" + packageMap.get(register.getPackageId()));
            userNewExcel.setLordId(register.getId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            userNewExcel.setRegisterTime(sdf.format(new Date(register.getRegisterTime())));
            excelList.add(userNewExcel);
        }
        // excel 头
        String[] header = {getMessage("sys.sequence"), getMessage("gamelog.gamezone"),
                getMessage("statistics.active.pacakge"), getMessage("gamelog.player"),
                getMessage("gmtool.lord.registerTime") };

        ExportExcel<UserNewExcel> excel = new ExportExcel<>();
        String path = request.getServletContext().getRealPath("/");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String fileName = "User_new" + sdf.format(System.currentTimeMillis()) + ".xls";
        // 在服务器生成excel文件
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path + System.getProperty("file.separator") + fileName);
            excel.exportExcel(header, excelList, out, "yyy-MM-dd HH:mm:ss");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        download(path + fileName, response);

    }
}
