package com.bankapi.controller;

import com.bankapi.dto.request.DepositRequest;
import com.bankapi.dto.request.TransferRequest;
import com.bankapi.dto.request.WithdrawRequest;
import com.bankapi.dto.response.ApiResponse;
import com.bankapi.dto.response.TransactionResponse;
import com.bankapi.model.User;
import com.bankapi.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<TransactionResponse>> deposit(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody DepositRequest request) {
        TransactionResponse response = transactionService.deposit(user, request);
        return ResponseEntity.ok(ApiResponse.success("Depósito realizado com sucesso", response));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<TransactionResponse>> withdraw(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody WithdrawRequest request) {
        TransactionResponse response = transactionService.withdraw(user, request);
        return ResponseEntity.ok(ApiResponse.success("Saque realizado com sucesso", response));
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransactionResponse>> transfer(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody TransferRequest request) {
        TransactionResponse response = transactionService.transfer(user, request);
        return ResponseEntity.ok(ApiResponse.success("Transferência realizada com sucesso", response));
    }

    @GetMapping("/statement")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getStatement(
            @AuthenticationPrincipal User user) {
        List<TransactionResponse> response = transactionService.getStatement(user);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
