package id.ac.ui.cs.advprog.mewingmenu.rating.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingDto {
    private String id;
    private String menuId;
    private String sessionId;
    private Integer rating;
    private String review;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

