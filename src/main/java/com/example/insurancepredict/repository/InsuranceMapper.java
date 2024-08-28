package com.example.insurancepredict.repository;

import com.example.insurancepredict.model.Insurance;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InsuranceMapper {
    void insertPrediction(Insurance insurance);
    Insurance selectInsuranceById(Long id);
}

