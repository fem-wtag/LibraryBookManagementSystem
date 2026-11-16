package com.library.domain.model;

public record Member(
        String memberId,
        String fullName,
        String email,
        MembershipTier tier
) {
    public Member {
        if (memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException("Member ID must not be blank");
        }
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name must not be blank");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Valid email is required");
        }
        if (tier == null) {
            tier = MembershipTier.STANDARD;
        }
    }
}
