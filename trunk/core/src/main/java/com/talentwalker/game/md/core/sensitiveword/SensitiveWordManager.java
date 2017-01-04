/**
 * @Title: SensitiveWordManager.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月5日  占志灵
 */

package com.talentwalker.game.md.core.sensitiveword;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: SensitiveWordManager
 * @Description: Description of this class
 * @author <a href="mailto:zhanzhiling@talentwalker.com">占志灵</a> 于 2016年8月5日 下午2:01:35
 */
@Repository
public class SensitiveWordManager implements ISensitiveWordManager {

    @Value("${sensitiveWord.path}")
    private String sensitiveWordPath;

    /**.
     * <p>Title: update</p>
     * <p>Description: </p>
     * @throws Exception 
     * @see com.talentwalker.game.slot.core.sensitiveword.ISensitiveWordManager#update()
     */
    @Override
    public void update() throws Exception {
        SensitiveWord.fromFilePath(sensitiveWordPath);
    }

}
