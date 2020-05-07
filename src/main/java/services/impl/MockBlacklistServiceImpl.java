package services.impl;

import services.BlacklistService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Thread.sleep;

public class MockBlacklistServiceImpl implements BlacklistService {
    private static final String REGISTER_NUMBER = "AA123AA";
    private static final long TIME_SLEEP = 50L;

    @Override
    public CompletableFuture<Boolean> blacklistRegisterNumber(String registerNumber) {
        AtomicReference<Boolean> result = new AtomicReference<>(false);
        return CompletableFuture.completedFuture(registerNumber).thenApply(s -> {
            try {
                sleep(TIME_SLEEP);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            result.set(s.equals(REGISTER_NUMBER));
            return result.get();
        });
    }
}
