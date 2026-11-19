package com.library.domain.port.out;

import com.library.domain.model.Book;
import com.library.domain.model.LibraryEvent;

import java.util.List;

public interface FileStoragePort {
    void writeBooksToFile(String filePath, List<Book> books);
    List<Book> readBooksFromFile(String filePath);
}
