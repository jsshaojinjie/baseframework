package com.baseframework.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @PostMapping("/test")
    public String test() {
        return "test";
    }
}
