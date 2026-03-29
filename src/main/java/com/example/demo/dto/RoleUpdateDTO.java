package com.example.demo.dto;

import lombok.Data;

@Data
public class RoleUpdateDTO {

    private String roleName;

    private String roleCode;

    private Integer sortOrder;

    private Integer status;

    private String remark;
}
