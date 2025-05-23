package id.ac.ui.cs.advprog.mewingmenu.rating.service;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import id.ac.ui.cs.advprog.mewingmenu.rating.model.Rating;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface RatingService {
    
    CompletableFuture<Rating> addRating(Rating rating);
    
    Optional<Rating> findById(String ratingId);
    
    CompletableFuture<List<Rating>> getAllRatingsByMenu(Menu menu);
    
    void deleteRatingById(String ratingId, String sessionid);
    
    CompletableFuture<Rating> updateRating(String ratingId, Rating updatedRating, String sessionId);
    
    CompletableFuture<List<Rating>> getAllRatingsBySession(String id);

    public Optional<Rating> getRatingByMenuAndSession(Menu menu, String id);
}