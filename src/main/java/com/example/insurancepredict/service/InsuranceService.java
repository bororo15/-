package com.example.insurancepredict.service;

import com.example.insurancepredict.model.Insurance;
import com.example.insurancepredict.repository.InsuranceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class InsuranceService {

    @Autowired
    private InsuranceMapper insuranceMapper;

    @Autowired
    private RestTemplate restTemplate;

    public float predictInsurance(Insurance insurance) {
        // FastAPI 서버에 예측 요청
        String fastApiUrl = "http://localhost:9999/predict";
        float prediction = restTemplate.postForObject(fastApiUrl, insurance, Float.class);

        // 예측 결과 저장
        insurance.setPredictedCharge(prediction);
        insuranceMapper.insertPrediction(insurance);

        return prediction;
    }
}