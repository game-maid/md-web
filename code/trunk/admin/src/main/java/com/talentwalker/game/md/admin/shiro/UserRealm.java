/**
 * @Title: UserRealm.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年4月28日  闫昆
 */
 

package com.talentwalker.game.md.admin.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.talentwalker.game.md.admin.domain.sys.User;
import com.talentwalker.game.md.admin.service.sys.UserService;

/**
 * @ClassName: UserRealm
 * @Description: 重写shiro授权、认证逻辑
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年4月28日 上午11:05:20
 */

public class UserRealm extends AuthorizingRealm {
    
    @Autowired
    private UserService userService;

    /**.
     * <p>Title: doGetAuthorizationInfo</p>
     * <p>Description: </p>
     * @param arg0
     * @return
     * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        User user = (User) principal.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(userService.findStringRoles(user));
        info.setStringPermissions(userService.findStringPermissions(user));
        return info;
    }

    /**.
     * <p>Title: doGetAuthenticationInfo</p>
     * <p>Description: </p>
     * @param arg0
     * @return
     * @throws AuthenticationException
     * @see org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {

        UsernamePasswordToken userToken = (UsernamePasswordToken) authenticationToken;
        String username = userToken.getUsername();
        
        User user = userService.findByUsername(username);
        
        if (null == user) {
            throw new UnknownAccountException();
        } else if (Boolean.FALSE.equals(user.getIsEnable())) {
            throw new LockedAccountException();
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(),
                ByteSource.Util.bytes(user.getSalt()), getName());

        return info;
    }
    
    /**
     * @param userService 要设置的 userService
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
