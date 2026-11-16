package com.library.domain.model;

public record Book(
        String isbn,
        String title,
        String author,
        String genre,
        int publicationYear,
        int totalCopies,
        int availableCopies
) {
   public Book {
       if (isbn == null || isbn.isBlank()) {
           throw new IllegalArgumentException("ISBN must not be blank");
       }
       if (title == null || title.isBlank()) {
           throw new IllegalArgumentException("Title must not be blank");
       }
       if (author == null || author.isBlank()) {
           throw new IllegalArgumentException("Title must not be blank");
       }
       if (genre == null || genre.isBlank()) {
           genre = "General";
       }
       if (totalCopies < 1) {
           throw new IllegalArgumentException("Total copies must be atleast 1");
       }
       if(availableCopies < 0 || availableCopies > totalCopies) {
           throw new IllegalArgumentException(
                   "Available copies (%d) must be between 0 and total copies (%d)"
                           .formatted(availableCopies, totalCopies)
           );
       }
   }

   public BookStatus status() {
       return BookStatus.fromAvailableCopies(availableCopies);
   }

   public Book borrowOne() {
       if (availableCopies <= 0) {
           //throw custom book not available exception
       }
       return new Book(isbn, title, author, genre, publicationYear, totalCopies, availableCopies - 1);
   }

   public Book returnOne() {
        if (availableCopies >= totalCopies) {
            throw new IllegalArgumentException("Cannot exceed total copies for ISBN: " + isbn);
        }
        return new Book(isbn, title, author, genre, publicationYear, totalCopies, availableCopies + 1);
   }

   public Book addCopies(int additionalCopies) {
       if (additionalCopies <= 0) {
           throw new IllegalArgumentException("Additional copies must be positive");
       }
       return new Book(isbn, title, author, genre, publicationYear,
               totalCopies + additionalCopies,
               availableCopies + additionalCopies);
   }
}
