/**
 * @Title: CommonTest.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月19日  占志灵
 */

package com.talentwalk.game.test;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.talentwalker.game.md.core.dataconfig.DataConfigManager;
import com.talentwalker.game.md.core.dataconfig.IDataConfigManager;

/**
 * @ClassName: CommonTest
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年5月19日 下午3:42:00
 */

public class CommonTest {
    public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
        IDataConfigManager m = DataConfigManager.getRemoteService("localhost", "8081", "");
        // m.updateOnlineIncVersion();
        System.out.println(m.getTest().getVersion());
        System.out.println(m.getSubmit().getVersion());
        System.out.println(m.getOnline().getVersion());
    }
}
