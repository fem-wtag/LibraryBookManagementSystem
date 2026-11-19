package com.library.domain.port.in;

import com.library.domain.model.Book;
import com.library.domain.model.Loan;

import java.util.List;
import java.util.Map;

public interface LibraryUseCase {
    Book addBook(Book book);
    List<Book> searchBooks(String keyword, boolean onlyAvailable);
    Loan borrowBook(String isbn, String borrowerName, int loanDays);
    Loan returnBook(String loanId);
    List<Loan> getOverdueLoans();
    Map<String, Long> getBookCountByGenre();
}
