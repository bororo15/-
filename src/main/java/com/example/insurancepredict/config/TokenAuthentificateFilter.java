package com.example.insurancepredict.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.insurancepredict.model.Insurance;
import com.example.insurancepredict.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenAuthentificateFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthentificateFilter.class);
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    public TokenAuthentificateFilter(AuthService authService, ObjectMapper objectMapper) {
        this.authService = authService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        boolean isValid = false;

        // 1. 세션의 정보를 찾아서 유효한 세션인지 검증
        if (!ObjectUtils.isEmpty(request.getSession(true).getAttribute("is_valid"))) {
            isValid = (boolean) request.getSession(true).getAttribute("is_valid");
        }

        // 1-1 세션이 유효하면 다음단계로 진행
        if (isValid) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. authorization 이 존재하는지
        if (authorization == null || authorization.isEmpty()) {
            request.getSession(true).setAttribute("is_valid", false);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseError(response, 403);
            return;
        }

        String[] tempArr = authorization.split(" ");

        // 3. Bearer, accesstoken 이 존재하는지
        if (tempArr == null || tempArr.length != 2) {
            request.getSession(true).setAttribute("is_valid", false);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseError(response, 403);
            return;
        }

        String accessTokenPrefix = tempArr[0];
        String accessToken = tempArr[1];

        // 4. Bearer 값이 들어왔는지
        if (accessTokenPrefix == null || !accessTokenPrefix.equals("Bearer")) {
            request.getSession(true).setAttribute("is_valid", false);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseError(response, 403);
            return;
        }

        // 5. accessToken 값이 들어왔는지
        if (accessToken == null || accessToken.isEmpty()) {
            request.getSession(true).setAttribute("is_valid", false);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseError(response, 401);
            return;
        } else {
            // 6. accessToken 값으로 DB조회해서 유효한지
            Insurance userInfo = authService.findCredentialByAccessToken(accessToken);

            if (userInfo == null) {
                // accessToken이 디비에 조회되지않음
                request.getSession(true).setAttribute("is_valid", false);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                responseError(response, 401);
                return;
            }

            if (userInfo.isExpired()) {
                // accessToken 만료
                request.getSession(true).setAttribute("is_valid", false);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                responseError(response, 401);
                return;
            } else {
                // 7. accessToken이 유효 함 세션에 저장후 반환
                request.getSession(true).setAttribute("is_valid", true);
                request.getSession(true).setAttribute("principal_name", userInfo.getPrincipalName());
                request.getSession(true).setAttribute("registered_client_id", userInfo.getRegisteredClientId());
                filterChain.doFilter(request, response);
            }
        }
    }

    private void responseError(HttpServletResponse response, int code) throws IOException {
        String jsonContentType = "application/json;charset=UTF-8";
        Map<String, Object> responseMessage = new HashMap<>();
        response.setContentType(jsonContentType);

        responseMessage.put("taskTag", "");
        responseMessage.put("taskResult", new ArrayList<>());

        switch (code) {
            case 401:
                responseMessage.put("message", "Invalid token");
                responseMessage.put("code", "401");
                break;
            case 403:
                responseMessage.put("message", "Access forbidden");
                responseMessage.put("code", "403");
                break;
            default:
                responseMessage.put("message", "Unknown error");
                responseMessage.put("code", "500");
                break;
        }

        objectMapper.writeValue(response.getWriter(), responseMessage);
    }
}