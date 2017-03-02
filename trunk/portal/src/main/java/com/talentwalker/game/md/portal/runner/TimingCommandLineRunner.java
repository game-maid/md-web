/**
 * @Title: Test.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年9月14日  赵丽宝
 */

package com.talentwalker.game.md.portal.runner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.talentwalker.game.md.core.dataconfig.IDataConfigManager;
import com.talentwalker.game.md.core.domain.DataZone;
import com.talentwalker.game.md.core.repository.DataZoneRepository;
import com.talentwalker.game.md.core.repository.gameworld.MailRepository;
import com.talentwalker.game.md.core.service.statistics.ActiveAndPersistenceService;
import com.talentwalker.game.md.core.timing.PvpTimerTask;
import com.talentwalker.game.md.core.timing.StatisticsActiveAndPersistenceTimerTask;

/**
 * @ClassName: TimingCommandLineRunner
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年9月14日 上午10:38:39
 */
@Component
public class TimingCommandLineRunner implements CommandLineRunner {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private MailRepository mailRepository;
    @Autowired
    private IDataConfigManager dataConfigManager;
    @Autowired
    private DataZoneRepository dataZoneRepository;
    private final static String TIMER_NAME = "Timer_PVP_";

    @Resource
    private ActiveAndPersistenceService activeAndPesistenceService;

    /**.
     * <p>Title: run</p>
     * <p>Description: </p>
     * @param args
     * @throws Exception
     * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
     */
    @Override
    public void run(String... args) throws Exception {
        // 启动PVP排行奖励定时任务
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateValue = sdf.format(System.currentTimeMillis());
        Date date = DateUtils.parseDate(dateValue, "yyyy-MM-dd", "yyyy-MM-dd");
        List<DataZone> list = dataZoneRepository.findAll();
        long time = date.getTime()
                + dataConfigManager.getTest().get("duel_other").get("getscoretime").getInteger("size")
                        * DateUtils.MILLIS_PER_HOUR;
        for (DataZone data : list) {
            Timer timer = new Timer(TIMER_NAME + data.getId());
            timer.scheduleAtFixedRate(
                    new PvpTimerTask(mongoTemplate, mailRepository, dataConfigManager, data.getId(), dateValue),
                    // new Date(time), DateUtils.MILLIS_PER_DAY);1000
                    new Date(), 2 * 60 * 1000);
        }
        // 启动活跃与存留定时统计任务
        Date activeAndPersistenceStartTime = DateUtils.parseDate(dateValue + " 23:55:00", "yyyy-MM-dd HH:mm:ss");
        Timer timer = new Timer("activeAndPersistenceStartTime:" + dateValue + " 23:55:00");
        timer.scheduleAtFixedRate(new StatisticsActiveAndPersistenceTimerTask(activeAndPesistenceService),
                // activeAndPersistenceStartTime, DateUtils.MILLIS_PER_DAY);
                new Date(), 3 * 60 * 1000);
    }

}
