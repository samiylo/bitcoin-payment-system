package com.yourcompany.bitcoinpayment.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class BtcConverter {

    private static final BigDecimal SATOSHI_PER_BTC = new BigDecimal("100000000");

    private BtcConverter() {}

    /**
     * @param fiatMinorUnits e.g. cents
     * @param fiatScale      decimal places for fiat (e.g. 2)
     * @param btcPrice       major fiat units per 1 BTC
     */
    public static long fiatMinorToSatoshi(long fiatMinorUnits, int fiatScale, BigDecimal btcPrice) {
        if (btcPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("btcPrice must be positive");
        }
        BigDecimal fiatMajor = BigDecimal.valueOf(fiatMinorUnits, fiatScale);
        BigDecimal btc = fiatMajor.divide(btcPrice, 12, RoundingMode.HALF_UP);
        BigDecimal sat = btc.multiply(SATOSHI_PER_BTC).setScale(0, RoundingMode.HALF_UP);
        long s = sat.longValueExact();
        if (s < 1) {
            return 1;
        }
        return s;
    }
}
