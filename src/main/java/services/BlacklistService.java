package services;

import java.util.concurrent.CompletableFuture;

public interface BlacklistService {
    CompletableFuture<Boolean> blacklistRegisterNumber(String registerNumber);
}
