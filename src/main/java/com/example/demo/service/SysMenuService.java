package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.dto.MenuCreateDTO;
import com.example.demo.entity.SysMenu;

import java.util.List;
import java.util.Set;

public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> getMenuTree();

    void createMenu(MenuCreateDTO dto);

    void updateMenu(Long id, MenuCreateDTO dto);

    void deleteMenu(Long id);

    Set<String> getPermissionsByUserId(Long userId);

    List<SysMenu> getMenusByUserId(Long userId);
}
