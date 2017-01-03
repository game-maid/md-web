/**
 * @Title: MenuService.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月7日 闫昆
 */

package com.talentwalker.game.md.admin.service.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.talentwalker.game.md.admin.domain.sys.Menu;
import com.talentwalker.game.md.admin.domain.sys.Role;
import com.talentwalker.game.md.admin.domain.sys.User;
import com.talentwalker.game.md.admin.repository.sys.MenuRepository;
import com.talentwalker.game.md.admin.repository.sys.RoleRepository;
import com.talentwalker.game.md.admin.service.BaseService;
import com.talentwalker.game.md.admin.shiro.UserUtil;

/**
 * @ClassName: MenuService
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月7日 下午11:45:25
 */

@Service
public class MenuService extends BaseService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * @Description: @return @throws
     */
    public List<Map<String, Object>> treeList() {
        List<Menu> menuList = menuRepository.findByIsEnable(Boolean.TRUE);
        return treeMenuList(menuList, "");
    }

    private List<Map<String, Object>> treeMenuList(List<Menu> menuList, String parentId) {
        List<Map<String, Object>> treeList = Lists.newArrayList();
        for (Menu menu : menuList) {
            Map<String, Object> m = Maps.newHashMap();
            String pid = StringUtils.isEmpty(menu.getParentId()) ? "" : menu.getParentId();
            if (StringUtils.equalsIgnoreCase(parentId, pid)) {
                List<Map<String, Object>> children = treeMenuList(menuList, menu.getId());
                m.put("id", menu.getId());
                m.put("text", getMessage(menu.getName()));
                m.put("icon", menu.getIcon());
                m.put("children", children);
                treeList.add(m);
            }
        }
        return treeList;
    }

    private List<Menu> treeMenu(List<Menu> menuList, String parentId) {
        List<Menu> treeList = Lists.newArrayList();
        for (Menu menu : menuList) {
            String pid = StringUtils.isEmpty(menu.getParentId()) ? "" : menu.getParentId();
            if (StringUtils.equalsIgnoreCase(parentId, pid)) {
                List<Menu> children = treeMenu(menuList, menu.getId());
                menu.setChildList(children);
                treeList.add(menu);
            }
        }
        return treeList;
    }

    /**
     * @Description: @return @throws
     */
    public List<Menu> findMenus() {
        User user = UserUtil.getUser();
        List<Menu> list = menuRepository.findAll(new Sort(Direction.ASC, "sort"));

        for (int i = 0; i < list.size(); i++) {
            Menu menu = list.get(i);
            if (!menu.getIsEnable()) {
                list.remove(i);
                continue;
            }
        }

        if (!user.getIsSuper()) {
            Map<String, String> hashMenu = Maps.newHashMap();
            for (Role role : user.getRoleList()) {
                for (Menu menu : role.getMenuList()) {
                    if (!hashMenu.containsKey(menu.getId())) {
                        hashMenu.put(menu.getId(), "");
                    }
                }
            }

            List<Menu> menus = Lists.newArrayList();
            for (int i = 0; i < list.size(); i++) {
                Menu m = list.get(i);
                if (hashMenu.containsKey(m.getId())) {
                    menus.add(m);
                }
            }
            list = menus;
        }

        return treeMenu(list, "");
    }

    /**
     * @param menu
     */
    public void saveMenu(Menu menu) {
        Menu m = new Menu();
        if (StringUtils.isEmpty(menu.getId())) {
            m.setCreateDate(new Date());
        } else {
            m = menuRepository.findOne(menu.getId());
        }
        m.setParentId(menu.getParentId());
        m.setName(menu.getName());
        m.setUrl(menu.getUrl());
        m.setIcon(menu.getIcon());
        m.setIsEnable(menu.getIsEnable());
        m.setSort(menu.getSort());
        m.setPermission(menu.getPermission());
        menuRepository.save(m);
    }

    /**
     * @Description:
     * @param id
     * @throws
     */
    public void delete(String id) {
        List<Role> list = roleRepository.findByMenuId(new ObjectId(id));
        List<Role> changeList = new ArrayList<>();
        List<Menu> menuList = menuRepository.findByParentId(id);
        List<String> menuChidList = Lists.newArrayList(); // 被移除的子类菜单
        menuChidList.add(id);
        for (Menu m : menuList) {
            menuChidList.add(m.getId());
        }
        for (Role role : list) {
            List<String> menuIds = Lists.newArrayList();
            for (Menu m : role.getMenuList()) {
                menuIds.add(m.getId());
            }
            menuIds.removeAll(menuChidList);
            role.setMenuList(menuRepository.findByIdIn(menuIds));
            changeList.add(role);
        }
        roleRepository.save(changeList); // 修改角色对应的菜单
        menuRepository.delete(menuList); // 删除子菜单
        menuRepository.delete(id);

    }

    /**
     * @Description:
     * @param id
     * @return
     * @throws
     */
    public List<Map<String, String>> getPickList(String id) {
        List<Role> roles = roleRepository.findByMenuId(new ObjectId(id));
        List<Role> roleAll = roleRepository.findAll();
        List<Map<String, String>> pickList = Lists.newArrayList();
        List<String> roleIds = Lists.newArrayList();
        for (Role r : roles) {
            Map<String, String> m = Maps.newHashMap();
            m.put("id", r.getId());
            m.put("text", r.getName());
            m.put("option", "selected");
            roleIds.add(r.getId());
            pickList.add(m);
        }
        for (Role r : roleAll) {
            if (roleIds.contains(r.getId())) {
                continue;
            }
            Map<String, String> m = Maps.newHashMap();
            m.put("id", r.getId());
            m.put("text", r.getName());
            pickList.add(m);
        }
        return pickList;
    }

    /**
     * @Description:
     * @throws
     */
    public void updataRoleByMenu(List<String> roleIds, String id) {
        List<String> changeMenuIds = Lists.newArrayList();
        List<String> childList = Lists.newArrayList();
        changeMenuIds.add(id);
        childList = getChildList(changeMenuIds); // 所有子类id
        changeMenuIds.addAll(childList);
        changeMenuIds.addAll(getParentList(id)); // 所有父类

        List<Role> roles = roleRepository.findByMenuId(new ObjectId(id));
        List<String> ids = Lists.newArrayList();
        for (Role r : roles) {
            ids.add(r.getId());
        }
        List<Role> changeRoleList = Lists.newArrayList(); // 新增的
        for (String roleId : roleIds) {
            if (ids.contains(roleId)) {
                continue;
            }
            Role role = roleRepository.findById(roleId);
            List<Menu> menus = role.getMenuList();
            List<String> menuIds = Lists.newArrayList();
            if (menus != null && roles.size() != 0) {
                for (Menu m : menus) {
                    menuIds.add(m.getId());
                }
            }
            // 不重复并集
            changeMenuIds.removeAll(menuIds);
            menuIds.addAll(changeMenuIds);

            role.setMenuList(menuRepository.findByIdIn(menuIds));
            changeRoleList.add(role);
        }
        // 被删除对应角色的
        for (String roleId : ids) {
            if (roleIds.contains(roleId)) {
                continue;
            }

            Role role = roleRepository.findById(roleId);
            List<String> menuIds = Lists.newArrayList();
            for (Menu m : role.getMenuList()) {
                menuIds.add(m.getId());
            }
            menuIds.removeAll(childList); // 删除角色对应的子集菜单

            List<String> parenList = getRemoveParentList(menuIds, id); // 要删除的父类id
            parenList.add(id);
            menuIds.removeAll(parenList);
            role.setMenuList(menuRepository.findByIdIn(menuIds));
            changeRoleList.add(role);
        }
        roleRepository.save(changeRoleList);

    }

    /**
     *   
     * @Description:根据id获取所以子节点
     * @param ids
     * @return
     * @throws
     */
    private List<String> getChildList(List<String> ids) {
        List<String> childList = Lists.newArrayList();
        for (String id : ids) {
            List<Menu> menuList = menuRepository.findByParentId(id); // 子集
            for (Menu m : menuList) {
                childList.add(m.getId());
            }
        }
        if (childList != null && childList.size() != 0) {
            childList.addAll(getChildList(childList));
        }
        return childList;
    }

    private List<String> getParentList(String id) {
        List<String> parentList = Lists.newArrayList();
        Menu menu = menuRepository.findById(id);
        id = menu.getParentId();
        if (!StringUtils.isEmpty(id)) {
            parentList.add(id);
            parentList.addAll(getParentList(id));
        }
        return parentList;
    }

    private List<String> getRemoveParentList(List<String> menuIds, String id) {
        List<String> parentList = Lists.newArrayList();
        Menu menu = menuRepository.findById(id);
        id = menu.getParentId();
        if (!StringUtils.isEmpty(id)) {
            List<Menu> list = menuRepository.findByParentId(id); // 兄弟菜单
            List<String> brotherList = Lists.newArrayList();
            for (Menu m : list) {
                if (menuIds.contains(m.getId())) {
                    brotherList.add(m.getId());
                }
            }
            if (brotherList.size() == 1) {
                parentList.add(id);
                parentList.addAll(getRemoveParentList(menuIds, id));
            }
        }
        return parentList;
    }

    /**
     * @Description:
     * @throws
     */
    public List<Menu> findAll() {
        List<Menu> list = menuRepository.findAll(new Sort(Direction.ASC, "sort"));
        List<Menu> treeList = Lists.newArrayList();
        for (Menu m : list) {
            if (StringUtils.isEmpty(m.getParentId())) {
                treeList.add(m);
                treeList.addAll(treeMenu(list, m.getId()));
            }
        }
        return treeList;
    }
}
