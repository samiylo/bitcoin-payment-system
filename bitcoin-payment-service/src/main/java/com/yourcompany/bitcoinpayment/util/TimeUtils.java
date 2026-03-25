package com.yourcompany.bitcoinpayment.util;

import java.time.Clock;
import java.time.Instant;

public final class TimeUtils {

    private static final Clock DEFAULT = Clock.systemUTC();

    private TimeUtils() {}

    public static Instant nowUtc() {
        return Instant.now(DEFAULT);
    }
}
