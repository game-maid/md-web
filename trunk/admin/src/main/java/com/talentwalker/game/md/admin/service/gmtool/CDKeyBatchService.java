
package com.talentwalker.game.md.admin.service.gmtool;
/**
 * @ClassName: CDKeyBatchService
 * @Description: 批次
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年11月29日 下午5:37:36
 */

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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.admin.service.BaseService;
import com.talentwalker.game.md.core.domain.gameworld.CDKey;
import com.talentwalker.game.md.core.domain.gameworld.CDKeyBatch;
import com.talentwalker.game.md.core.domain.gameworld.CDKeyExcel;
import com.talentwalker.game.md.core.exception.ErrorCode;
import com.talentwalker.game.md.core.repository.gameworld.CDKeyBatchRepository;
import com.talentwalker.game.md.core.repository.gameworld.CDKeyRepository;
import com.talentwalker.game.md.core.util.ExportExcel;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.StringUtils;

import net.sf.json.JSONObject;

@Service
public class CDKeyBatchService extends BaseService {
    @Resource
    private CDKeyBatchRepository cdKeyBatchRepository;
    @Resource
    private CDKeyRepository cdKeyRepository;
    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * @Description:查询所有批次
     * @return
     * @throws
     */
    public List<CDKeyBatch> findAll() {
        return cdKeyBatchRepository.findAll();
    }

