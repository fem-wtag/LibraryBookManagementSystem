package com.library.domain.model;

import java.time.LocalDateTime;

import static com.library.domain.model.LibraryEvent.*;

public sealed interface LibraryEvent
        permits BookAdded, BookBorrowed, BookReturned {

    record BookAdded(LocalDateTime time, String isbn, String title, int copies) implements LibraryEvent {}
    record BookBorrowed(LocalDateTime time, String loanId, String isbn, String borrower) implements LibraryEvent {}
    record BookReturned(LocalDateTime time, String loanId, double fine) implements LibraryEvent {}

    default String toLogEntry() {
        return switch (this) {
            case BookAdded(var t, var isbn, var title, var copies) ->
                    "[%s] ADDED    | ISBN: %s | %s (+%d copies)".formatted(t, isbn, title, copies);
            case BookBorrowed(var t, var id, var isbn, var user) ->
                    "[%s] BORROWED | Loan: %s | ISBN: %s | By: %s".formatted(t, id, isbn, user);
            case BookReturned(var t, var id, var fine) ->
                    "[%s] RETURNED | Loan: %s | Fine: $%.2f".formatted(t, id, fine);
        };
    }
}
