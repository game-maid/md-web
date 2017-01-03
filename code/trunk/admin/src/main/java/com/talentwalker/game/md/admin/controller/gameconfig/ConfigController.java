/**
 * @Title: UpdateConfig.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月1日  赵丽宝
 */

package com.talentwalker.game.md.admin.controller.gameconfig;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.talentwalker.game.md.admin.controller.BaseController;
import com.talentwalker.game.md.core.dataconfig.DataConfigManager;
import com.talentwalker.game.md.core.dataconfig.IDataConfigManager;
import com.talentwalker.game.md.core.domain.GameZone;
import com.talentwalker.game.md.core.domain.PhysicalServer;
import com.talentwalker.game.md.core.exception.ErrorCode;
import com.talentwalker.game.md.core.repository.PhysicalRepository;
import com.talentwalker.game.md.core.response.GameResponseEntity;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.web.bind.annotation.GameResponse;

import net.sf.json.JSONObject;

/**
 * @ClassName: ConfigController
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年6月1日 上午11:16:36
 */
@Controller
@RequestMapping("config")
public class ConfigController extends BaseController {

    @Autowired
    private IDataConfigManager dataConfigManager;

    @Autowired
    private PhysicalRepository physicalRepository;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String forword() {
        return "gameconfig/config";
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Object upload(@RequestParam("file") MultipartFile file, @RequestParam("type") List<Integer> type,
            HttpServletResponse response) throws Exception {
        DataConfigManager dataConfigManager = new DataConfigManager();
        String fileName = file.getOriginalFilename();
        try {
            for (int updataType : type) {
                if (GameZone.TYPE_TEST == updataType) {
                    File myfile1 = new File(dataConfigManager.getTestPath() + "/" + fileName);
                    FileUtils.copyInputStreamToFile(file.getInputStream(), myfile1);
                } else if (GameZone.TYPE_AUDIT == updataType) {
                    File myfile2 = new File(dataConfigManager.getSubmitPath() + "/" + fileName);
                    FileUtils.copyInputStreamToFile(file.getInputStream(), myfile2);
                } else if (GameZone.TYPE_ONLINE == updataType) {
                    File myfile3 = new File(dataConfigManager.getOnlinePath() + "/" + fileName);
                    FileUtils.copyInputStreamToFile(file.getInputStream(), myfile3);
                }
            }
        } catch (Exception e) {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter()
                    .println(JSONObject.fromObject(new GameResponseEntity<>(ErrorCode.FAIL_DEFAULT, e.getMessage())));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GameResponse
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Object update(@RequestParam("type") List<Integer> type) {
        List<PhysicalServer> list = physicalRepository.findAll();
        for (PhysicalServer server : list) {
            try {
                IDataConfigManager imanager = DataConfigManager.getRemoteService(server.getHost(), server.getPort(),
                        server.getContext());
                for (Integer updataType : type) {
                    if (GameZone.TYPE_TEST == updataType) {
                        imanager.updateTest();
                    } else if (GameZone.TYPE_AUDIT == updataType) {
                        imanager.updateSubmit();
                    } else if (GameZone.TYPE_ONLINE == updataType) {
                        imanager.updateOnline();
                    }
                }

            } catch (Exception e) {
                GameExceptionUtils.throwException(ErrorCode.FAIL_DEFAULT, e.getMessage(), e);
            }
        }

        for (Integer updataType : type) {
            if (GameZone.TYPE_TEST == updataType) {
                dataConfigManager.updateTestIncVersion();
            } else if (GameZone.TYPE_AUDIT == updataType) {
                dataConfigManager.updateSubmitIncVersion();
            } else if (GameZone.TYPE_ONLINE == updataType) {
                dataConfigManager.updateOnlineIncVersion();
            }
        }

        return null;
    }

    @ResponseBody
    @RequestMapping(value = "treeList", method = RequestMethod.GET)
    public Object treeList() {
        List<Integer> typeList = Lists.newArrayList();
        typeList.add(GameZone.TYPE_ONLINE);
        typeList.add(GameZone.TYPE_TEST);
        typeList.add(GameZone.TYPE_AUDIT);

        List<PhysicalServer> list = physicalRepository.findAll();
        List<Map<String, Object>> treeList = Lists.newArrayList();
        for (PhysicalServer server : list) {
            try {
                IDataConfigManager dataConfigManager = DataConfigManager.getRemoteService(server.getHost(),
                        server.getPort(), server.getContext());
                List<Map<String, Object>> chidren = Lists.newArrayList();
                for (Integer type : typeList) {
                    Map<String, Object> chidrenMap = Maps.newHashMap();
                    if (type == GameZone.TYPE_ONLINE) {
                        String text = getMessage("sys.config.online") + ":"
                                + dataConfigManager.getOnline().getVersion();
                        chidrenMap.put("text", text);
                        chidren.add(chidrenMap);
                    } else if (type == GameZone.TYPE_TEST) {
                        String text = getMessage("sys.config.test") + ":" + dataConfigManager.getTest().getVersion();
                        chidrenMap.put("text", text);
                        chidren.add(chidrenMap);
                    } else if (type == GameZone.TYPE_AUDIT) {
                        String text = getMessage("sys.config.submit") + ":"
                                + dataConfigManager.getSubmit().getVersion();
                        chidrenMap.put("text", text);
                        chidren.add(chidrenMap);
                    }
                }

                Map<String, Object> m = Maps.newHashMap();
                m.put("text", server.getName());
                m.put("children", chidren);
                treeList.add(m);
            } catch (Exception e) {
                GameExceptionUtils.throwException(ErrorCode.FAIL_DEFAULT, e.getMessage(), e);
            }

        }
        return treeList;
    }

}
