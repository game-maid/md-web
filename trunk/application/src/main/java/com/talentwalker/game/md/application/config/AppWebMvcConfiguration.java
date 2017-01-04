/**
 * @Title: WebMvcConfig.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年4月26日  闫昆
 */

package com.talentwalker.game.md.application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.talentwalker.game.md.application.web.servlet.LoginHandlerInterceptor;
import com.talentwalker.game.md.core.service.IGameUserServiceRemote;

/**
 * 
 * @ClassName: AppWebMvcConfiguration
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年5月23日 上午10:27:57
 */
@Configuration
public class AppWebMvcConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    private IGameUserServiceRemote gameUserServiceRemote;

    /**.
     * <p>Title: addInterceptors</p>
     * <p>Description: </p>
     * @param registry
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry)
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor(gameUserServiceRemote));
    }

}
