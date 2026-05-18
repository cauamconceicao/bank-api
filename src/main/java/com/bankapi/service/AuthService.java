package com.bankapi.service;

import com.bankapi.dto.request.LoginRequest;
import com.bankapi.dto.request.RegisterRequest;
import com.bankapi.dto.response.AuthResponse;
import com.bankapi.exception.BusinessException;
import com.bankapi.model.Account;
import com.bankapi.model.User;
import com.bankapi.repository.AccountRepository;
import com.bankapi.repository.UserRepository;
import com.bankapi.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        try {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new BusinessException("Email já cadastrado");
            }
            if (userRepository.existsByCpf(request.getCpf())) {
                throw new BusinessException("CPF já cadastrado");
            }

            User user = User.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .cpf(request.getCpf())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();

            userRepository.save(user);
            log.debug("Usuário salvo com id={}", user.getId());

            Account account = Account.builder()
                    .accountNumber(generateAccountNumber())
                    .agency("0001")
                    .balance(BigDecimal.ZERO)
                    .user(user)
                    .build();

            accountRepository.save(account);
            log.debug("Conta criada: número={}", account.getAccountNumber());

            String token = jwtService.generateToken(user);
            log.debug("Token JWT gerado com sucesso para email={}", user.getEmail());

            return AuthResponse.builder()
                    .token(token)
                    .type("Bearer")
                    .email(user.getEmail())
                    .name(user.getName())
                    .build();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro no registro: ", e);
            throw e;
        }
    }

    public AuthResponse login(LoginRequest request) {
        try {
            log.debug("Tentativa de login para email={}", request.getEmail());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            log.debug("Autenticação bem-sucedida para email={}", request.getEmail());

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
            log.debug("Usuário encontrado: id={}", user.getId());

            String token = jwtService.generateToken(user);
            log.debug("Token JWT gerado com sucesso para email={}", user.getEmail());

            return AuthResponse.builder()
                    .token(token)
                    .type("Bearer")
                    .email(user.getEmail())
                    .name(user.getName())
                    .build();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro no login: ", e);
            throw e;
        }
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }
}
