package com.cournarie.codinggame.service;

/**
 * Configuration for fraud detection.
 */
public  final class FraudConfiguration {
    /** Maximum amount of money in euro cents. Default is 10.000 euros*/
    private static long maxAmount = 10000 * 100;
    /** Time window in milliseconds. Default is 20 minutes. */
    private static long elapseTime = 20 * 60 *1000;

    /**
     * Hidden constructor.
     */
    private FraudConfiguration() {
    }

    public static long getMaxAmount() {
        return maxAmount;
    }

    public static void setMaxAmount(long maxAmount) {
        FraudConfiguration.maxAmount = maxAmount;
    }

    public static long getElapseTime() {
        return elapseTime;
    }

    public static void setElapseTime(long elapseTime) {
        FraudConfiguration.elapseTime = elapseTime;
    }
}
