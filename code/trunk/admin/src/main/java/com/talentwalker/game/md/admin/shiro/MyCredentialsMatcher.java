/**
 * @Title: MyCredentialsMatcher.java
 * @Copyright (C) 2015 Ykun
 * @Description:
 * @Revision 1.0 2015年8月31日 闫昆
 */

package com.talentwalker.game.md.admin.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

/**
 * @ClassName: MyCredentialsMatcher
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2015年8月31日 下午1:59:12
 */

public class MyCredentialsMatcher extends HashedCredentialsMatcher {

    public MyCredentialsMatcher() {
        super(PasswordUtil.HASH_ALGORITHM);
    }

    /**
     * @Title: doCredentialsMatch
     * @Description: 可以扩展验证，例如验证登陆次数
     * @param token
     * @param info
     * @return
     * @see org.apache.shiro.authc.credential.SimpleCredentialsMatcher#doCredentialsMatch(org.apache.shiro.authc.AuthenticationToken,
     *      org.apache.shiro.authc.AuthenticationInfo)
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        return super.doCredentialsMatch(token, info);
    }

}
