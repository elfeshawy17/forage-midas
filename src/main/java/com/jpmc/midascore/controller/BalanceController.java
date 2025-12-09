package com.jpmc.midascore.controller;

import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.service.balance.BalanceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/balance")
public class BalanceController {
    private final BalanceService balanceService;

    BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping()
    public Balance query(@RequestParam Long userId) {
        return balanceService.queryBalance(userId);
    }
}