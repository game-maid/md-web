/**
 * @Title: RoleService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年5月4日  闫昆
 */

package com.talentwalker.game.md.admin.service.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.talentwalker.game.md.admin.domain.sys.Role;
import com.talentwalker.game.md.admin.domain.sys.User;
import com.talentwalker.game.md.admin.repository.sys.MenuRepository;
import com.talentwalker.game.md.admin.repository.sys.RoleRepository;
import com.talentwalker.game.md.admin.repository.sys.UserRepository;
import com.talentwalker.game.md.admin.service.BaseService;
import com.talentwalker.game.md.core.exception.GameException;

/**
 * @ClassName: RoleService
 * @Description: Description of this class
 * @author <a href="yankun@talentwalker.com">闫昆</a> 于 2016年5月4日 上午10:12:53
 */

@Service
public class RoleService extends BaseService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * @Description: @param request @return @throws
     */
    public Page<Role> findAll(HttpServletRequest request) {
        return roleRepository.findAll(buildPageRequest(request));
    }

    public Page<Role> findByCondition(HttpServletRequest request, String condition) {
        return roleRepository.findByIdOrName(condition, condition, buildPageRequest(request));
    }

    public List<Role> findAvailableRole() {
        return roleRepository.findByIsEnable(true);
    }

    /**
     * @return
     * @throws GameException
     * @Description: @param role @throws
     */
    public void saveRole(Role role) throws GameException {
        Role newRole = new Role();
        if (StringUtils.isEmpty(role.getId())) {
            newRole.setCreateDate(new Date());
        } else {
            newRole = roleRepository.findOne(role.getId());
        }
        newRole.setName(role.getName());
        String[] menuIds = StringUtils.split(role.getMenuIds(), ",");

        // String[] menuIds = role.getMenuIds().substring(0, role.getMenuIds().length() - 1).split(",");
        List<String> ids = Lists.newArrayList();
        for (String menuId : menuIds) {
            ids.add(menuId);
        }
        // 去重
        List<String> listIds = Lists.newArrayList();
        for (String i : ids) {
            if (!listIds.contains(i)) {
                listIds.add(i);
            }
        }
        newRole.setMenuList(menuRepository.findByIdIn(listIds));
        newRole.setIsEnable(role.getIsEnable());
        roleRepository.save(newRole);
    }

    /**
     * @Description: 根据id删除一条角色信息 @param id @throws
     */
    public void deleteOneById(String id) {
        List<User> list = userRepository.findByRole(new ObjectId(id));
        List<User> changeList = new ArrayList<>();
        for (User user : list) {
            for (int i = 0; i < user.getRoleList().size(); i++) {
                if (id.equals(user.getRoleList().get(i).getId())) {
                    user.getRoleList().remove(i);
                }
            }
            changeList.add(user);
        }
        userRepository.save(changeList);
        roleRepository.delete(id);
    }

    /**
     * @return 
     * @Description: 根据角色查询用户（全部用户和已经有该角色的用户）
     * @param id
     * @throws
     */
    public List<Map<String, String>> getPickList(String id) {
        List<User> users = userRepository.findByRole(new ObjectId(id));
        List<User> userAll = userRepository.findAll();
        List<Map<String, String>> pickList = Lists.newArrayList();
        List<String> ids = Lists.newArrayList();
        for (User u : users) {
            Map<String, String> m = Maps.newHashMap();
            m.put("id", u.getId());
            m.put("text", u.getName());
            m.put("option", "selected");
            ids.add(u.getId());
            pickList.add(m);
        }
        for (User u : userAll) {
            if (ids.contains(u.getId())) {
                continue;
            }
            Map<String, String> m = Maps.newHashMap();
            m.put("id", u.getId());
            m.put("text", u.getName());
            pickList.add(m);
        }
        return pickList;
    }

    /**
     * @Description:
     * @param data
     * @return
     * @throws
     */
    public void updataAllByRole(List<String> data, String id) {
        List<User> users = userRepository.findByRole(new ObjectId(id));
        List<String> ids = Lists.newArrayList();
        for (User u : users) {
            ids.add(u.getId());
        }
        List<User> changeUserList = Lists.newArrayList();
        for (String userId : data) {
            if (ids.contains(userId)) {
                continue;
            }
            User user = userRepository.findById(userId);
            List<Role> roles = user.getRoleList();
            List<String> roleIds = Lists.newArrayList();

            if (roles != null && roles.size() != 0) {
                for (Role r : roles) {
                    roleIds.add(r.getId());
                }
            }

            roleIds.add(id);
            user.setRoleList(roleRepository.findByIdIn(roleIds));
            changeUserList.add(user);
        }
        // 被删除对应用户的
        for (String userId : ids) {
            if (data.contains(userId)) {
                continue;
            }
            User user = userRepository.findById(userId);
            for (int i = 0; i < user.getRoleList().size(); i++) {
                if (id.equals(user.getRoleList().get(i).getId())) {
                    user.getRoleList().remove(i);
                }
            }
            changeUserList.add(user);
        }
        userRepository.save(changeUserList);
    }
}
