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

import com.talentwalker.game.md.core.dataconfig.IDataConfigManager;

/**
 * 
 * @ClassName: ServiceExporterConfiguration
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年5月30日 下午7:11:22
 */
@Configuration
public class ServiceExporterConfiguration {

    @Bean(name = {"/remote/dataConfigService" })
    public HttpInvokerServiceExporter dataConfigService(IDataConfigManager dataConfigManager) {
        HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
        exporter.setService(dataConfigManager);
        exporter.setServiceInterface(IDataConfigManager.class);
        exporter.afterPropertiesSet();
        return exporter;
    }

}
