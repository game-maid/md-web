/**
 * @Title: WebMvcConfig.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年4月26日  闫昆
 */

package com.talentwalker.game.md.admin.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.talentwalker.game.md.admin.web.AdminHandlerExceptionResolver;

/**
 * @ClassName: WebMvcConfig
 * @Description: Spring MVC配置
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年4月26日 下午2:49:21
 */

@Configuration
public class AdminWebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private HttpMessageConverters messageConverters;

    /**
     * 静态资源映射
     * 
     * @param registry
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry)
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    /**
     * 注册默认跳转
     * 
     * @param registry
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#addViewControllers(org.springframework.web.servlet.config.annotation.ViewControllerRegistry)
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/welcome").setViewName("sys/welcome");
    }

    /**
     * @Title: configureHandlerExceptionResolvers
     * @Description: 异常处理
     * @param exceptionResolvers
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#configureHandlerExceptionResolvers(java.util.List)
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.clear();
        exceptionResolvers.add(new AdminHandlerExceptionResolver());
    }

    /**
     * 国际化设置，使用session策略
     * 
     * @return
     */
    @Bean
    public SessionLocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        return resolver;
    }

    // @Bean
    // public Converter userConverter() {
    // return new Converter<DBObject, User>() {
    // @Override
    // public User convert(DBObject source) {
    // User user = new User();
    // user.setName(source.get("name") + "");
    // user.setUsername(source.get("username") + "");
    // user.setIsSuper(Boolean.valueOf(source.get("is_super") + ""));
    // user.setIsEnable(Boolean.valueOf(source.get("is_enable") + ""));
    // user.setPassword((source.get("password") + ""));
    // user.setSalt(source.get("salt") + "");
    // return user;
    // }
    // };
    // }

    // @Bean
    // public CustomConversions customConversions() {
    // List<Converter<?, ?>> converterList = new ArrayList<>();
    // // OAuth2AuthenticationReadConverter converter = new OAuth2AuthenticationReadConverter();
    // converterList.add(userConverter());
    // return new CustomConversions(converterList);
    // }

}
