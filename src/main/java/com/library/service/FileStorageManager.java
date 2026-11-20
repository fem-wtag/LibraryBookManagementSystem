package com.library.service;

import com.library.domain.model.Book;

import java.util.List;

public interface FileStorageManager {
    void writeBooksToFile(String filePath, List<Book> books);
    List<Book> readBooksFromFile(String filePath);
}
