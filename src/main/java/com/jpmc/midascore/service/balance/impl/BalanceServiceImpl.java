package com.jpmc.midascore.service.balance.impl;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.service.balance.BalanceService;
import org.springframework.stereotype.Service;

@Service
public class BalanceServiceImpl implements BalanceService {

    private final DatabaseConduit databaseConduit;

    public BalanceServiceImpl(DatabaseConduit databaseConduit) {
        this.databaseConduit = databaseConduit;
    }

    @Override
    public Balance queryBalance(Long userId) {
        UserRecord user = databaseConduit.findUserById(userId);
        return new Balance(user.getBalance());
    }
}