    /**
     * @Description:添加批次，生成兑换码
     * @param batch
     * @throws
     */
    public void add(CDKeyBatch batch) {
        CDKeyBatch cdKeyBatch = new CDKeyBatch();
        if (batch.getId().equals("0")) {// 新增批次
            // 校验时间
            long startTime = 0L;
            long endTime = 0L;
            try {
                startTime = DateUtils.parseDate(batch.getStartDate(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd").getTime();
                endTime = DateUtils.parseDate(batch.getEndDate(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd").getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println(startTime - endTime);
            System.out.println(startTime >= endTime);
            if (startTime >= endTime) {
                GameExceptionUtils.throwException(ErrorCode.FAIL_DEFAULT, getMessage("config.shop.recruit.time.error"));
            }
            // 校验数量
            if (batch.getNum() > CDKeyBatch.LIMIT_NUM) {
                GameExceptionUtils.throwException(ErrorCode.FAIL_DEFAULT, getMessage("gmtool.cdk.number.limit"));
            }
            // 奖励
            Map<String, Integer> reward = (Map<String, Integer>) JSONObject
                    .toBean(JSONObject.fromObject(batch.getRewards()), Map.class, new HashMap<String, Integer>());
            cdKeyBatch.setContent(batch.getContent());
            cdKeyBatch.setLevel(batch.getLevel());
            cdKeyBatch.setName(batch.getName());
            cdKeyBatch.setPackageIds(batch.getPackageIds());
            cdKeyBatch.setZoneIds(batch.getZoneIds());
            cdKeyBatch.setRank(batch.getRank());
            cdKeyBatch.setSender(batch.getSender());
            cdKeyBatch.setSenderHeadIcon(batch.getSenderHeadIcon());
            cdKeyBatch.setTitle(batch.getTitle());
            cdKeyBatch.setValidDay(batch.getValidDay());
            cdKeyBatch.setStartTime(startTime);
            cdKeyBatch.setEndTime(endTime);
            cdKeyBatch.setReward(reward);
            cdKeyBatch = cdKeyBatchRepository.save(cdKeyBatch);
        } else {
            // 校验同一批次的数量
            cdKeyBatch = cdKeyBatchRepository.findOne(batch.getId());
            Query query = new Query(Criteria.where("batch").is(cdKeyBatch));
            long count = mongoTemplate.count(query, CDKey.class);
            if (count + batch.getNum() > CDKeyBatch.LIMIT_NUM) {
                GameExceptionUtils.throwException(ErrorCode.FAIL_DEFAULT, getMessage("gmtool.cdk.number.limit"));
            }
        }
        // 生成兑换码
        Set<CDKey> set = new HashSet<>();
        for (int i = 0; i < batch.getNum(); i++) {
            CDKey cdKey = new CDKey();
            String id = StringUtils.getRandomLowerStr(6);
            cdKey.setId(id);
            cdKey.setStatus(CDKey.USING);
            cdKey.setBatch(cdKeyBatch);
            set.add(cdKey);
        }
        cdKeyRepository.save(set);
    }

    /**
     * @Description:根据条件查询，并作为excel文件导出
     * @param batchId
     * @param cdkId
     * @throws
     */
    public void exportCDKey(String batchId, String cdkId, HttpServletRequest request, HttpServletResponse response) {
        CDKey cdk = null;
        List<CDKey> list = new ArrayList<>();
        Query query = new Query();
        if (!batchId.isEmpty()) {
            query.addCriteria(Criteria.where("batch").is(cdKeyBatchRepository.findOne(batchId)));
        }
        if (!cdkId.isEmpty()) {
            query.addCriteria(Criteria.where("id").is(cdkId));
        }
        list = mongoTemplate.find(query, CDKey.class);
        // excel 文件内容
        int index = 1;
        List<CDKeyExcel> excelList = new ArrayList<>();
        for (CDKey cdKey : list) {
            CDKeyBatch batch = cdKey.getBatch();
            String status = "";
            if (cdKey.getStatus() == 0) {
                status = getMessage("gmtool.cdk.status.using");
            } else if (cdKey.getStatus() == 1) {
                status = getMessage("gmtool.cdk.status.used");
            } else {
                status = getMessage("gmtool.cdk.status.cancellation");
            }
            StringBuilder zoneIds = new StringBuilder();
            for (String zoneId : batch.getZoneIds()) {
                zoneIds.append(zoneId).append("-");
            }
            StringBuilder packageIds = new StringBuilder();
            for (String packageId : batch.getPackageIds()) {
                packageIds.append(packageId).append("-");
            }
            StringBuilder reward = new StringBuilder();
            Map<String, Integer> rewardMap = batch.getReward();
            Set<String> itemSet = rewardMap.keySet();
            for (String itemId : itemSet) {
                reward.append(itemId).append(":").append(rewardMap.get(itemId)).append("-");
            }
            Date useTime = null;
            if (cdKey.getUseTime() != 0) {
                useTime = new Date(cdKey.getUseTime());
            }
            CDKeyExcel cdKeyExcel = new CDKeyExcel(index++, cdKey.getId(), batch.getName(), batch.getLevel(),
                    batch.getRank(), status, new Date(batch.getStartTime()), new Date(batch.getEndTime()),
                    cdKey.getUseId(), useTime, zoneIds.toString(), packageIds.toString(), reward.toString());
            excelList.add(cdKeyExcel);
        }
        // excel 标题
        String[] headers = {getMessage("sys.sequence"), getMessage("gmtool.cdk.name"),
                getMessage("gmtool.cdk.batch.name"), getMessage("gmtool.cdk.level"),
                getMessage("gmtool.cdk.reward.rank"), getMessage("gmtool.cdk.status"), getMessage("sys.effect.time"),
                getMessage("sys.end.time"), getMessage("gmtool.cdk.user") + "ID", getMessage("gmtool.cdk.user.time"),
                getMessage("gamelog.gamezone"), getMessage("gamelog.package"), getMessage("gmtool.cdk.reward.item") };
        ExportExcel<CDKeyExcel> excel = new ExportExcel<>();
        String path = request.getServletContext().getRealPath("/");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String fileName = "CDK_" + sdf.format(System.currentTimeMillis()) + ".xls";
        // 在服务器生成excel文件
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path + System.getProperty("file.separator") + fileName);
            excel.exportExcel(headers, excelList, out, "yyy-MM-dd HH:mm:ss");
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
