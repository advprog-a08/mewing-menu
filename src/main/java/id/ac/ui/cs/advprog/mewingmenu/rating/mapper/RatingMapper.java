package id.ac.ui.cs.advprog.mewingmenu.rating.mapper;

import id.ac.ui.cs.advprog.mewingmenu.rating.dto.RatingDto;
import id.ac.ui.cs.advprog.mewingmenu.rating.model.Rating;

public class RatingMapper {
    public static RatingDto toDTO(Rating rating) {
        RatingDto dto = new RatingDto();
        dto.setId(rating.getId());
        dto.setMenuId(rating.getMenu() != null ? rating.getMenu().getId() : null);
        dto.setSessionId(rating.getSessionId());
        dto.setRating(rating.getRating());
        dto.setReview(rating.getReview());
        dto.setCreatedAt(rating.getCreatedAt());
        dto.setUpdatedAt(rating.getUpdatedAt());
        return dto;
    }
}
