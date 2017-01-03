/**
 * @Title: BaseController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月1日  占志灵
 */

package com.talentwalker.game.md.core.util;

import org.springframework.beans.factory.annotation.Autowired;

import com.talentwalker.game.md.core.response.GameModel;

/**
 * @ClassName: BaseController
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月1日 上午10:12:02
 */

public class BaseGameSupport {
    @Autowired
    protected GameModel gameModel;
}
