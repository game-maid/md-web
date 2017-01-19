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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.talentwalker.game.md.core.constant.ItemID;
import com.talentwalker.game.md.core.domain.GameLog;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.exception.GameException;
import com.talentwalker.game.md.core.repository.GameLogRepository;
import com.talentwalker.game.md.core.response.GameModel;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.util.ServletUtils;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

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
            gameLogRepository.insert(log);
        }
        return result;
    }

    /**
     * @Description:允许将非字符串为键的map 格式化为jsonObject
     * @param obj
     * @return
     * @throws
     */
    private String formatJSONObject(Object obj) {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setAllowNonStringKeys(true);
        jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
            @Override
            public boolean apply(Object arg0, String key, Object value) {
                return value == null;
            }
        });
        return JSONObject.fromObject(obj, jsonConfig).toString();
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
            log.setPostGold(getLord().getGold());
            log.setPostLevel(getLord().getLevel());
            log.setPostVipscore(getLord().getVipScore());
            if (log.getPreDiamond() > log.getPostDiamond()) {// 消耗钻石
                expendItems.add(ItemID.DIAMOND);
            }
            if (log.getPreGold() > log.getPostDiamond()) {// 消耗金币
                expendItems.add(ItemID.GOLD);
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

    public static void main(String[] args) {
        Lord lord = new Lord();
        lord.setId("id");
        JSONObject fromObject = JSONObject.fromObject(lord);
    }
}
