/**
 * @Title: ServletUtil.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年4月26日  闫昆
 */

package com.talentwalker.game.md.core.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @ClassName: ServletUtil
 * @Description: Description of this class
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年4月26日 下午4:06:46
 */

public class ServletUtils {

    /**
     * @Description: 获取客户端ip
     * @param request HttpServletRequest
     * @return 返回客户端ip，出现异常ip为unKnownIp
     */
    public static String getRequestIp(HttpServletRequest request) {
        String ip = "";
        try {
            ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            // 防止多级ip代理，生成多个ip，第一个为真实ip
            if (!StringUtils.isEmpty(ip) && ip.length() > 15 && ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        } catch (Exception e) {
            ip = "unKnownIp";
        }
        return ip;
    }

    /**
     * @Description: 获取客户端ip，springmvc
     * @return 返回客户端ip，出现异常ip为unKnownIp
     */
    public static String getRequestIp() {
        return getRequestIp(getRequest());
    }

    /**
     * @Description: 获取request，基于springmvc
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 校验请求是否为ajax请求
     * @param request
     * @return true ajax请求 || false 同步请求
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String str = request.getHeader("x-requested-with");
        if (!StringUtils.isEmpty(str)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @Description: 获取参数名是以prefix开头的，返回的key值已经去掉prefix
     * @param request
     * @param prefix 前缀
     * @return
     */
    public static Map<String, Object> getParametersStartWith(HttpServletRequest request, String prefix) {
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, Object> params = new HashMap<String, Object>();
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if (paramName.startsWith(prefix)) {
                String key = StringUtils.substringAfter(paramName, prefix);
                String[] values = request.getParameterValues(paramName);
                if (values == null || values.length == 0) {
                } else if (values.length == 1) {
                    params.put(key, values[0]);
                } else {
                    params.put(key, values);
                }
            }
        }
        return params;
    }

    public static void main(String[] args) {
        String s = "search_aaaa_bb";
        System.out.println(StringUtils.substringAfter(s, "search_"));
    }
}
