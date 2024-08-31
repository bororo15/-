package com.example.insurancepredict.controller;

import com.example.insurancepredict.dto.InsurancePredictionRequestDto;
import com.example.insurancepredict.dto.InsuranceResponseDto;
import com.example.insurancepredict.exception.BadRequestException;
import com.example.insurancepredict.exception.ResourceNotFoundException;
import com.example.insurancepredict.exception.InternalServerException;
import com.example.insurancepredict.service.InsuranceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/insurance")
@RequiredArgsConstructor
public class InsuranceController {

    private final InsuranceService insuranceService;
    private static final Logger logger = LoggerFactory.getLogger(InsuranceController.class);


    @PostMapping("/predict")
    public ResponseEntity<Float> predictInsurance(@RequestBody InsurancePredictionRequestDto requestDto) {
        if (requestDto.getAge() < 0) {
            throw new BadRequestException("Age cannot be negative");
        }
        try {
            float prediction = insuranceService.predictInsurance(requestDto);
            return ResponseEntity.ok(prediction);
        } catch (Exception e) {
            logger.error("Error occurred while predicting insurance", e);
            throw new InternalServerException("Error occurred while predicting insurance");
            // throw new InternalServerException("Error occurred while predicting insurance");
        }
    }

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome to Insurance Prediction API");
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsuranceResponseDto> getInsurance(@PathVariable Long id) {
        InsuranceResponseDto insurance = insuranceService.getInsuranceById(id);
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

    @GetMapping("/test-500")
    public ResponseEntity<String> test500() {
        throw new InternalServerException("Simulated internal server error");
    }
}