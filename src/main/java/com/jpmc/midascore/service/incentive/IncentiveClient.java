package com.jpmc.midascore.service.incentive;

import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IncentiveClient {

    private static final String INCENTIVE_URL = "http://localhost:8080/incentive";
    private final RestTemplate restTemplate;

    public IncentiveClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Balance getIncentive(Transaction transaction) {
        return restTemplate.postForObject(
                INCENTIVE_URL,
                transaction,
                Balance.class
        );
    }
}