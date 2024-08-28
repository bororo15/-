package com.example.insurancepredict.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsurancePredictionRequestDto {
    private int age;
    private float bmi;
    private int children;
    private String sex;
    private String smoker;
    private String region;
}
