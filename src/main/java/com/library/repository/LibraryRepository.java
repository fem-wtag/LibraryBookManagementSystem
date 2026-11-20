package com.library.repository;

import com.library.domain.model.Book;
import com.library.domain.model.Loan;

import java.util.List;
import java.util.Optional;

public interface LibraryRepository {
    void saveBook(Book book);
    Optional<Book> findBookByIsbn(String isbn);
    List<Book> findAllBooks();

    void saveLoan(Loan loan);
    Optional<Loan> findLoanById(String loanId);
    List<Loan> findAllLoans();
}
