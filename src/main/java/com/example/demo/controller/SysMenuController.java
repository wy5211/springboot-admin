package com.example.demo.controller;

import com.example.demo.common.annotation.Log;
import com.example.demo.common.result.Result;
import com.example.demo.dto.MenuCreateDTO;
import com.example.demo.entity.SysMenu;
import com.example.demo.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "菜单管理")
@RestController
@RequestMapping("/api/system/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService menuService;

    @Operation(summary = "菜单树")
    @PreAuthorize("hasAuthority('system:menu:list')")
    @GetMapping("/tree")
    public Result<List<SysMenu>> tree() {
        return Result.success(menuService.getMenuTree());
    }

    @Operation(summary = "新增菜单")
    @Log(title = "菜单管理", businessType = 1)
    @PreAuthorize("hasAuthority('system:menu:add')")
    @PostMapping
    public Result<Void> create(@Valid @RequestBody MenuCreateDTO dto) {
        menuService.createMenu(dto);
        return Result.success();
    }

    @Operation(summary = "修改菜单")
    @Log(title = "菜单管理", businessType = 2)
    @PreAuthorize("hasAuthority('system:menu:edit')")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody MenuCreateDTO dto) {
        menuService.updateMenu(id, dto);
        return Result.success();
    }

    @Operation(summary = "删除菜单")
    @Log(title = "菜单管理", businessType = 3)
    @PreAuthorize("hasAuthority('system:menu:delete')")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return Result.success();
    }

    @Operation(summary = "当前用户菜单")
    @GetMapping("/user-menus")
    public Result<List<SysMenu>> userMenus() {
        return Result.success(menuService.getMenusByUserId(1L)); // TODO: 从 SecurityContext 获取当前用户
    }
}
