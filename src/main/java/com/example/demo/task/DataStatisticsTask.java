package com.example.demo.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataStatisticsTask {

    // 每天凌晨3点执行
    @Scheduled(cron = "0 0 3 * * ?")
    public void dailyStatistics() {
        log.info("=== 开始执行每日数据统计 ===");
        // TODO: 统计每日新增用户数、活跃用户数、接口调用次数等
        log.info("=== 每日数据统计完成 ===");
    }
}
