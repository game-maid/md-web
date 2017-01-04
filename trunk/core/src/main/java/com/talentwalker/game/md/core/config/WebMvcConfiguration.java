/**
 * @Title: WebMvcConfiguration.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月16日  占志灵
 */

package com.talentwalker.game.md.core.config;

import java.util.List;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.talentwalker.game.md.core.response.GameModel;
import com.talentwalker.game.md.core.web.bind.annotation.RequestBean;
import com.talentwalker.game.md.core.web.servlet.filter.CorsFilter;
import com.talentwalker.game.md.core.web.servlet.mvc.method.annotation.GameRequestResponseBodyMethodProcessor;
import com.talentwalker.game.md.core.web.servlet.mvc.support.GameDefaultHandlerExceptionResolver;

/**
 * @ClassName: WebMvcConfiguration
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年5月16日 下午3:43:25
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    @RequestBean
    public GameModel requestGameModelMap() {
        return new GameModel();
    }

    @Autowired
    private HttpMessageConverters messageConverters;

    /**
     * .
     * <p>
     * Title: configureHandlerExceptionResolvers
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param exceptionResolvers
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#configureHandlerExceptionResolvers(java.util.List)
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        if (exceptionResolvers.isEmpty()) {
            exceptionResolvers.add(new GameDefaultHandlerExceptionResolver());
        }
    }

    /**
     * .
     * <p>
     * Title: addReturnValueHandlers
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param returnValueHandlers
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#addReturnValueHandlers(java.util.List)
     */
    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(new GameRequestResponseBodyMethodProcessor(this.messageConverters.getConverters()));
    }

    @Bean
    public Filter corsFilter() {
        return new CorsFilter();
    }

}
