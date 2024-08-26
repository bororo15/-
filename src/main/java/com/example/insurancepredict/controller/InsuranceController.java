package com.example.insurancepredict.controller;

import com.example.insurancepredict.model.Insurance;
import com.example.insurancepredict.service.InsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/insurance")
public class InsuranceController {

    @Autowired
    private InsuranceService insuranceService;

    @PostMapping("/predict")
    public float predictInsurance(@RequestBody Insurance insurance) {
        return insuranceService.predictInsurance(insurance);
    }
}