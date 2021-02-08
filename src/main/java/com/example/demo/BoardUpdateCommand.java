package com.example.demo;

import java.util.Objects;

public class BoardUpdateCommand {
    private final String title;
    private final String content;

    public BoardUpdateCommand(String title, String content) {
        if (title.isBlank()) {
            throw new IllegalArgumentException();
        }

        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardUpdateCommand that = (BoardUpdateCommand) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, content);
    }
}
