package org.example;

import java.io.Serializable;

public class IssueBook implements Serializable {

    private static final long serialVersionUID = 1L;

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
