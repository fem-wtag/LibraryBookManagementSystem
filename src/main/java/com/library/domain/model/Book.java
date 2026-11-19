package com.library.domain.model;

import com.library.domain.exception.LibraryException;

public record Book(
        String isbn,
        String title,
        String author,
        String genre,
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
           throw new IllegalArgumentException("Author must not be blank");
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

   public Book borrowCopy() throws LibraryException {
       if (availableCopies == 0) {
           throw new LibraryException(LibraryException.ErrorType.UNAVAILABLE, "No available copies for: " + title);
       }
       return new Book(isbn, title, author, genre, totalCopies, availableCopies - 1);
   }

   public Book returnCopy() throws LibraryException {
       if (availableCopies == totalCopies) {
           throw new LibraryException(LibraryException.ErrorType.INVALID_INPUT, "All copies are already in the library.");
       }
       return new Book(isbn, title, author, genre, totalCopies, availableCopies + 1);
   }

   public Book addStock(int extraCopies) throws IllegalArgumentException {
       if (extraCopies < 1) throw new IllegalArgumentException("Extra copies must be positive");

       return new Book(isbn, title, author, genre, totalCopies + extraCopies, availableCopies + extraCopies);
   }

   public String availableLabel() {
       return switch (Integer.valueOf(availableCopies)) {
           case Integer n when n == 0 -> "OUT OF STOCK";
           case Integer n when n <= 2 -> "LOW STOCK (" + n + " left)";
           case Integer n -> "AVAILABLE (" + n + "/" + totalCopies + ")";
       };
   }
}
