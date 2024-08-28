package com.example.insurancepredict.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceResponseDto {
    private Long id;
    private int age;
    private float bmi;
    private int children;
    private String sex;
    private String smoker;
    private String region;
    private float predictedCharge;
}