/**
 * @Title: LogAspect.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年4月26日  闫昆
 */

package com.talentwalker.game.md.portal.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.talentwalker.game.md.core.domain.PortalLog;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.exception.GameException;
import com.talentwalker.game.md.core.repository.PortalLogRepository;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.util.ServletUtils;

/**
 * @ClassName: LogAspect
 * @Description: 门户日志
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月14日 下午6:47:05
 */

@Aspect
@Component
public class LogAspect extends GameSupport {

    @Autowired
    private PortalLogRepository portalLogRepository;

    @Pointcut("execution(* com.talentwalker.game.md.portal.controller..*(..))")
    public void logPoincut() {
    }

    @Around("logPoincut()")
    public Object accessLog(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        long start = System.currentTimeMillis();
        PortalLog log = getLog();
        try {
            log.setParams(point.getArgs());
            result = point.proceed();
            log.setResult(result);
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
            portalLogRepository.insert(log);
        }
        return result;
    }

    private PortalLog getLog() {
        PortalLog log = new PortalLog();
        log.setIp(ServletUtils.getRequestIp());
        log.setRequestTime(System.currentTimeMillis());
        log.setUri(ServletUtils.getRequest().getRequestURI());
        log.setCode(GameErrorCode.SUCCESS);
        return log;
    }

}
