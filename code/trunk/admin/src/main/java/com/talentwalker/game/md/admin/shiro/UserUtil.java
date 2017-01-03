/**
 * @Title: UserUtil.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月9日 闫昆
 */
 

package com.talentwalker.game.md.admin.shiro;

import org.apache.shiro.SecurityUtils;

import com.talentwalker.game.md.admin.domain.sys.User;

/**
 * @ClassName: UserUtil
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月9日 下午5:55:06
 */

public class UserUtil {

    public static User getUser() {
        return (User) SecurityUtils.getSubject().getPrincipal();
    }

}
