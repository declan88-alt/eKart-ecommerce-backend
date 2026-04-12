package com.vedasole.ekartecommercebackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/monitoring")
public class MonitoringController {

    @GetMapping
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("eKart Backend is running smoothly with fork repository by Group 18");
    }

}
