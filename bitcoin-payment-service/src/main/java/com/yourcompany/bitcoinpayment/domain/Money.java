package com.yourcompany.bitcoinpayment.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Fiat amount stored in minor units (e.g. cents) to avoid floating-point drift.
 */
public final class Money {

    private final String currency;
    private final long minorUnits;

    public Money(String currency, long minorUnits) {
        this.currency = Objects.requireNonNull(currency, "currency").toUpperCase();
        this.minorUnits = minorUnits;
    }

    public static Money fromMajorUnits(String currency, BigDecimal major, int scale) {
        BigDecimal scaled = major.setScale(scale, RoundingMode.HALF_UP);
        long minor = scaled.unscaledValue().longValueExact();
        return new Money(currency, minor);
    }

    public String getCurrency() {
        return currency;
    }

    public long getMinorUnits() {
        return minorUnits;
    }

    public BigDecimal toMajorUnits(int scale) {
        return BigDecimal.valueOf(minorUnits, scale);
    }
}
