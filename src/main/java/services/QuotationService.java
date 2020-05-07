package services;

import models.Vehicle;

import java.util.concurrent.CompletableFuture;

public interface QuotationService {
    CompletableFuture<Integer> calculateQuotation(Vehicle vehicle);
}
