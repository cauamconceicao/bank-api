package com.bankapi.controller;

import com.bankapi.dto.response.AccountResponse;
import com.bankapi.dto.response.ApiResponse;
import com.bankapi.dto.response.BalanceResponse;
import com.bankapi.model.User;
import com.bankapi.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AccountResponse>> getMyAccount(@AuthenticationPrincipal User user) {
        AccountResponse response = accountService.getAccountDetails(user);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/balance")
    public ResponseEntity<ApiResponse<BalanceResponse>> getBalance(@AuthenticationPrincipal User user) {
        BalanceResponse response = accountService.getBalance(user);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
