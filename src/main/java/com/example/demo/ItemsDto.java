package com.example.demo;

import java.util.List;

public class ItemsDto<T> {
    public final List<T> items;

    public ItemsDto(List<T> items) {
        this.items = items;
    }

    public List<T> getItems() {
        return items;
    }
}
