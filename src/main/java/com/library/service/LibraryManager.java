package com.library.service;

import com.library.domain.model.Book;
import com.library.domain.model.Loan;

import java.util.List;

public interface LibraryManager {
    Book addBook(Book book);
    List<Book> searchBooks(String keyword, boolean onlyAvailable);
    Loan borrowBook(String isbn, String borrowerName, int loanDays);
    Loan returnBook(String loanId);
    List<Loan> getOverdueLoans();
    void exportCatalogCsv(String filePath);
    int importCatalogCsv(String filePath);
}
