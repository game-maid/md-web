
package com.talentwalker.game.md.admin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.talentwalker.game.md.core.service.IDataZoneService;
import com.talentwalker.game.md.core.service.IGamePackageService;
import com.talentwalker.game.md.core.service.IGameZoneService;
import com.talentwalker.game.md.core.service.IPhysicalService;
import com.talentwalker.game.md.core.service.IPlatformService;

@Configuration
public class AdminRemoteServiceConfiguration {
    @Value("${game.portal.url.prefix}")
    private String portalUrlPrefix;

    @Bean(name = {"gameZoneService" })
    public Object GameZoneInfoService() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        proxy.setServiceUrl(portalUrlPrefix + "/remote/gameZoneService");
        proxy.setServiceInterface(IGameZoneService.class);
        proxy.afterPropertiesSet();
        return proxy;
    }

    @Bean(name = {"dataLogicService" })
    public Object DataLogicInfoService() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        proxy.setServiceUrl(portalUrlPrefix + "/remote/dataLogicService");
        proxy.setServiceInterface(IDataZoneService.class);
        proxy.afterPropertiesSet();
        return proxy;
    }

    @Bean(name = {"platformService" })
    public Object PlatformInfoService() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        proxy.setServiceUrl(portalUrlPrefix + "/remote/platformService");
        proxy.setServiceInterface(IPlatformService.class);
        proxy.afterPropertiesSet();
        return proxy;
    }

    @Bean(name = {"applyPackageService" })
    public Object ApplyPackageService() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        proxy.setServiceUrl(portalUrlPrefix + "/remote/applyPackageService");
        proxy.setServiceInterface(IGamePackageService.class);
        proxy.afterPropertiesSet();
        return proxy;
    }

    @Bean(name = {"physicalService" })
    public Object PhysicalService() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        proxy.setServiceUrl(portalUrlPrefix + "/remote/physicalService");
        proxy.setServiceInterface(IPhysicalService.class);
        proxy.afterPropertiesSet();
        return proxy;
    }

}
