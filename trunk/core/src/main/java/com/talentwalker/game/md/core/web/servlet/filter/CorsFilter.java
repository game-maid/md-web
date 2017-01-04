/**
 * @Title: CorsFilter.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月16日 闫昆
 */
 
package com.talentwalker.game.md.core.web.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsUtils;

/**
 * @ClassName: CorsFilter
 * @Description: 处理跨域请求
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月16日 下午1:20:52
 */

public class CorsFilter implements Filter {

    private static final String HEADER_SESSIONID = "sessionId";
    private static final String HEADER_CONTENTTYPE = "Content-Type";

    /**
     * @Title: init
     * @Description:
     * @param filterConfig
     * @throws ServletException
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub

    }

    /**
     * @Title: doFilter
     * @Description:
     * @param servletRequest
     * @param ServletResponse
     * @param chain
     * @throws IOException
     * @throws ServletException
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (CorsUtils.isCorsRequest(request)) {
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, HEADER_SESSIONID);
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeader(HttpHeaders.ORIGIN));
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, request.getMethod());
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, HEADER_CONTENTTYPE);
        }
        chain.doFilter(request, response);
    }

    /**
     * @Title: destroy
     * @Description:
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}
