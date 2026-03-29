package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.common.page.PageResult;
import com.example.demo.dto.UserCreateDTO;
import com.example.demo.dto.UserQueryDTO;
import com.example.demo.dto.UserUpdateDTO;
import com.example.demo.entity.SysUser;
import com.example.demo.vo.UserVO;

public interface SysUserService extends IService<SysUser> {

    PageResult<UserVO> pageList(UserQueryDTO query);

    UserVO getUserDetail(Long id);

    void createUser(UserCreateDTO dto);

    void updateUser(Long id, UserUpdateDTO dto);

    void deleteUser(Long id);

    void resetPassword(Long id);

    void assignRoles(Long userId, java.util.List<Long> roleIds);
}
