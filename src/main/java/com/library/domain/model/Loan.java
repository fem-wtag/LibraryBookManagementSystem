package com.library.domain.model;

import com.library.exception.LibraryException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record Loan(
        String loanId,
        String isbn,
        String borrowerName,
        LocalDate borrowDate,
        LocalDate dueDate,
        boolean returned
) {
    private static final double DAILY_LATE_RATE = 0.50;

    public Loan {
        if (loanId == null || loanId.isBlank()) {
            throw new IllegalArgumentException("Loan ID must not be blank");
        }
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN must not be blank");
        }
        if (borrowerName == null || borrowerName.isBlank()) {
            throw new IllegalArgumentException("Borrower name must not be blank");
        }
    }

    public boolean isOverdue(LocalDate today) {
        return !returned && today.isAfter(dueDate);
    }

    public double calculateFine(LocalDate today) {
        long daysLate = ChronoUnit.DAYS.between(dueDate, today);
        return daysLate > 0 ? daysLate * DAILY_LATE_RATE : 0.0;
    }

    public Loan markReturned() throws LibraryException {
        if (returned) {
            throw new LibraryException(LibraryException.ErrorType.INVALID_INPUT, "Loan " + loanId + " is already returned.");
        }
        return new Loan(loanId, isbn, borrowerName, borrowDate, dueDate, true);
    }
}
