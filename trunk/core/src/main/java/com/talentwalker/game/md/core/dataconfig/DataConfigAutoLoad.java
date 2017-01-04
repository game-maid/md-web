/**
 * @Title: DataConfigAutoLoad.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月31日  占志灵
 */

package com.talentwalker.game.md.core.dataconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @ClassName: DataConfigAutoLoad
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年5月31日 下午3:00:59
 */
@Component
public class DataConfigAutoLoad implements CommandLineRunner {
    @Autowired
    private IDataConfigManager dataConfigManager;

    /**.
     * <p>Title: run</p>
     * <p>Description: </p>
     * @param args
     * @throws Exception
     * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
     */
    @Override
    public void run(String... args) throws Exception {
        dataConfigManager.updateOnline();
        dataConfigManager.updateSubmit();
        dataConfigManager.updateTest();
    }

}
