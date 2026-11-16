package com.library.domain.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record Loan(
        String loanId,
        String isbn,
        String memberId,
        LocalDate borrowDate,
        LocalDate dueDate,
        LocalDate returnDate
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
        if (borrowDate == null || dueDate == null || dueDate.isBefore(borrowDate)) {
            throw new IllegalArgumentException("Invalid borrow or due date");
        }
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    public boolean isOverdue(LocalDate referenceDate) {
        LocalDate effectiveEnd = isReturned() ? returnDate : referenceDate;
        return effectiveEnd.isAfter(dueDate);
    }

    public double calculateFine(LocalDate referenceDate, double dailyRate) {
        LocalDate effectiveEnd = isReturned() ? returnDate : referenceDate;
        long overdueDays = ChronoUnit.DAYS.between(dueDate, effectiveEnd);
        return overdueDays > 0 ? overdueDays * dailyRate : 0.0;
    }

    public Loan markReturned(LocalDate actualReturnDate) {
        if (isReturned()) {
            //Throw custom loan exception
        }
        return new Loan(loanId, isbn, memberId, borrowDate, dueDate, actualReturnDate);
    }
}
