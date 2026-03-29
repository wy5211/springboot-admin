package com.example.demo.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CleanTokenTask {

    // 每天凌晨2点执行
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanExpiredTokens() {
        log.info("=== 开始清理过期 Token ===");
        // TODO: 从 Redis 中清理过期的 Token 黑名单
        // 这里可以扩展 Token 黑名单机制，定期清理过期的记录
        log.info("=== 过期 Token 清理完成 ===");
    }
}
