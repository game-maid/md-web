/**
 * @Title: StatisitcsService.java
 * @Copyright (C) 2017 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2017年2月14日  张福涛
 */

package com.talentwalker.game.md.admin.service.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.dataconfig.IDataConfigManager;
import com.talentwalker.game.md.core.util.ConfigKey;

import net.sf.json.JSONObject;

/**
 * @ClassName: StatisitcsService
 * @Description:后台统计
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2017年2月14日 下午8:06:01
 */
@Service
public class StatisitcsService {
    @Resource
    private IDataConfigManager dataConfigManager;

    /**
     * @Description:查询功能名称配置
     * @param language
     * @throws
     */
    public List<Map<String, Object>> queryFunctionName(String language) {
        List<Map<String, Object>> result = new ArrayList<>();
        DataConfig functionNameConfig = dataConfigManager.getTest().get(ConfigKey.FUNCTION_NAME);
        JSONObject functionNameJson = functionNameConfig.getJsonObject();
        Set<String> keySet = functionNameJson.keySet();
        for (String uri : keySet) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", uri);
            map.put("name", functionNameConfig.get(uri).getString(language));
            result.add(map);
        }
        return result;
    }

}
