package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.dto.MenuCreateDTO;
import com.example.demo.entity.SysMenu;
import com.example.demo.mapper.SysMenuMapper;
import com.example.demo.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<SysMenu> getMenuTree() {
        List<SysMenu> allMenus = this.list(new LambdaQueryWrapper<SysMenu>()
                .orderByAsc(SysMenu::getSortOrder));
        return buildTree(allMenus, 0L);
    }

    @Override
    public void createMenu(MenuCreateDTO dto) {
        SysMenu menu = new SysMenu();
        menu.setParentId(dto.getParentId());
        menu.setMenuName(dto.getMenuName());
        menu.setMenuType(dto.getMenuType());
        menu.setPath(dto.getPath());
        menu.setComponent(dto.getComponent());
        menu.setPermission(dto.getPermission());
        menu.setIcon(dto.getIcon());
        menu.setSortOrder(dto.getSortOrder());
        menu.setVisible(dto.getVisible());
        menu.setStatus(1);
        this.save(menu);
    }

    @Override
    public void updateMenu(Long id, MenuCreateDTO dto) {
        SysMenu menu = this.getById(id);
        if (menu == null) {
            throw new BusinessException("菜单不存在");
        }
        menu.setParentId(dto.getParentId());
        menu.setMenuName(dto.getMenuName());
        menu.setMenuType(dto.getMenuType());
        menu.setPath(dto.getPath());
        menu.setComponent(dto.getComponent());
        menu.setPermission(dto.getPermission());
        menu.setIcon(dto.getIcon());
        menu.setSortOrder(dto.getSortOrder());
        menu.setVisible(dto.getVisible());
        this.updateById(menu);
    }

    @Override
    public void deleteMenu(Long id) {
        long childCount = this.count(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, id));
        if (childCount > 0) {
            throw new BusinessException("存在子菜单，不能删除");
        }
        this.removeById(id);
    }

    @Override
    public Set<String> getPermissionsByUserId(Long userId) {
        return baseMapper.selectPermissionsByUserId(userId);
    }

    @Override
    public List<SysMenu> getMenusByUserId(Long userId) {
        List<SysMenu> allMenus = this.list(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getStatus, 1)
                .orderByAsc(SysMenu::getSortOrder));

        // 超级管理员返回所有菜单
        Set<String> permissions = baseMapper.selectPermissionsByUserId(userId);
        if (permissions.contains("*:*:*")) {
            return buildTree(allMenus, 0L);
        }

        // 根据用户菜单权限过滤
        List<SysMenu> userMenus = allMenus.stream()
                .filter(m -> m.getMenuType() != 3) // 过滤掉按钮
                .collect(Collectors.toList());

        return buildTree(userMenus, 0L);
    }

    private List<SysMenu> buildTree(List<SysMenu> menus, Long parentId) {
        return menus.stream()
                .filter(menu -> parentId.equals(menu.getParentId()))
                .peek(menu -> menu.setChildren(buildTree(menus, menu.getId())))
                .collect(Collectors.toList());
    }
}
