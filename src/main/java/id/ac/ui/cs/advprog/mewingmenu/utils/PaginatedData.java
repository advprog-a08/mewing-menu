package id.ac.ui.cs.advprog.mewingmenu.utils;

import java.util.List;

public class PaginatedData<T> {
    private List<T> items;
    private int page;
    private int size;
    private long totalItems;
    private int totalPages;

    public PaginatedData(List<T> items, int page, int size, long totalItems) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.totalItems = totalItems;
        this.totalPages = (int) Math.ceil((double) totalItems / size);
    }

    public List<T> getItems() {
        return items;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }
}