package com.example.studentService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateLimiterUpdate implements Serializable {
    private String rateLimiterName;
    private int newLimitForPeriod;
    private Duration newTimeoutDuration;
}
