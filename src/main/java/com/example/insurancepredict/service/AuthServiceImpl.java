package com.example.insurancepredict.service;

import com.example.insurancepredict.model.Insurance;
import com.example.insurancepredict.repository.InsuranceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final InsuranceMapper insuranceMapper;

    public AuthServiceImpl(InsuranceMapper insuranceMapper) {
        this.insuranceMapper = insuranceMapper;
    }

    @Override
    public Insurance findCredentialByAccessToken(String accessToken) {
        Insurance userInfo = null;
        try {
            userInfo = insuranceMapper.findUserByAccessToken(accessToken);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return userInfo;
    }
}