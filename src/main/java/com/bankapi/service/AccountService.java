package com.bankapi.service;

import com.bankapi.dto.response.AccountResponse;
import com.bankapi.dto.response.BalanceResponse;
import com.bankapi.exception.ResourceNotFoundException;
import com.bankapi.model.Account;
import com.bankapi.model.User;
import com.bankapi.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public AccountResponse getAccountDetails(User user) {
        Account account = findAccountByUser(user);
        return mapToResponse(account);
    }

    @Transactional(readOnly = true)
    public BalanceResponse getBalance(User user) {
        Account account = findAccountByUser(user);
        return BalanceResponse.builder()
                .accountNumber(account.getAccountNumber())
                .agency(account.getAgency())
                .balance(account.getBalance())
                .build();
    }

    public Account findAccountByUser(User user) {
        return accountRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada para o usuário: " + user.getEmail()));
    }

    public Account findAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada: " + accountNumber));
    }

    private AccountResponse mapToResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .agency(account.getAgency())
                .balance(account.getBalance())
                .ownerName(account.getUser().getName())
                .ownerEmail(account.getUser().getEmail())
                .createdAt(account.getCreatedAt())
                .build();
    }
}
