package com.example.demo.controller;

import com.example.demo.common.annotation.Log;
import com.example.demo.common.result.Result;
import com.example.demo.common.page.PageResult;
import com.example.demo.dto.AssignRoleDTO;
import com.example.demo.dto.UserCreateDTO;
import com.example.demo.dto.UserQueryDTO;
import com.example.demo.dto.UserUpdateDTO;
import com.example.demo.service.SysUserService;
import com.example.demo.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/system/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;

    @Operation(summary = "分页查询用户")
    @PreAuthorize("hasAuthority('system:user:list')")
    @GetMapping("/page")
    public Result<PageResult<UserVO>> page(UserQueryDTO query) {
        return Result.success(userService.pageList(query));
    }

    @Operation(summary = "用户详情")
    @PreAuthorize("hasAuthority('system:user:list')")
    @GetMapping("/{id}")
    public Result<UserVO> detail(@PathVariable Long id) {
        return Result.success(userService.getUserDetail(id));
    }

    @Operation(summary = "新增用户")
    @Log(title = "用户管理", businessType = 1)
    @PreAuthorize("hasAuthority('system:user:add')")
    @PostMapping
    public Result<Void> create(@Valid @RequestBody UserCreateDTO dto) {
        userService.createUser(dto);
        return Result.success();
    }

    @Operation(summary = "修改用户")
    @Log(title = "用户管理", businessType = 2)
    @PreAuthorize("hasAuthority('system:user:edit')")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto) {
        userService.updateUser(id, dto);
        return Result.success();
    }

    @Operation(summary = "删除用户")
    @Log(title = "用户管理", businessType = 3)
    @PreAuthorize("hasAuthority('system:user:delete')")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }

    @Operation(summary = "重置密码")
    @Log(title = "用户管理", businessType = 2)
    @PreAuthorize("hasAuthority('system:user:edit')")
    @PutMapping("/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id);
        return Result.success();
    }

    @Operation(summary = "分配角色")
    @Log(title = "用户管理", businessType = 2)
    @PreAuthorize("hasAuthority('system:user:edit')")
    @PostMapping("/assign-roles")
    public Result<Void> assignRoles(@Valid @RequestBody AssignRoleDTO dto) {
        userService.assignRoles(dto.getUserId(), dto.getRoleIds());
        return Result.success();
    }
}
