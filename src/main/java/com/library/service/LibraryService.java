package com.library.service;

import com.library.domain.model.Book;
import com.library.domain.model.Loan;
import com.library.exception.LibraryException;
import com.library.repository.LibraryRepository;

import java.time.LocalDate;
import java.util.*;

public class LibraryService implements LibraryManager {
    private final LibraryRepository libraryRepository;
    private final FileStorageManager fileStorageManager;

    public LibraryService(LibraryRepository libraryRepository, FileStorageManager fileStorageManager) {
        this.libraryRepository = libraryRepository;
        this.fileStorageManager = fileStorageManager;
    }

    @Override
    public Book addBook(Book book) {
        Book bookToSave = libraryRepository.findBookByIsbn(book.isbn())
                .map(existing -> existing.addStock(book.totalCopies()))
                .orElse(book);

        libraryRepository.saveBook(bookToSave);
        return bookToSave;
    }

    @Override
    public List<Book> searchBooks(String keyword, boolean onlyAvailable) {
        String query = (keyword == null) ? "" : keyword.toLowerCase();

        return libraryRepository.findAllBooks().stream()
                .filter(b -> query.isEmpty()
                        || b.title().toLowerCase().contains(query)
                        || b.author().toLowerCase().contains(query)
                        || b.isbn().toLowerCase().contains(query))
                .filter(b -> !onlyAvailable || b.availableCopies() > 0)
                .sorted(Comparator.comparing(Book::title, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    @Override
    public Loan borrowBook(String isbn, String borrowerName, int loanDays) {
        Book book = libraryRepository.findBookByIsbn(isbn)
                .orElseThrow(() -> new LibraryException(LibraryException.ErrorType.NOT_FOUND, "Book with ISBN: %s not found".formatted(isbn)));

        Book updatedBook = book.borrowCopy();
        libraryRepository.saveBook(updatedBook);

        String loanId = "LN-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        LocalDate today = LocalDate.now();
        Loan loan = new Loan(loanId, isbn, borrowerName, today, today.plusDays(loanDays), false);

        libraryRepository.saveLoan(loan);
        return loan;
    }

    @Override
    public Loan returnBook(String loanId) {
        Loan loan = libraryRepository.findLoanById(loanId)
                .orElseThrow(() -> new LibraryException(LibraryException.ErrorType.NOT_FOUND, "Loan with LoanID: %s not found".formatted(loanId)));

        Loan returnedLoan = loan.markReturned();
        libraryRepository.saveLoan(returnedLoan);

        Book book = libraryRepository.findBookByIsbn(loan.isbn())
                .orElseThrow(() -> new LibraryException(LibraryException.ErrorType.NOT_FOUND, "Book with i=ISBN: %s not found".formatted(loan.isbn())));
        libraryRepository.saveBook(book.returnCopy());

        double fine = returnedLoan.calculateFine(LocalDate.now());
        return returnedLoan;
    }

    @Override
    public List<Loan> getOverdueLoans() {
        LocalDate today = LocalDate.now();
        return libraryRepository.findAllLoans().stream()
                .filter(loan -> loan.isOverdue(today))
                .sorted(Comparator.comparing(Loan::dueDate))
                .toList();
    }

    @Override
    public void exportCatalogCsv(String filePath) {
        fileStorageManager.writeBooksToFile(filePath, libraryRepository.findAllBooks());
    }

    @Override
    public int importCatalogCsv(String filePath) {
        List<Book> books = fileStorageManager.readBooksFromFile(filePath);
        books.forEach(this::addBook);
        return books.size();
    }
}
