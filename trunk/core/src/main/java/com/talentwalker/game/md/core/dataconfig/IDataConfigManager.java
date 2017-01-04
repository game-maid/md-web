/**
 * @Title: IDataConfigManager.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月30日  占志灵
 */

package com.talentwalker.game.md.core.dataconfig;

/**
 * @ClassName: IDataConfigManager
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年5月30日 下午7:14:13
 */

public interface IDataConfigManager {
    public DataConfig getTest();

    public DataConfig getSubmit();

    public DataConfig getOnline();

    public DataConfig updateTestIncVersion();

    public DataConfig updateSubmitIncVersion();

    public DataConfig updateOnlineIncVersion();

    public DataConfig updateTest();

    public DataConfig updateSubmit();

    public DataConfig updateOnline();

}
