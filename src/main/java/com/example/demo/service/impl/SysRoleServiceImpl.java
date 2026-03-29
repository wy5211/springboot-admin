package com.example.demo.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.dto.RoleCreateDTO;
import com.example.demo.dto.RoleUpdateDTO;
import com.example.demo.entity.SysRole;
import com.example.demo.entity.SysRoleMenu;
import com.example.demo.mapper.SysRoleMapper;
import com.example.demo.mapper.SysRoleMenuMapper;
import com.example.demo.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMenuMapper roleMenuMapper;

    @Override
    public List<SysRole> listAll() {
        return this.list(new LambdaQueryWrapper<SysRole>().orderByAsc(SysRole::getSortOrder));
    }

    @Override
    @Transactional
    public void createRole(RoleCreateDTO dto) {
        long count = this.count(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleCode, dto.getRoleCode()));
        if (count > 0) {
            throw new BusinessException("角色编码已存在");
        }
        SysRole role = new SysRole();
        role.setRoleName(dto.getRoleName());
        role.setRoleCode(dto.getRoleCode());
        role.setSortOrder(dto.getSortOrder());
        role.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        role.setRemark(dto.getRemark());
        this.save(role);
    }

    @Override
    @Transactional
    public void updateRole(Long id, RoleUpdateDTO dto) {
        SysRole role = this.getById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        if (StrUtil.isNotBlank(dto.getRoleCode()) && !dto.getRoleCode().equals(role.getRoleCode())) {
            long count = this.count(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleCode, dto.getRoleCode()));
            if (count > 0) {
                throw new BusinessException("角色编码已存在");
            }
        }
        if (StrUtil.isNotBlank(dto.getRoleName())) role.setRoleName(dto.getRoleName());
        if (StrUtil.isNotBlank(dto.getRoleCode())) role.setRoleCode(dto.getRoleCode());
        if (dto.getSortOrder() != null) role.setSortOrder(dto.getSortOrder());
        if (dto.getStatus() != null) role.setStatus(dto.getStatus());
        if (dto.getRemark() != null) role.setRemark(dto.getRemark());
        this.updateById(role);
    }

    @Override
    public void deleteRole(Long id) {
        SysRole role = this.getById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        if ("SUPER_ADMIN".equals(role.getRoleCode())) {
            throw new BusinessException("不能删除超级管理员角色");
        }
        this.removeById(id);
    }

    @Override
    public List<SysRole> getRolesByUserId(Long userId) {
        return baseMapper.selectRolesByUserId(userId);
    }

    @Override
    @Transactional
    public void assignMenus(Long roleId, List<Long> menuIds) {
        SysRole role = this.getById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        // 先删除旧的关联
        roleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>()
                .eq(SysRoleMenu::getRoleId, roleId));
        // 再插入新的关联
        if (menuIds != null && !menuIds.isEmpty()) {
            for (Long menuId : menuIds) {
                SysRoleMenu rm = new SysRoleMenu();
                rm.setRoleId(roleId);
                rm.setMenuId(menuId);
                roleMenuMapper.insert(rm);
            }
        }
    }
}
