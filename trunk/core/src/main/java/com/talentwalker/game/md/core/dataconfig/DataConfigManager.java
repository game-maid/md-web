/**
 * @Title: DataConfigManager.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月30日  占志灵
 */

package com.talentwalker.game.md.core.dataconfig;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Repository;

import com.talentwalker.game.md.core.exception.ErrorCode;
import com.talentwalker.game.md.core.repository.CounterRepository;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.RemoteServiceUtils;

/**
 * @ClassName: DataConfigManager
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年5月30日 下午6:03:42
 */
@Repository
public class DataConfigManager implements IDataConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(DataConfigManager.class);
    private static final String CACHE_NAME = "dataconfig";
    private static final String CACHE_KEY_TEST = "'test'";
    private static final String CACHE_KEY_SUBMIT = "'submit'";
    private static final String CACHE_KEY_ONLINE = "'online'";

    private static final String VERSION_NAME_TEST = "test_config_version";
    private static final String VERSION_NAME_SUBMIT = "submit_config_version";
    private static final String VERSION_NAME_ONLINE = "online_config_version";

    private String propertiesFile = "dataconfig.properties";

    // @Value("${path.test}")
    private String dataconfigPathTest;
    // @Value("${path.submit}")
    private String dataconfigPathSubmit;
    // @Value("${path.online}")
    private String dataconfigPathOnline;

    @Autowired
    private CounterRepository counterRepository;

    public DataConfigManager() {
        String activeProfiles = System.getProperty("spring.profiles.active");
        if (activeProfiles != null) {
            propertiesFile = "dataconfig-" + activeProfiles + ".properties";
        }
        Properties pro = null;
        try {
            pro = PropertiesLoaderUtils
                    .loadProperties(new EncodedResource(new ClassPathResource(propertiesFile), "utf-8"));
            logger.info("配置文件管理器加载属性文件[" + propertiesFile + "]");
            dataconfigPathTest = pro.getProperty("path.test");
            dataconfigPathSubmit = pro.getProperty("path.submit");
            dataconfigPathOnline = pro.getProperty("path.online");
        } catch (IOException e) {
            GameExceptionUtils.throwException(ErrorCode.FAIL_DEFAULT, e.getMessage(), e);
        }
    }

    @Cacheable(value = CACHE_NAME, key = CACHE_KEY_TEST)
    public DataConfig getTest() {
        return update(dataconfigPathTest, VERSION_NAME_TEST, false);
    }

    @Cacheable(value = CACHE_NAME, key = CACHE_KEY_SUBMIT)
    public DataConfig getSubmit() {
        return update(dataconfigPathSubmit, VERSION_NAME_SUBMIT, false);
    }

    @Cacheable(value = CACHE_NAME, key = CACHE_KEY_ONLINE)
    public DataConfig getOnline() {
        return update(dataconfigPathOnline, VERSION_NAME_ONLINE, false);
    }

    @CachePut(value = CACHE_NAME, key = CACHE_KEY_TEST)
    public DataConfig updateTestIncVersion() {
        return update(dataconfigPathTest, VERSION_NAME_TEST, true);
    }

    @CachePut(value = CACHE_NAME, key = CACHE_KEY_SUBMIT)
    public DataConfig updateSubmitIncVersion() {
        return update(dataconfigPathSubmit, VERSION_NAME_SUBMIT, true);
    }

    @CachePut(value = CACHE_NAME, key = CACHE_KEY_ONLINE)
    public DataConfig updateOnlineIncVersion() {
        return update(dataconfigPathOnline, VERSION_NAME_ONLINE, true);
    }

    @CachePut(value = CACHE_NAME, key = CACHE_KEY_TEST)
    public DataConfig updateTest() {
        return update(dataconfigPathTest, VERSION_NAME_TEST, false);
    }

    @CachePut(value = CACHE_NAME, key = CACHE_KEY_SUBMIT)
    public DataConfig updateSubmit() {
        return update(dataconfigPathSubmit, VERSION_NAME_SUBMIT, false);
    }

    @CachePut(value = CACHE_NAME, key = CACHE_KEY_ONLINE)
    public DataConfig updateOnline() {
        return update(dataconfigPathOnline, VERSION_NAME_ONLINE, false);
    }

    public static IDataConfigManager getRemoteService(String host, String port, String contextPath) {
        return (IDataConfigManager) RemoteServiceUtils.getRemoteService(host, port, contextPath,
                IDataConfigManager.class, "/remote/dataConfigService");
    }

    private DataConfig update(String path, String versionName, boolean isIncVersion) {
        DataConfig config = DataConfig.fromFilePath(path);
        Long version = null;
        if (isIncVersion) {
            version = counterRepository.getNextSequence(versionName);
        } else {
            version = counterRepository.findOne(versionName).getSeq();
        }
        config.setVersion(version);
        return config;
    }

    public String getOnlinePath() {
        return this.dataconfigPathOnline;
    }

    public String getSubmitPath() {
        return this.dataconfigPathSubmit;
    }

    public String getTestPath() {
        return this.dataconfigPathTest;
    }

}
