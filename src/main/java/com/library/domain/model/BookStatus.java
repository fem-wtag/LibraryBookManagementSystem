package com.library.domain.model;

public sealed interface BookStatus
        permits BookStatus.Available, BookStatus.LowStock, BookStatus.OutOfStock {

    record Available(int copies) implements BookStatus {}
    record LowStock(int copies) implements BookStatus {}
    record OutOfStock() implements BookStatus {}

    static BookStatus fromAvailableCopies(int copies) {
        return switch (Integer.valueOf(copies)) {
            case Integer c when c <= 0 -> new OutOfStock();
            case Integer c when c <= 3 -> new LowStock(c);
            case Integer c -> new Available(c);
        };
    }
}
