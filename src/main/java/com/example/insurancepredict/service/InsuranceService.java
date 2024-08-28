package com.example.insurancepredict.service;

import com.example.insurancepredict.dto.InsurancePredictionRequestDto;
import com.example.insurancepredict.dto.InsuranceResponseDto;
import com.example.insurancepredict.model.Insurance;
import com.example.insurancepredict.repository.InsuranceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class InsuranceService {

    private final InsuranceMapper insuranceMapper;
    private final RestTemplate restTemplate;

    public float predictInsurance(InsurancePredictionRequestDto requestDto) {
        String fastApiUrl = "http://localhost:9999/predict";
        float prediction = restTemplate.postForObject(fastApiUrl, requestDto, Float.class);

        Insurance insurance = convertToInsurance(requestDto);
        insurance.setPredictedCharge(prediction);
        insuranceMapper.insertPrediction(insurance);

        return prediction;
    }

    public InsuranceResponseDto getInsuranceById(Long id) {
        Insurance insurance = insuranceMapper.selectInsuranceById(id);
        return insurance != null ? convertToResponseDto(insurance) : null;
    }

    private Insurance convertToInsurance(InsurancePredictionRequestDto requestDto) {
        Insurance insurance = new Insurance();
        insurance.setAge(requestDto.getAge());
        insurance.setBmi(requestDto.getBmi());
        insurance.setChildren(requestDto.getChildren());
        insurance.setSex(requestDto.getSex());
        insurance.setSmoker(requestDto.getSmoker());
        insurance.setRegion(requestDto.getRegion());
        return insurance;
    }
    private InsuranceResponseDto convertToResponseDto(Insurance insurance) {
        InsuranceResponseDto responseDto = new InsuranceResponseDto();
        responseDto.setId(insurance.getId());
        responseDto.setAge(insurance.getAge());
        responseDto.setBmi(insurance.getBmi());
        responseDto.setChildren(insurance.getChildren());
        responseDto.setSex(insurance.getSex());
        responseDto.setSmoker(insurance.getSmoker());
        responseDto.setRegion(insurance.getRegion());
        responseDto.setPredictedCharge(insurance.getPredictedCharge());
        return responseDto;
    }
}