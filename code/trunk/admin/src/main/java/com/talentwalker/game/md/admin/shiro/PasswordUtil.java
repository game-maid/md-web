/**
 * @Title: PasswordUtil.java
 * @Copyright (C) 2015 Ykun
 * @Description:
 * @Revision 1.0 2015年8月31日 闫昆
 */

package com.talentwalker.game.md.admin.shiro;

import org.apache.shiro.crypto.hash.SimpleHash;

import com.talentwalker.game.md.admin.domain.sys.User;
import com.talentwalker.game.md.core.util.StringUtils;

/**
 * @ClassName: PasswordUtil
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2015年8月31日 上午11:34:07
 */

public class PasswordUtil {

    public final static String HASH_ALGORITHM = "MD5";

    public static void encryptPassword(User user) {
        String salt = StringUtils.getRandomStr(8);
        String password = new SimpleHash(HASH_ALGORITHM, user.getPassword(), salt).toHex();
        user.setSalt(salt);
        user.setPassword(password);
    }

    public static String getEncryptPass(String password, String salt) {
        return new SimpleHash(HASH_ALGORITHM, password, salt).toHex();
    }

}
