/**
 * @Title: ServiceExporterConfiguration.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月17日  占志灵
 */

package com.talentwalker.game.md.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

import com.talentwalker.game.md.core.service.IGameUserServiceRemote;

/**
 * @ClassName: ServiceExporterConfiguration
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年5月17日 下午4:16:22
 */
@Configuration
public class CoreServiceExporterConfiguration {
    @Bean(name = {"/remote/gameUserService" })
    public HttpInvokerServiceExporter PhysicalService(IGameUserServiceRemote gameUserServiceRemote) {
        HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
        exporter.setService(gameUserServiceRemote);
        exporter.setServiceInterface(IGameUserServiceRemote.class);
        exporter.afterPropertiesSet();
        return exporter;
    }

}
