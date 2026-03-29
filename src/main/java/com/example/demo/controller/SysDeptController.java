package com.example.demo.controller;

import com.example.demo.common.result.Result;
import com.example.demo.entity.SysDept;
import com.example.demo.service.SysDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "部门管理")
@RestController
@RequestMapping("/api/system/dept")
@RequiredArgsConstructor
public class SysDeptController {

    private final SysDeptService deptService;

    @Operation(summary = "部门树")
    @PreAuthorize("hasAuthority('system:dept:list')")
    @GetMapping("/tree")
    public Result<List<SysDept>> tree() {
        return Result.success(deptService.getDeptTree());
    }
}
