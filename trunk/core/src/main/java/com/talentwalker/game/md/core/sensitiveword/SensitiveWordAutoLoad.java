/**
 * @Title: SensitiveAutoLoad.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月5日  占志灵
 */

package com.talentwalker.game.md.core.sensitiveword;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @ClassName: SensitiveAutoLoad
 * @Description: Description of this class
 * @author <a href="mailto:zhanzhiling@talentwalker.com">占志灵</a> 于 2016年8月5日 下午1:20:27
 */
@Component
public class SensitiveWordAutoLoad implements CommandLineRunner {
    @Autowired
    private ISensitiveWordManager sensitiveWordManager;

    /**.
     * <p>Title: run</p>
     * <p>Description: </p>
     * @param args
     * @throws Exception
     * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
     */
    @Override
    public void run(String... args) throws Exception {
        sensitiveWordManager.update();
    }

}
