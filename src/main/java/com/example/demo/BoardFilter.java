package com.example.demo;

import java.util.Objects;

public class BoardFilter {
    private String title;

    public BoardFilter(String title) {
        this.title = title;
    }

    public BoardFilter() {
        this.title = null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardFilter that = (BoardFilter) o;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
