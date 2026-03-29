package com.example.demo.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserVO {

    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
    private Long deptId;
    private Integer status;
    private LocalDateTime createTime;
    private List<Long> roleIds;
    private List<String> roleNames;
}
