package com.programmingtechie.zalo_oa_service.controller.token;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import com.programmingtechie.zalo_oa_service.dto.response.token.TokenResponse;
import com.programmingtechie.zalo_oa_service.service.token.TokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/zalo-oa/token")
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {

        // Tạo body của response
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false)); // đường dẫn của request

        // Trả về response với mã 500
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<TokenResponse> getAccessToken() {
        return ResponseEntity.ok(tokenService.getTokenResponse());
    }

    @GetMapping("/generate-token")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<TokenResponse> generateToken() {
        return ResponseEntity.ok(tokenService.generateToken());
    }

    @GetMapping("/generate-token/authorization-code")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<TokenResponse> generateTokenByAuthorizationCode(
            @RequestParam(value = "authorizationCode") String authorizationCode) {
        return ResponseEntity.ok(tokenService.generateTokenByAuthorizationCode(authorizationCode));
    }

    @GetMapping("/refresh_token")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<TokenResponse> refreshToken() {
        return ResponseEntity.ok(tokenService.refreshToken());
    }
}
