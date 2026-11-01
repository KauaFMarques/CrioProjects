package org.example;

public class IssueBook {
    private final int bookId;
    private final int userId;

    public IssueBook(int bookId, int userId) {
        this.bookId = bookId;
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Empréstimo: -> " + "bookId=" + bookId + ", userId=" + userId ;
    }
}
