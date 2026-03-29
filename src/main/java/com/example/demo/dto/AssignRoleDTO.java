package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AssignRoleDTO {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    private List<Long> roleIds;
}
