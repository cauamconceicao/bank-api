package com.bankapi.service;

import com.bankapi.dto.request.DepositRequest;
import com.bankapi.dto.request.TransferRequest;
import com.bankapi.dto.request.WithdrawRequest;
import com.bankapi.dto.response.TransactionResponse;
import com.bankapi.exception.BusinessException;
import com.bankapi.exception.InsufficientFundsException;
import com.bankapi.model.Account;
import com.bankapi.model.Transaction;
import com.bankapi.model.TransactionType;
import com.bankapi.model.User;
import com.bankapi.repository.AccountRepository;
import com.bankapi.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @Transactional
    public TransactionResponse deposit(User user, DepositRequest request) {
        Account account = accountService.findAccountByUser(user);
        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .type(TransactionType.DEPOSIT)
                .amount(request.getAmount())
                .targetAccount(account)
                .description("Depósito")
                .build();

        return mapToResponse(transactionRepository.save(transaction));
    }

    @Transactional
    public TransactionResponse withdraw(User user, WithdrawRequest request) {
        Account account = accountService.findAccountByUser(user);

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Saldo insuficiente");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .type(TransactionType.WITHDRAW)
                .amount(request.getAmount())
                .sourceAccount(account)
                .description("Saque")
                .build();

        return mapToResponse(transactionRepository.save(transaction));
    }

    @Transactional
    public TransactionResponse transfer(User user, TransferRequest request) {
        Account sourceAccount = accountService.findAccountByUser(user);
        Account targetAccount = accountService.findAccountByNumber(request.getTargetAccountNumber());

        if (sourceAccount.getAccountNumber().equals(targetAccount.getAccountNumber())) {
            throw new BusinessException("Não é possível transferir para a própria conta");
        }

        if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Saldo insuficiente");
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
        targetAccount.setBalance(targetAccount.getBalance().add(request.getAmount()));

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        Transaction transaction = Transaction.builder()
                .type(TransactionType.TRANSFER_SENT)
                .amount(request.getAmount())
                .sourceAccount(sourceAccount)
                .targetAccount(targetAccount)
                .description(request.getDescription() != null ? request.getDescription() : "Transferência")
                .build();

        return mapToResponse(transactionRepository.save(transaction));
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getStatement(User user) {
        Account account = accountService.findAccountByUser(user);
        return transactionRepository.findAllByAccount(account)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .sourceAccountNumber(transaction.getSourceAccount() != null
                        ? transaction.getSourceAccount().getAccountNumber() : null)
                .targetAccountNumber(transaction.getTargetAccount() != null
                        ? transaction.getTargetAccount().getAccountNumber() : null)
                .description(transaction.getDescription())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
