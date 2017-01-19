/**
 * @Title: TopUpStatisticsExcelService.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年1月12日  张福涛
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.admin.service.BaseService;
import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.dataconfig.IDataConfigManager;
import com.talentwalker.game.md.core.domain.GamePackage;
import com.talentwalker.game.md.core.domain.GameZone;
import com.talentwalker.game.md.core.domain.gameworld.Order;
import com.talentwalker.game.md.core.domain.statistics.TopUpStatisticsExcel;
import com.talentwalker.game.md.core.service.IGamePackageService;
import com.talentwalker.game.md.core.service.IGameZoneService;
import com.talentwalker.game.md.core.util.ExportExcel;

/**
 * @ClassName: TopUpStatisticsExcelService
 * @Description: Description of this class
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年1月12日 下午2:06:47
 */
@Service
public class TopUpStatisticsExcelService extends BaseService {

    @Resource
    private MongoTemplate mongoTemplate;
    @Resource(name = "gameZoneService")
    private IGameZoneService gameZoneService;

    @Resource(name = "applyPackageService")
    private IGamePackageService applyPackageService;
    @Resource
    private IDataConfigManager configManager;

    /**
     * @Description:excel 文件导出
     * @param packageId
     * @param orderState
     * @param itemType
     * @param zoneArr
     * @param startStr
     * @param endStr
     * @throws
     */
    public void export(String packageId, String orderState, String itemType, String[] zoneArr, String startStr,
            String endStr, HttpServletRequest request, HttpServletResponse response) {
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = DateUtils.parseDate(startStr, "yyyy-MM-dd HH:mm:ss");
            endDate = DateUtils.parseDate(endStr, "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("create_time").lte(endDate.getTime()).gte(startDate.getTime()));
        query.addCriteria(Criteria.where("zone_id").in(zoneArr));
        if (!StringUtils.isEmpty(packageId)) {
            query.addCriteria(Criteria.where("package_id").is(packageId));
        }
        if (!StringUtils.isEmpty(orderState)) {
            query.addCriteria(Criteria.where("state").is(orderState));
        }
        if ("1".equals(itemType)) {
            query.addCriteria(Criteria.where("product_type").is(itemType));
        } else if ("2".equals(itemType)) {
            query.addCriteria(Criteria.where("product_type").ne(1));
        }
        List<Order> list = mongoTemplate.find(query, Order.class);

        // 查询区服/包
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
        // 获取订单配置
        DataConfig dataConfig = configManager.getTest().get("cashShop_config");
        // excel 内容
        List<TopUpStatisticsExcel> excelList = new ArrayList<>();
        int index = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Order order : list) {
            TopUpStatisticsExcel excel = new TopUpStatisticsExcel();
            excel.setIndex(++index);
            excel.setOrderId(order.getId());
            excel.setDesc(dataConfig.get(order.getProductId()).getString("desp"));
            excel.setProductId(order.getProductId());
            excel.setNum(order.getQuantity());
            excel.setLordId(order.getLordId());
            excel.setPlatformId(order.getPlatformId());
            ;
            excel.setZoneName(zoneMap.get(order.getZoneId()));
            excel.setPackageName(packageMap.get(order.getPackageId()));
            excel.setPrice(order.getPrice());
            if (order.getState() == 0) {// 支付成功
                excel.setState(getMessage("statistics.top.up.order.state.success"));
            } else {// 未支付
                excel.setState(getMessage("statistics.top.up.order.state.ing"));
            }
            excel.setCreateTime(sdf.format(new Date(order.getCreateTime())));
            excel.setPayTime(sdf.format(new Date(order.getPayTime())));
            excel.setLordLevel(order.getLordLevel());
            excel.setLordVipLevel(order.getLordVipLevel());
            if (order.getProductType() == 1) {// 钻石
                excel.setProductType(getMessage("statistics.top.up.item.type.diamond"));
            } else {// 月卡
                excel.setProductType(getMessage("statistics.top.up.item.type.month.card"));
            }
            excel.setVipScore(order.getLordVipScore());
            excel.setOtherOrderId(order.getOrderId());
            excelList.add(excel);
        }
        // excel 头
        String[] header = {getMessage("sys.sequence"), getMessage("statistics.top.up.order"),
                getMessage("statistics.top.up.item.desc"), getMessage("statistics.top.up.item.ID"),
                getMessage("statistics.top.up.num"), getMessage("statistics.top.up.lord.id"),
                getMessage("statistics.top.up.platform.id"), getMessage("statistics.top.up.zone"),
                getMessage("statistics.top.up.package"), getMessage("statistics.top.up.price"),
                getMessage("statistics.top.up.order.state"), getMessage("statistics.top.up.create.time"),
                getMessage("statistics.top.up.pay.time"), getMessage("statistics.top.up.current.level"),
                getMessage("statistics.top.up.current.vip.level"), getMessage("statistics.top.up.item.type"),
                getMessage("statistics.top.up.current.vip.score"), getMessage("statistics.top.up.another.order.id") };
        ExportExcel<TopUpStatisticsExcel> excel = new ExportExcel<>();
        String path = request.getServletContext().getRealPath("/");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String fileName = "TopUpStatistics" + sdf1.format(System.currentTimeMillis()) + ".xls";
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
}
