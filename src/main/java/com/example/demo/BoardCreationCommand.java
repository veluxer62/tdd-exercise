package com.example.demo;

import java.util.Objects;

public class BoardCreationCommand {
    private final String title;
    private final String content;
    private final String author;

    public BoardCreationCommand(String title, String content, String author) {
        if (title.isBlank() || author.isBlank()) {
            throw new IllegalArgumentException();
        }

        this.title = title;
        this.content = content;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardCreationCommand that = (BoardCreationCommand) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(content, that.content) &&
                Objects.equals(author, that.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, content, author);
    }
}
