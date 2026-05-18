package com.bankapi.dto.response;

import com.bankapi.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private TransactionType type;
    private BigDecimal amount;
    private String sourceAccountNumber;
    private String targetAccountNumber;
    private String description;
    private LocalDateTime createdAt;
}
