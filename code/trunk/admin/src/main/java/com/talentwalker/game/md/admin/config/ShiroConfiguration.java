/**
 * @Title: ShiroConfig.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年4月28日  闫昆
 */
 

package com.talentwalker.game.md.admin.config;

import java.util.Map;

import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.google.common.collect.Maps;
import com.talentwalker.game.md.admin.shiro.MyCredentialsMatcher;
import com.talentwalker.game.md.admin.shiro.UserRealm;

/**
 * @ClassName: ShiroConfig
 * @Description: shiro 配置
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年4月28日 上午11:04:09
 */

@Configuration
public class ShiroConfiguration {
    
    @Bean
    public MemoryConstrainedCacheManager shiroCacheManager() {
        return new MemoryConstrainedCacheManager();
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(){
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager());
        shiroFilter.setLoginUrl("/login");
        shiroFilter.setSuccessUrl("/main");
        shiroFilter.setFilterChainDefinitionMap(getFilterChainDefinitionMap());
        return shiroFilter;
    }
    
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm());
        securityManager.setCacheManager(shiroCacheManager());
        return securityManager;
    }
    
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
    
    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager());
        return advisor;
    }
    
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
    
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public UserRealm userRealm() {
        UserRealm realm = new UserRealm();
        realm.setCredentialsMatcher(new MyCredentialsMatcher());
        realm.setCacheManager(shiroCacheManager());
        return realm;
    }
    
    private Map<String,String> getFilterChainDefinitionMap(){
        Map<String,String> chainMap = Maps.newLinkedHashMap();
        chainMap.put("/login", "authc");
        chainMap.put("/static/**", "anon");
        chainMap.put("/logout", "logout");
        chainMap.put("/**", "user");
        return chainMap;
    }

}
