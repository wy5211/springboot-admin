package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.common.page.PageResult;
import com.example.demo.entity.SysOperLog;

public interface SysOperLogService extends IService<SysOperLog> {

    void saveLog(SysOperLog operLog);

    PageResult<SysOperLog> pageList(int pageNum, int pageSize);
}
