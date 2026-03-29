package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.SysRole;
import com.example.demo.dto.RoleCreateDTO;
import com.example.demo.dto.RoleUpdateDTO;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    List<SysRole> listAll();

    void createRole(RoleCreateDTO dto);

    void updateRole(Long id, RoleUpdateDTO dto);

    void deleteRole(Long id);

    List<SysRole> getRolesByUserId(Long userId);

    void assignMenus(Long roleId, List<Long> menuIds);
}
