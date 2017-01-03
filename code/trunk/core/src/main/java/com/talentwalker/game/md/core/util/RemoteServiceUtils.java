/**
 * @Title: RemoteServiceUtils.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月2日  占志灵
 */

package com.talentwalker.game.md.core.util;

import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

/**
 * @ClassName: RemoteServiceUtils
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月2日 上午11:07:49
 */

public class RemoteServiceUtils {
    public static Object getRemoteService(String host, String port, String contextPath, Class<?> interfaceClass,
            String interfaceUrl) {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        if (contextPath == null || contextPath.equals("")) {
            contextPath = "";
        } else if (!contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }
        proxy.setServiceUrl("http://" + host + ":" + port + contextPath + interfaceUrl);
        proxy.setServiceInterface(interfaceClass);
        proxy.afterPropertiesSet();
        return proxy.getObject();
    }
}
