package com.example.demo.controller;

import com.example.demo.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "演示接口")
@RestController
@RequestMapping("/api")
public class DemoController {

    @Operation(summary = "Hello World")
    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("Hello, Spring Boot!");
    }
}
