package org.example;

import java.io.Serializable;

public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    private static int nextId = 1;

    private final int bookId;
    private String bookName;
    private String writerName;
    private double price;
    private int quantity;

    public Book(String bookName, String writerName, double price, int quantity) {
        this.bookId = nextId++;
        this.bookName = bookName;
        this.writerName = writerName;
        this.price = price;
        this.quantity = quantity;
    }

    public int getBookId() { return bookId; }
    public String getBookName() { return bookName; }
    public String getWriterName() { return writerName; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void setBookName(String bookName) { this.bookName = bookName; }
    public void setWriterName(String writerName) { this.writerName = writerName; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public static void syncNextId(int maxId) {
        if (maxId >= nextId) nextId = maxId + 1;
    }

    @Override
    public String toString() {
        return "ID: " + bookId +
                " | Nome: " + bookName +
                " | Autor: " + writerName +
                " | Preço: R$ " + price +
                " | Qtd: " + quantity;
    }
}