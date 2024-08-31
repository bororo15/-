package com.example.insurancepredict.model;

import lombok.Getter;
import lombok.Setter;

public class Insurance {
    @Getter @Setter
    private Long id;

    @Getter @Setter
    private int age;

    @Getter @Setter
    private double bmi;

    @Getter @Setter
    private int children;

    @Getter @Setter
    private String sex;

    @Getter @Setter
    private boolean smoker;

    @Getter @Setter
    private String region;

    @Getter @Setter
    private double predictedCharge;

    @Getter @Setter
    private boolean expired;

    @Getter @Setter
    private String principalName;

    @Getter @Setter
    private String registeredClientId;

    public Insurance(int age, double bmi, int children, String sex, boolean smoker, String region) {
        this.age = age;
        this.bmi = bmi;
        this.children = children;
        this.sex = sex;
        this.smoker = smoker;
        this.region = region;
    }

    public Insurance() {
    }

    public void setPredictedCharge(double predictedCharge) {
        this.predictedCharge = predictedCharge;
    }


    public boolean getSmoker() {
        return smoker;
    }
}