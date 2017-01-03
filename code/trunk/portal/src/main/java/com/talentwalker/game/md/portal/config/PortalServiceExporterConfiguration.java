/**
 * @Title: ServiceExporterConfiguration.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月17日  占志灵
 */

package com.talentwalker.game.md.portal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

import com.talentwalker.game.md.core.service.DataZoneService;
import com.talentwalker.game.md.core.service.GamePackageService;
import com.talentwalker.game.md.core.service.GameZoneService;
import com.talentwalker.game.md.core.service.IDataZoneService;
import com.talentwalker.game.md.core.service.IGamePackageService;
import com.talentwalker.game.md.core.service.IGameZoneService;
import com.talentwalker.game.md.core.service.IPhysicalService;
import com.talentwalker.game.md.core.service.IPlatformService;
import com.talentwalker.game.md.core.service.PhysicalService;
import com.talentwalker.game.md.core.service.PlatformService;

/**
 * @ClassName: ServiceExporterConfiguration
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年5月17日 下午4:16:22
 */
@Configuration
public class PortalServiceExporterConfiguration {
    @Bean(name = {"/remote/gameZoneService" })
    public HttpInvokerServiceExporter GameZoneInfoService(GameZoneService gameZoneService) {
        HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
        exporter.setService(gameZoneService);
        exporter.setServiceInterface(IGameZoneService.class);
        exporter.afterPropertiesSet();
        return exporter;
    }

    @Bean(name = {"/remote/dataLogicService" })
    public HttpInvokerServiceExporter DataLogicInfoService(DataZoneService dataLogicService) {
        HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
        exporter.setService(dataLogicService);
        exporter.setServiceInterface(IDataZoneService.class);
        exporter.afterPropertiesSet();
        return exporter;
    }

    @Bean(name = {"/remote/platformService" })
    public HttpInvokerServiceExporter PlatformInfoService(PlatformService platformService) {
        HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
        exporter.setService(platformService);
        exporter.setServiceInterface(IPlatformService.class);
        exporter.afterPropertiesSet();
        return exporter;
    }

    @Bean(name = {"/remote/applyPackageService" })
    public HttpInvokerServiceExporter ApplyPackageService(GamePackageService applyPackageService) {
        HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
        exporter.setService(applyPackageService);
        exporter.setServiceInterface(IGamePackageService.class);
        exporter.afterPropertiesSet();
        return exporter;
    }

    @Bean(name = {"/remote/physicalService" })
    public HttpInvokerServiceExporter PhysicalService(PhysicalService physicalService) {
        HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
        exporter.setService(physicalService);
        exporter.setServiceInterface(IPhysicalService.class);
        exporter.afterPropertiesSet();
        return exporter;
    }

}
