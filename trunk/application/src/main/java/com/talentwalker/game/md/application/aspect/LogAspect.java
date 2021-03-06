/**
 * @Title: LogAspect.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年4月26日  闫昆
 */

package com.talentwalker.game.md.application.aspect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.talentwalker.game.md.core.constant.ItemID;
import com.talentwalker.game.md.core.domain.GameLog;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.exception.GameException;
import com.talentwalker.game.md.core.repository.GameLogRepository;
import com.talentwalker.game.md.core.response.GameModel;
import com.talentwalker.game.md.core.response.ResponseKey;
import com.talentwalker.game.md.core.util.ConfigKey;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.util.ServletUtils;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * @ClassName: LogAspect
 * @Description: 游戏日志
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月14日 下午6:47:05
 */

@Aspect
@Component
public class LogAspect extends GameSupport {

    @Autowired
    private GameLogRepository gameLogRepository;
    @Autowired
    private final static Logger LOGGER = Logger.getLogger(LogAspect.class);

    @Pointcut("execution(* com.talentwalker.game.md.application.controller..*(..))")
    public void logPoincut() {
    }

    @Around("logPoincut()")
    public Object accessLog(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        long start = System.currentTimeMillis();
        GameLog log = getLog();
        try {
            log.setParams(point.getArgs());
            result = point.proceed();
            if (result instanceof GameModel) {
                GameModel gameModel = (GameModel) result;
                // JSONObject json = gameModel.getModel();
                log.setResult(formatJSONObject(gameModel.getModel()));
            } else {
                log.setResult(result);
            }
            updateLog(log);
        } catch (Throwable e) {
            if (e instanceof GameException) {
                GameException exception = (GameException) e;
                log.setCode(exception.getErrCode());
            } else {
                log.setCode(GameErrorCode.FAIL_DEFAULT);
                log.setResult(e.getMessage());
            }
            throw e;
        } finally {
            log.setCost(System.currentTimeMillis() - start);
            long startTime = System.currentTimeMillis();
            gameLogRepository.insert(log);
            LOGGER.info("游戏日志保存耗时：" + (System.currentTimeMillis() - startTime));
        }
        return result;
    }

    /**
     * @Description:允许将非字符串为键的map 格式化为jsonObject
     * @param obj
     * @return
     * @throws
     */
    private JSONObject formatJSONObject(Object obj) {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setAllowNonStringKeys(true);
        String temp = JSONObject.fromObject(obj, jsonConfig).toString();
        return JSONObject.fromObject(temp.replace("null", "\"null\""));
    }

    private GameLog getLog() {
        GameLog log = new GameLog();
        log.setCode(GameErrorCode.SUCCESS);
        log.setIp(ServletUtils.getRequestIp());
        log.setUri(ServletUtils.getRequest().getRequestURI());
        log.setPackageId(getGameUser().getPackageId());
        log.setPhysicalserverId(getGameUser().getPhysicalServerId());
        log.setPlatformId(getGameUser().getPlatformId());
        log.setZoneId(getGameUser().getGameZoneId());
        log.setSsoId(getGameUser().getSsoId());
        log.setPlayerId(getGameUser().getId());
        log.setClientVersion(getGameUser().getClientVersion());
        log.setRequestTime(System.currentTimeMillis());
        log.setSessionId(getGameUser().getGamesessionId());
        if (!isThrough()) {
            log.setPreDiamond(getLord().getDiamond());
            log.setPrePersentDiamond(getLord().getPersentDiamond());
            log.setPreGold(getLord().getGold());
            log.setPreLevel(getLord().getLevel());
            log.setPreVipscore(getLord().getVipScore());
        }
        return log;
    }

    private void updateLog(GameLog log) {
        List<String> expendItems = new ArrayList<>();
        log.setExpendItems(expendItems);
        if (!isThrough()) {
            log.setPostDiamond(getLord().getDiamond());
            log.setPostPersentDiamond(getLord().getPersentDiamond());
            log.setPostGold(getLord().getGold());
            log.setPostLevel(getLord().getLevel());
            log.setPostVipscore(getLord().getVipScore());
            if (log.getPreDiamond() > log.getPostDiamond()) {// 消耗充值钻石
                expendItems.add(ItemID.DIAMOND);
            }
            if (log.getPrePersentDiamond() > log.getPostPersentDiamond()) {// 消耗赠送钻石
                expendItems.add(ItemID.PERSENT_DIAMOND);
            }
            if (log.getPreGold() > log.getPostGold()) {// 消耗金币
                expendItems.add(ItemID.GOLD);
            }
            JSONObject itemJSON = getDataConfig().get(ConfigKey.ITEM).getJsonObject();
            Object payModel = this.gameModel.getModel(ResponseKey.PAY);
            if (payModel != null) {
                Map<String, Object> payMap = (Map<String, Object>) payModel;
                Object itemsModel = payMap.get(ResponseKey.ITEMS);
                if (itemsModel != null) {
                    Map<String, Object> itemsMap = (Map<String, Object>) itemsModel;
                    Set<String> keySet = itemsMap.keySet();
                    for (String itemId : keySet) {
                        expendItems.add(itemId);
                    }
                }
            }
        }

    }

    private boolean isThrough() {
        if (ServletUtils.getRequest().getRequestURI().contains("game/lord/register")) {
            return true;
        }
        if (ServletUtils.getRequest().getRequestURI().contains("game/lord/random/name")) {
            return true;
        }
        return false;
    }
}
