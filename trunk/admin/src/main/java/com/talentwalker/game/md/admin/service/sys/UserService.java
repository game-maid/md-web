/**
 * @Title: UserService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年4月28日  闫昆
 */

package com.talentwalker.game.md.admin.service.sys;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import com.talentwalker.game.md.admin.domain.sys.Menu;
import com.talentwalker.game.md.admin.domain.sys.Role;
import com.talentwalker.game.md.admin.domain.sys.User;
import com.talentwalker.game.md.admin.repository.sys.MenuRepository;
import com.talentwalker.game.md.admin.repository.sys.RoleRepository;
import com.talentwalker.game.md.admin.repository.sys.UserRepository;
import com.talentwalker.game.md.admin.service.BaseService;
import com.talentwalker.game.md.admin.shiro.PasswordUtil;
import com.talentwalker.game.md.core.exception.GameException;
import com.talentwalker.game.md.core.util.GameExceptionUtils;

/**
 * @ClassName: UserService
 * @Description: Description of this class
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年4月28日 下午3:57:32
 */

@Service
public class UserService extends BaseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MenuRepository menuRepository;

    /**
     * @param request
     * @return
     */
    public Page<User> findAll(HttpServletRequest request) {
        return userRepository.findAll(buildPageRequest(request));
    }

    public Page<User> findByCondition(HttpServletRequest request, String condition) {
        return userRepository.findByUsernameOrName(condition, condition, buildPageRequest(request));
    }

    /**
     * @param user
     */
    public void saveUser(User user) {
        user.setCreateDate(new Date());
        user.setRoleList(roleRepository.findByIdIn(user.getRoles()));
        PasswordUtil.encryptPassword(user);
        userRepository.save(user);
    }

    /**
     * @param username
     * @return
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * @param user
     * @return
     */
    public Set<String> findStringRoles(User user) {
        Set<String> roles = Sets.newHashSet();
        List<Role> list = user.getRoleList();
        if (user.getIsSuper()) {
            list = roleRepository.findAll();
        }
        for (Role role : list) {
            roles.add(role.getId());
        }
        return roles;
    }

    /**
     * @Description: @param user @return @throws
     */
    public Set<String> findStringPermissions(User user) {
        Set<String> permissions = Sets.newHashSet();
        if (user.getIsSuper()) {
            List<Menu> list = menuRepository.findAll();
            for (Menu menu : list) {
                if (StringUtils.isNotEmpty(menu.getPermission())) {
                    permissions.add(menu.getPermission());
                }
            }
        } else {
            for (Role role : user.getRoleList()) {
                for (Menu menu : role.getMenuList()) {
                    if (StringUtils.isNotEmpty(menu.getPermission()) && !permissions.contains(menu.getPermission())) {
                        permissions.add(menu.getPermission());
                    }
                }
            }
        }
        return permissions;
    }

    /**
     * 修改密码
     * 
     * @param request
     * @return
     * @throws GameException
     */
    public void changePass(HttpServletRequest request) throws GameException {
        String username = request.getParameter("username");
        String oldpass = request.getParameter("oldpass");
        String newpass = request.getParameter("password");

        User user = userRepository.findByUsername(StringUtils.trim(username));
        if (null == user) {
            GameExceptionUtils.throwException(getMessage("sys.changepass.fail.unknow"));
        }

        if (!StringUtils.equals(user.getPassword(), PasswordUtil.getEncryptPass(oldpass, user.getSalt()))) {
            GameExceptionUtils.throwException(getMessage("sys.changepass.fail.wrongpass"));
        }

        user.setPassword(newpass);
        PasswordUtil.encryptPassword(user);
        userRepository.save(user);
    }

    /**
     * @param id
     */
    public void delete(String id) {
        userRepository.delete(id);
    }

    /**
     * @param user
     * @return
     */
    public void updateUser(User user) {
        User u = userRepository.findById(user.getId());
        if (u != null) {
            u.setIsEnable(user.getIsEnable());
            u.setIsSuper(user.getIsSuper());
            u.setName(user.getName());
            u.setRoleList(roleRepository.findByIdIn(user.getRoles()));
            if (StringUtils.isNotEmpty(user.getPassword())) {
                u.setPassword(user.getPassword());
                PasswordUtil.encryptPassword(u);
            }
            userRepository.save(u);
        }
    }

    /**
     * @Description: 获取所有管理用户
     * @return
     * @throws
     */
    public Object findList() {
        return userRepository.findAll();
    }

}
