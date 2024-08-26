package com.example.insurancepredict.controller;

import com.example.insurancepredict.exception.BadRequestException;
import com.example.insurancepredict.exception.ResourceNotFoundException;
import com.example.insurancepredict.exception.InternalServerException;
import com.example.insurancepredict.model.Insurance;
import com.example.insurancepredict.service.InsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/insurance")
public class InsuranceController {

    @Autowired
    private InsuranceService insuranceService;

    @PostMapping("/predict")
    public ResponseEntity<Float> predictInsurance(@RequestBody Insurance insurance) {
        if (insurance.getAge() < 0) {
            throw new BadRequestException("Age cannot be negative");
        }
        try {
            float prediction = insuranceService.predictInsurance(insurance);
            return ResponseEntity.ok(prediction);
        } catch (Exception e) {
            throw new InternalServerException("Error occurred while predicting insurance");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Insurance> getInsurance(@PathVariable Long id) {
        Insurance insurance = insuranceService.getInsuranceById(id);
        if (insurance == null) {
            throw new ResourceNotFoundException("Insurance not found with id: " + id);
        }
        return ResponseEntity.ok(insurance);
    }

    @GetMapping("/test-error")
    public void testError(@RequestParam("type") String errorType) {
        switch (errorType) {
            case "404":
                throw new ResourceNotFoundException("Resource not found");
            case "400":
                throw new BadRequestException("Bad request");
            case "500":
                throw new InternalServerException("Internal server error");
            default:
                throw new BadRequestException("Invalid error type");
        }
    }

    @GetMapping("/test-400")
    public ResponseEntity<String> test400(@RequestParam("age") int age) {
        if (age < 0) {
            throw new BadRequestException("Age cannot be negative");
        }
        return ResponseEntity.ok("Valid age: " + age);
    }

//test 404
//    @GetMapping("/{id}")
//    public ResponseEntity<Insurance> getInsuranceById(@PathVariable Long id) {
//        Insurance insurance = insuranceService.getInsuranceById(id);
//        if (insurance == null) {
//            throw new ResourceNotFoundException("Insurance not found with id: " + id);
//        }
//        return ResponseEntity.ok(insurance);
//    }

    @GetMapping("/test-500")
    public ResponseEntity<String> test500() {
        throw new RuntimeException("Simulated internal server error");
    }
}