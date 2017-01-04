/**
 * @Title: LogAspect.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年4月26日  闫昆
 */

package com.talentwalker.game.md.admin.aop;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.talentwalker.game.md.admin.domain.log.Log;
import com.talentwalker.game.md.admin.domain.sys.User;
import com.talentwalker.game.md.admin.repository.log.LogRepository;
import com.talentwalker.game.md.admin.shiro.UserUtil;
import com.talentwalker.game.md.core.util.ServletUtils;

import net.sf.json.JSONObject;

/**
 * @ClassName: LogAspect
 * @Description: 以AOP的方式记录操作日志
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年4月26日 下午3:23:52
 */

@Aspect
@Component
public class LogAspect {

    @Autowired
    private LogRepository logRepository;

    private final static Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(* com.talentwalker.game.md.admin.controller..*(..))")
    public void logPoincut() {
    }

    @Around("logPoincut()")
    public Object accessLog(ProceedingJoinPoint point) throws Throwable {
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            User user = UserUtil.getUser();
            if (null != user) {
                Log log = new Log();
                log.setIp(ServletUtils.getRequestIp());
                log.setMethod(request.getMethod());
                log.setParams(JSONObject.fromObject(request.getParameterMap()).toString());
                log.setUri(request.getRequestURI());
                log.setUserId(user.getId());
                log.setCreateTime(new Date());
                logRepository.save(log);
            }
            Object result = point.proceed();
            // long start = System.currentTimeMillis();
            // logger.info("Request: {}.{} Method: {} Result : {} Cost : {}",
            // point.getSignature().getDeclaringTypeName(),
            // point.getSignature().getName(), request.getMethod(), result, (System.currentTimeMillis() - start));
            return result;
        } catch (Exception e) {
            logger.error("fucking error:{}", e.getMessage());
            throw e;
        }
    }

}
