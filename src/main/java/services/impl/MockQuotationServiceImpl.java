package services.impl;

import models.Vehicle;
import services.QuotationService;

import java.util.concurrent.CompletableFuture;

import static java.lang.Thread.sleep;

public class MockQuotationServiceImpl implements QuotationService {
    private static final int COAST = 35000;
    private static final long TIME_SLEEP = 50L;

    @Override
    public CompletableFuture<Integer> calculateQuotation(Vehicle vehicle) {
        try {
            sleep(TIME_SLEEP);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        return CompletableFuture.supplyAsync(() -> COAST);
    }
}
