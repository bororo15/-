package com.example.insurancepredict.service;

import com.example.insurancepredict.model.Insurance;

public interface AuthService {
    Insurance findCredentialByAccessToken(String accessToken);
}