package com.example.insurancepredict.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Insurance {
    private Long id;
    private int age;
    private float bmi;
    private int children;
    private String sex;
    private String smoker;
    private String region;
    private float predictedCharge;
    private LocalDateTime predictionDate;
}