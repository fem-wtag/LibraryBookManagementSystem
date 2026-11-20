package com.library.repository;

import com.library.domain.model.Book;
import com.library.domain.model.Loan;
import com.library.exception.LibraryException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcLibraryRepository implements LibraryRepository {

    private final String jdbcUrl;
    private final String user;
    private final String password;

    public JdbcLibraryRepository(String jdbcUrl, String user, String password) {
        this.jdbcUrl = jdbcUrl;
        this.user = user;
        this.password = password;
        initTables();
    }
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, user, password);
    }

    private void initTables() {
        String createBooks = """
                CREATE TABLE IF NOT EXISTS books (
                    isbn VARCHAR(30) PRIMARY KEY,
                    title VARCHAR(200) NOT NULL,
                    author VARCHAR(200) NOT NULL,
                    genre VARCHAR(100) NOT NULL,
                    total_copies INT NOT NULL,
                    available_copies INT NOT NULL
                )
                """;

        String createLoans = """
                CREATE TABLE IF NOT EXISTS loans (
                    loan_id VARCHAR(30) PRIMARY KEY,
                    isbn VARCHAR(30) NOT NULL,
                    borrower_name VARCHAR(200) NOT NULL,
                    borrow_date DATE NOT NULL,
                    due_date DATE NOT NULL,
                    returned BOOLEAN NOT NULL,
                    FOREIGN KEY (isbn) REFERENCES book(isbn)
                )
                """;

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(createBooks);
            stmt.execute(createLoans);
        } catch (SQLException e) {
            throw new LibraryException(LibraryException.ErrorType.STORAGE_ERROR, "Failed to initialize database tables", e);
        }
    }

    @Override
    public void saveBook(Book book) {
        String sql = """
                MERGE INTO books (isbn, title, author, genre, total_copies, available_copies)
                KEY (isbn) VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.isbn());
            ps.setString(2, book.title());
            ps.setString(3, book.author());
            ps.setString(4, book.genre());
            ps.setInt(5, book.totalCopies());
            ps.setInt(6, book.availableCopies());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new LibraryException(LibraryException.ErrorType.STORAGE_ERROR, "Failed to save book: %s".formatted(book.isbn()), e);
        }
    }

    @Override
    public Optional<Book> findBookByIsbn(String isbn) {
        String sql = "SELECT * FROM books WHERE isbn = ?";

        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                return Optional.ofNullable(mapBook(rs));
            }
        } catch (SQLException e) {
            throw new LibraryException(LibraryException.ErrorType.STORAGE_ERROR, "Failed to find book: %s".formatted(isbn), e);
        }
    }

    @Override
    public List<Book> findAllBooks() {
        String sql = "SELECT * FROM books";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Book> books = new ArrayList<>();
            while (rs.next()) {
                books.add(mapBook(rs));
            }
            return books;
        } catch (SQLException e) {
            throw new LibraryException(LibraryException.ErrorType.STORAGE_ERROR, "Failed to load books", e);
        }
    }

    @Override
    public void saveLoan(Loan loan) {
        String sql = """
                MERGE INTO loans (loanId, isbn, borrower_name, borrow_date, due_date, returned)
                KEY (loanId) VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, loan.loanId());
            ps.setString(2, loan.isbn());
            ps.setString(3, loan.borrowerName());
            ps.setDate(4, Date.valueOf(loan.borrowDate()));
            ps.setDate(5, Date.valueOf(loan.dueDate()));
            ps.setBoolean(6, loan.returned());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new LibraryException(LibraryException.ErrorType.STORAGE_ERROR, "Failed to save loan: %s".formatted(loan.loanId()), e);
        }
    }

    @Override
    public Optional<Loan> findLoanById(String loanId) {
        String sql = "SELECT * FROM loans WHERE loanId = ?";

        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, loanId);
            try (ResultSet rs = ps.executeQuery()) {
                return Optional.ofNullable(mapLoan(rs));
            }
        } catch (SQLException e) {
            throw new LibraryException(LibraryException.ErrorType.STORAGE_ERROR, "Failed to find loan: %s".formatted(loanId), e);
        }
    }

    @Override
    public List<Loan> findAllLoans() {
        String sql = "SELECT * FROM loans";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Loan> loans = new ArrayList<>();
            while (rs.next()) {
                loans.add(mapLoan(rs));
            }
            return loans;
        } catch (SQLException e) {
            throw new LibraryException(LibraryException.ErrorType.STORAGE_ERROR, "Failed to load loans", e);
        }
    }

    private Book mapBook(ResultSet rs) throws SQLException {
        return new Book(
                rs.getString("isbn"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("genre"),
                rs.getInt("total_copies"),
                rs.getInt("available_copies")
        );
    }

    private Loan mapLoan(ResultSet rs) throws SQLException {
        return new Loan(
                rs.getString("loan_id"),
                rs.getString("isbn"),
                rs.getString("borrower_name"),
                rs.getDate("borrow_date").toLocalDate(),
                rs.getDate("due_date").toLocalDate(),
                rs.getBoolean("returned")
        );
    }
}
