package org.mihaimadan.wwi.users.service;

import org.mihaimadan.wwi.users.repository.PasswordTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class TokenSchedulingService {
    private final PasswordTokenRepository passwordTokenRepository;

    public TokenSchedulingService(PasswordTokenRepository passwordTokenRepository) {
        this.passwordTokenRepository = passwordTokenRepository;
    }

    @Scheduled(cron = "* 0/15 * * * *")
    public void removeExpiredTokens() {
        passwordTokenRepository.findAll().stream()
                .filter(it -> ChronoUnit.MINUTES.between(it.getCreatedTime(), LocalDateTime.now()) > 15)
                .forEach(passwordTokenRepository::delete);
    }
}
