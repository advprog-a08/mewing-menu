package id.ac.ui.cs.advprog.mewingmenu.rating.model;

import java.time.LocalDateTime;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;

public class Rating {
    private String id;
    private Menu menu;
    private String userId;
    private Integer rating;
    private String review;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
