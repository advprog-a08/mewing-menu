package id.ac.ui.cs.advprog.mewingmenu.rating.service;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import id.ac.ui.cs.advprog.mewingmenu.rating.model.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingService {
    Rating addRating(Rating rating);
    Optional<Rating> findById(String ratingId);
    List<Rating> getAllRatingsByMenu(Menu menu);
    void deleteRatingById(String ratingId, String userId);
    Rating updateRating(String ratingId, Rating updatedRating, String userId);
}
