package com.library.domain.model;

public enum MembershipTier {
    STUDENT(3, 0.25),
    STANDARD(5, 0.50),
    PREMIUM(10, 0.10);

    private final int maxActiveLoans;
    private final double dailyLateFee;

    MembershipTier(int maxActiveLoans, double dailyLateFee) {
        this.maxActiveLoans = maxActiveLoans;
        this.dailyLateFee = dailyLateFee;
    }

    public int getMaxActiveLoans() {
        return maxActiveLoans;
    }

    public double getDailyLateFee() {
        return dailyLateFee;
    }
}
