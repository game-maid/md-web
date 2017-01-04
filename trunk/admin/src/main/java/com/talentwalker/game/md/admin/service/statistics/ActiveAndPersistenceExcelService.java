
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
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.talentwalker.game.md.admin.service.BaseService;
import com.talentwalker.game.md.core.domain.statistics.ActiveAndPersistenceBaseExcel;
import com.talentwalker.game.md.core.domain.statistics.ActiveAndPersistenceLtvExcel;
import com.talentwalker.game.md.core.domain.statistics.ActiveAndPersistencePayExcel;
import com.talentwalker.game.md.core.domain.statistics.ActiveAndPersistencePersistenceExcel;
import com.talentwalker.game.md.core.domain.statistics.ActiveBaseData;
import com.talentwalker.game.md.core.domain.statistics.ActiveBasePackage;
import com.talentwalker.game.md.core.service.statistics.ActiveAndPersistenceService;
import com.talentwalker.game.md.core.util.ExportExcel;

/**
 * @ClassName: ActiveAndPersistenceService
 * @Description: 活跃和存留 excel导出
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年12月21日 下午2:40:10
 */
@Service
public class ActiveAndPersistenceExcelService extends BaseService {

    @Resource
    private ActiveAndPersistenceService activeAndPersistenceService;

    /**
     * @Description:excel 导出
     * @param dateType
     * @param date
     * @param zoneArr
     * @throws
     */
    public void baseExport(String dateType, String dateStr, String[] zoneArr, Integer isDetail,
            HttpServletRequest request, HttpServletResponse response) {
        List<ActiveBaseData> list = activeAndPersistenceService.baseSelect(dateType, dateStr, zoneArr);
        // excel 内容
        List<ActiveAndPersistenceBaseExcel> excelList = new ArrayList<>();
        int index = 0;
        for (ActiveBaseData abd : list) {
            Map<String, Map<String, ActiveBasePackage>> zoneData = abd.getZoneData();
            Set<String> zoneDataKeySet = zoneData.keySet();
            for (String zoneId : zoneDataKeySet) {
                Map<String, ActiveBasePackage> packageData = zoneData.get(zoneId);
                Set<String> keySet = packageData.keySet();
                for (String packageId : keySet) {
                    if (isDetail == 0 && !packageId.equals(ActiveBaseData.DAY_TOTAL)) {
                        continue;
                    }
                    if (isDetail == 1 && !packageId.equals(ActiveBaseData.DAY_TOTAL)
                            && !packageId.equals(ActiveBaseData.ZONE_TOTAL)) {
                        continue;
                    }
                    ActiveBasePackage abp = packageData.get(packageId);
                    ActiveAndPersistenceBaseExcel aape = new ActiveAndPersistenceBaseExcel();
                    if (packageId.equals(ActiveBaseData.DAY_TOTAL)) {
                        zoneId = getMessage("statistics.total");
                        packageId = getMessage("statistics.total");
                    }
                    if (packageId.equals(ActiveBaseData.ZONE_TOTAL)) {
                        packageId = getMessage("statistics.zone.total");
                    }
                    aape.setIndex(index++);
                    aape.setDate(abd.getDate());
                    aape.setZoneId(zoneId);
                    aape.setPackageId(packageId);
                    aape.setActiveNum(checkIntNull(abp.getActiveNum()));
                    aape.setNewUserNum(checkIntNull(abp.getNewUserNum()));
                    aape.setNewPayerNum(checkIntNull(abp.getNewPayerNum()));
                    aape.setPayerNum(checkIntNull(abp.getPayerNum()));
                    aape.setIncomeNum(checkDoubleNull(abp.getIncomeNum()));
                    aape.setIncomeTimes(checkIntNull(abp.getIncomeTimes()));
                    aape.setNewPayRate(divide(abp.getNewPayerNum(), abp.getNewUserNum()) + "%");
                    aape.setActivePayRate(divide(abp.getPayerNum(), abp.getActiveNum()) + "%");
                    aape.setArpu(divide(abp.getIncomeNum(), abp.getActiveNum()));
                    aape.setArppu(divide(abp.getIncomeNum(), abp.getPayerNum()));
                    aape.setSecondPersistenceRate(divide(abp.getPreOnePersistence(), abp.getPreOneNewUserNum()) + "%");
                    aape.setThirdlyPersistenceRate(divide(abp.getPreTwoPersistence(), abp.getPreTwoNewUserNum()) + "%");
                    aape.setSeventhPersistenceRate(divide(abp.getPreSixPersistence(), abp.getPreSixNewUserNum()) + "%");
                    aape.setSecondLtv(divide(abp.getPreOneIncomeNum(), abp.getPreOneNewUserNum()) + "%");
                    aape.setThirdlyLtv(divide(abp.getPreTwoIncomeNum(), abp.getPreTwoNewUserNum()) + "%");
                    aape.setSeventhLtv(divide(abp.getPreSixIncomeNum(), abp.getPreTwoNewUserNum()) + "%");
                    excelList.add(aape);
                }
            }
        }
        // excel 头
        String[] header = {getMessage("sys.sequence"), getMessage("statistics.active.date"),
                getMessage("statistics.active.zone"), getMessage("statistics.active.pacakge"),
                getMessage("statistics.active.num"), getMessage("statistics.active.new.user.num"),
                getMessage("statistics.active.new.payer.num"), getMessage("statistics.active.payer.num"),
                getMessage("statistics.active.income.num"), getMessage("statistics.active.income.times"),
                getMessage("statistics.active.new.pay.rate"), getMessage("statistics.active.active.pay.rate"),
                getMessage("statistics.active.ARPU"), getMessage("statistics.active.ARPPU"),
                getMessage("statistics.active.second.persistence.rate"),
                getMessage("statistics.active.thirdly.persistence.rate"),
                getMessage("statistics.active.seventh.persistence.rate"), getMessage("statistics.active.second.LTV"),
                getMessage("statistics.active.thirdly.LTV"), getMessage("statistics.active.seventh.LTV") };

        ExportExcel<ActiveAndPersistenceBaseExcel> excel = new ExportExcel<>();
        String path = request.getServletContext().getRealPath("/");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String fileName = "ActiveAndPersistence_Base" + sdf.format(System.currentTimeMillis()) + ".xls";
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

    private int checkIntNull(Integer num) {
        if (num == null)
            return 0;
        return num;
    }

    private double checkDoubleNull(Double num) {
        if (num == null)
            return 0.00D;
        return num;
    }

    /**
     * @Description:除法 num1/num2
     * @param num1
     * @param num2
     * @return
     * @throws
     */
    private double divide(Double num1, Integer num2) {
        if (num1 == null || num1 == 0 || num2 == null || num2 == 0) {
            return 0.00D;
        }
        return (double) Math.round(num1 / num2 * 100) / 100;
    }

    private double divide(Integer num1, Integer num2) {
        if (num1 == null || num1 == 0 || num2 == null || num2 == 0) {
            return 0.00D;
        }
        return (double) Math.round((double) num1 / num2 * 100) / 100;
    }

    /**
     * @Description:详细付费数据 Excel 导出
     * @param dateType
     * @param date
     * @param zoneArr
     * @param isDetail
     * @param request
     * @param response
     * @throws
     */
    public void payExport(String dateType, String dateStr, String[] zoneArr, Integer isDetail,
            HttpServletRequest request, HttpServletResponse response) {
        List<ActiveBaseData> list = activeAndPersistenceService.paySelect(dateType, dateStr, zoneArr);
        // excel 内容
        List<ActiveAndPersistencePayExcel> excelList = new ArrayList<>();
        int index = 0;
        for (ActiveBaseData abd : list) {
            Map<String, Map<String, ActiveBasePackage>> zoneData = abd.getZoneData();
            Set<String> zoneDataKeySet = zoneData.keySet();
            for (String zoneId : zoneDataKeySet) {
                Map<String, ActiveBasePackage> packageData = zoneData.get(zoneId);
                Set<String> keySet = packageData.keySet();
                for (String packageId : keySet) {
                    if (isDetail == 0 && !packageId.equals(ActiveBaseData.DAY_TOTAL)) {
                        continue;
                    }
                    if (isDetail == 1 && !packageId.equals(ActiveBaseData.DAY_TOTAL)
                            && !packageId.equals(ActiveBaseData.ZONE_TOTAL)) {
                        continue;
                    }
                    ActiveBasePackage abp = packageData.get(packageId);
                    ActiveAndPersistencePayExcel aape = new ActiveAndPersistencePayExcel();
                    if (packageId.equals(ActiveBaseData.DAY_TOTAL)) {
                        zoneId = getMessage("statistics.total");
                        packageId = getMessage("statistics.total");
                    }
                    if (packageId.equals(ActiveBaseData.ZONE_TOTAL)) {
                        packageId = getMessage("statistics.zone.total");
                    }
                    aape.setIndex(index++);
                    aape.setDate(abd.getDate());
                    aape.setZoneId(zoneId);
                    aape.setPackageId(packageId);
                    aape.setNewUserPayer(checkIntNull(abp.getNewUserPayer()));
                    aape.setOldUserPayer(checkIntNull(abp.getNewPayerNum()) - checkIntNull(abp.getNewUserPayer()));
                    aape.setContinuePayer(checkIntNull(abp.getPayerNum()) - checkIntNull(abp.getNewPayerNum()));
                    aape.setNewUserPayerRate(divide(abp.getNewUserPayer(), abp.getNewUserNum()) + "%");
                    excelList.add(aape);
                }
            }
        }
        // excel 头
        String[] header = {getMessage("sys.sequence"), getMessage("statistics.active.date"),
                getMessage("statistics.active.zone"), getMessage("statistics.active.pacakge"),
                getMessage("statistics.active.new.user.payer"), getMessage("statistics.active.old.user.payer"),
                getMessage("statistics.active.continue.payer"), getMessage("statistics.active.new.user.payer.rate") };

        ExportExcel<ActiveAndPersistencePayExcel> excel = new ExportExcel<>();
        String path = request.getServletContext().getRealPath("/");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String fileName = "ActiveAndPersistence_Pay" + sdf.format(System.currentTimeMillis()) + ".xls";
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
     * @Description:详细存留数据 excel
     * @param dateType
     * @param date
     * @param zoneArr
     * @param isDetail
     * @param request
     * @param response
     * @throws
     */
    public void persistenceExport(String dateType, String dateStr, String[] zoneArr, Integer isDetail,
            HttpServletRequest request, HttpServletResponse response) {
        List<ActiveBaseData> list = activeAndPersistenceService.persistenceSelect(dateType, dateStr, zoneArr);
        // excel 内容
        List<ActiveAndPersistencePersistenceExcel> excelList = new ArrayList<>();
        int index = 0;
        for (ActiveBaseData abd : list) {
            Map<String, Map<String, ActiveBasePackage>> zoneData = abd.getZoneData();
            Set<String> zoneDataKeySet = zoneData.keySet();
            for (String zoneId : zoneDataKeySet) {
                Map<String, ActiveBasePackage> packageData = zoneData.get(zoneId);
                Set<String> keySet = packageData.keySet();
                for (String packageId : keySet) {
                    if (isDetail == 0 && !packageId.equals(ActiveBaseData.DAY_TOTAL)) {
                        continue;
                    }
                    if (isDetail == 1 && !packageId.equals(ActiveBaseData.DAY_TOTAL)
                            && !packageId.equals(ActiveBaseData.ZONE_TOTAL)) {
                        continue;
                    }
                    ActiveBasePackage abp = packageData.get(packageId);
                    ActiveAndPersistencePersistenceExcel aape = new ActiveAndPersistencePersistenceExcel();
                    if (packageId.equals(ActiveBaseData.DAY_TOTAL)) {
                        zoneId = getMessage("statistics.total");
                        packageId = getMessage("statistics.total");
                    }
                    if (packageId.equals(ActiveBaseData.ZONE_TOTAL)) {
                        packageId = getMessage("statistics.zone.total");
                    }
                    aape.setIndex(index++);
                    aape.setDate(abd.getDate());
                    aape.setZoneId(zoneId);
                    aape.setPackageId(packageId);
                    aape.setSecondPersistenceRate(divide(abp.getPreOnePersistence(), abp.getPreOneNewUserNum()) + "%");
                    aape.setThirdlyPersistenceRate(divide(abp.getPreTwoPersistence(), abp.getPreTwoNewUserNum()) + "%");
                    aape.setFourthPersistenceRate(
                            divide(abp.getPreThreePersistence(), abp.getPreThreeNewUserNum()) + "%");
                    aape.setFifthPersistenceRate(divide(abp.getPreFourPersistence(), abp.getPreFourNewUserNum()) + "%");
                    aape.setSixthPersistenceRate(divide(abp.getPreFivePersistence(), abp.getPreFiveNewUserNum()) + "%");
                    aape.setSeventhPersistenceRate(divide(abp.getPreSixPersistence(), abp.getPreSixNewUserNum()) + "%");
                    aape.setFifteenthPersistenceRate(
                            divide(abp.getPreFourteenPersistence(), abp.getPreFourNewUserNum()) + "%");
                    aape.setThirtiethPersistenceRate(
                            divide(abp.getPreTwentyNinePersistence(), abp.getPreTwentyNineNewUserNum()) + "%");
                    excelList.add(aape);
                }
            }
        }
        // excel 头
        String[] header = {getMessage("sys.sequence"), getMessage("statistics.active.date"),
                getMessage("statistics.active.zone"), getMessage("statistics.active.pacakge"),
                getMessage("statistics.active.second.persistence.rate"),
                getMessage("statistics.active.thirdly.persistence.rate"),
                getMessage("statistics.active.fourth.persistence.rate"),
                getMessage("statistics.active.fifth.persistence.rate"),
                getMessage("statistics.active.sixth.persistence.rate"),
                getMessage("statistics.active.seventh.persistence.rate"),
                getMessage("statistics.active.fifteenth.persistence.rate"),
                getMessage("statistics.active.thirtieth.persistence.rate") };

        ExportExcel<ActiveAndPersistencePersistenceExcel> excel = new ExportExcel<>();
        String path = request.getServletContext().getRealPath("/");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String fileName = "ActiveAndPersistence_Persistence" + sdf.format(System.currentTimeMillis()) + ".xls";
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
     * @Description:详细ltv excel导出
     * @param dateType
     * @param date
     * @param zoneArr
     * @param isDetail
     * @param request
     * @param response
     * @throws
     */
    public void ltvExport(String dateType, String dateStr, String[] zoneArr, Integer isDetail,
            HttpServletRequest request, HttpServletResponse response) {
        List<ActiveBaseData> list = activeAndPersistenceService.ltvSelect(dateType, dateStr, zoneArr);
        // excel 内容
        List<ActiveAndPersistenceLtvExcel> excelList = new ArrayList<>();
        int index = 0;
        for (ActiveBaseData abd : list) {
            Map<String, Map<String, ActiveBasePackage>> zoneData = abd.getZoneData();
            Set<String> zoneDataKeySet = zoneData.keySet();
            for (String zoneId : zoneDataKeySet) {
                Map<String, ActiveBasePackage> packageData = zoneData.get(zoneId);
                Set<String> keySet = packageData.keySet();
                for (String packageId : keySet) {
                    if (isDetail == 0 && !packageId.equals(ActiveBaseData.DAY_TOTAL)) {
                        continue;
                    }
                    if (isDetail == 1 && !packageId.equals(ActiveBaseData.DAY_TOTAL)
                            && !packageId.equals(ActiveBaseData.ZONE_TOTAL)) {
                        continue;
                    }
                    ActiveBasePackage abp = packageData.get(packageId);
                    ActiveAndPersistenceLtvExcel aape = new ActiveAndPersistenceLtvExcel();
                    if (packageId.equals(ActiveBaseData.DAY_TOTAL)) {
                        zoneId = getMessage("statistics.total");
                        packageId = getMessage("statistics.total");
                    }
                    if (packageId.equals(ActiveBaseData.ZONE_TOTAL)) {
                        packageId = getMessage("statistics.zone.total");
                    }
                    aape.setIndex(index++);
                    aape.setDate(abd.getDate());
                    aape.setZoneId(zoneId);
                    aape.setPackageId(packageId);
                    aape.setSecondLtv(divide(abp.getPreOneIncomeNum(), abp.getPreOneNewUserNum()) + "%");
                    aape.setThirdlyLtv(divide(abp.getPreTwoIncomeNum(), abp.getPreTwoNewUserNum()) + "%");
                    aape.setSeventhLtv(divide(abp.getPreSixIncomeNum(), abp.getPreSixNewUserNum()) + "%");
                    aape.setFourteenthLtv(divide(abp.getPreThirteenIncomeNum(), abp.getPreThirteenNewUserNum()) + "%");
                    aape.setTwentyFirstLtv(divide(abp.getPreTwentyIncomeNum(), abp.getPreTwentyNewUserNum()) + "%");
                    aape.setTwentyEighthLtv(
                            divide(abp.getPreTwentySevenIncomeNum(), abp.getPreTwentySevenNewUserNum()) + "%");
                    aape.setThirtyFifthLtv(
                            divide(abp.getPreThirtyFourIncomeNum(), abp.getPreThirtyFourNewUserNum()) + "%");
                    aape.setFortyNinthLtv(
                            divide(abp.getPreFortyEightIncomeNum(), abp.getPreFortyEightNewUserNum()) + "%");
                    aape.setFiftySixthLtv(
                            divide(abp.getPreFiftyFiveIncomeNum(), abp.getPreFiftyFiveNewUserNum()) + "%");
                    excelList.add(aape);
                }
            }
        }
        // excel 头
        String[] header = {getMessage("sys.sequence"), getMessage("statistics.active.date"),
                getMessage("statistics.active.zone"), getMessage("statistics.active.pacakge"),
                getMessage("statistics.active.second.LTV"), getMessage("statistics.active.thirdly.LTV"),
                getMessage("statistics.active.seventh.LTV"), getMessage("statistics.active.fourteenth.LTV"),
                getMessage("statistics.active.twenty.first.LTV"), getMessage("statistics.active.twenty.eighth.LTV"),
                getMessage("statistics.active.thirty.fifth.LTV"), getMessage("statistics.active.forty.ninth.LTV"),
                getMessage("statistics.active.fifty.sixth.LTV") };

        ExportExcel<ActiveAndPersistenceLtvExcel> excel = new ExportExcel<>();
        String path = request.getServletContext().getRealPath("/");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String fileName = "ActiveAndPersistence_LTV" + sdf.format(System.currentTimeMillis()) + ".xls";
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
