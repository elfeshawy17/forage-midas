package com.jpmc.midascore.service.balance;

import com.jpmc.midascore.foundation.Balance;

public interface BalanceService {
    Balance queryBalance(Long userId);
}