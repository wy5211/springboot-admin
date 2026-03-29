package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.page.PageResult;
import com.example.demo.entity.SysOperLog;
import com.example.demo.mapper.SysOperLogMapper;
import com.example.demo.service.SysOperLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements SysOperLogService {

    @Override
    @Async
    public void saveLog(SysOperLog operLog) {
        try {
            this.save(operLog);
        } catch (Exception e) {
            log.error("保存操作日志失败", e);
        }
    }

    @Override
    public PageResult<SysOperLog> pageList(int pageNum, int pageSize) {
        Page<SysOperLog> page = Page.of(pageNum, pageSize);
        Page<SysOperLog> result = this.page(page, null);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }
}
