package com.library.service;

import com.library.domain.model.Book;

import java.util.List;

public class NioFileStorage implements FileStorageManager {
    @Override
    public void writeBooksToFile(String filePath, List<Book> books) {
        //impl
    }

    @Override
    public List<Book> readBooksFromFile(String filePath) {
        return List.of();
    }
}
