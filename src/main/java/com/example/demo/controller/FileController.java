package com.example.demo.controller;

import com.example.demo.common.annotation.Log;
import com.example.demo.common.result.Result;
import com.example.demo.service.MinioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "文件管理")
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final MinioService minioService;

    @Operation(summary = "上传文件")
    @Log(title = "文件管理", businessType = 1)
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        String url = minioService.upload(file);
        return Result.success(url);
    }

    @Operation(summary = "删除文件")
    @Log(title = "文件管理", businessType = 3)
    @DeleteMapping
    public Result<Void> delete(@RequestParam("fileName") String fileName) {
        minioService.delete(fileName);
        return Result.success();
    }
}
