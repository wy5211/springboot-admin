package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MenuCreateDTO {

    private Long parentId = 0L;

    @NotBlank(message = "菜单名称不能为空")
    private String menuName;

    @NotNull(message = "菜单类型不能为空")
    private Integer menuType;

    private String path;

    private String component;

    private String permission;

    private String icon;

    private Integer sortOrder = 0;

    private Integer visible = 1;
}
