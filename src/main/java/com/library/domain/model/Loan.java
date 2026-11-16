package com.library.domain.model;

public record Loan(
        String loanId,
        String isbn,
        String memberId
        //borrowDate
        //dueDate
        //returnDate?
) {
    public Loan {
        if (loanId == null || loanId.isBlank()) {
            throw new IllegalArgumentException("Loan ID must not be blank");
        }
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN must not be blank");
        }
        if (memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException("Member ID must not be blank");
        }
    }
}
