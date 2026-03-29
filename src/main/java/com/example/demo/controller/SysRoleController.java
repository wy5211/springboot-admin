package com.example.demo.controller;

import com.example.demo.common.annotation.Log;
import com.example.demo.common.result.Result;
import com.example.demo.dto.AssignMenuDTO;
import com.example.demo.dto.RoleCreateDTO;
import com.example.demo.dto.RoleUpdateDTO;
import com.example.demo.entity.SysRole;
import com.example.demo.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "角色管理")
@RestController
@RequestMapping("/api/system/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService roleService;

    @Operation(summary = "角色列表")
    @PreAuthorize("hasAuthority('system:role:list')")
    @GetMapping("/list")
    public Result<List<SysRole>> list() {
        return Result.success(roleService.listAll());
    }

    @Operation(summary = "新增角色")
    @Log(title = "角色管理", businessType = 1)
    @PreAuthorize("hasAuthority('system:role:add')")
    @PostMapping
    public Result<Void> create(@Valid @RequestBody RoleCreateDTO dto) {
        roleService.createRole(dto);
        return Result.success();
    }

    @Operation(summary = "修改角色")
    @Log(title = "角色管理", businessType = 2)
    @PreAuthorize("hasAuthority('system:role:edit')")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody RoleUpdateDTO dto) {
        roleService.updateRole(id, dto);
        return Result.success();
    }

    @Operation(summary = "删除角色")
    @Log(title = "角色管理", businessType = 3)
    @PreAuthorize("hasAuthority('system:role:delete')")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return Result.success();
    }

    @Operation(summary = "分配菜单权限")
    @Log(title = "角色管理", businessType = 2)
    @PreAuthorize("hasAuthority('system:role:edit')")
    @PostMapping("/assign-menus")
    public Result<Void> assignMenus(@Valid @RequestBody AssignMenuDTO dto) {
        roleService.assignMenus(dto.getRoleId(), dto.getMenuIds());
        return Result.success();
    }
}
