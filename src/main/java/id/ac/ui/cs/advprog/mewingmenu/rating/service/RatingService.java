package id.ac.ui.cs.advprog.mewingmenu.rating.service;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import id.ac.ui.cs.advprog.mewingmenu.rating.dto.RatingDto;
import id.ac.ui.cs.advprog.mewingmenu.rating.model.Rating;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface RatingService {
    
    CompletableFuture<RatingDto> addRating(Rating rating);
    
    Optional<RatingDto> findById(String ratingId);
    
    CompletableFuture<List<RatingDto>> getAllRatingsByMenu(Menu menu);

    CompletableFuture<List<RatingDto>> getAll();
    
    void deleteRatingById(String ratingId, String sessionid);
    
    CompletableFuture<RatingDto> updateRating(String ratingId, Rating updatedRating, String sessionId);
    
    List<RatingDto> getAllRatingsBySession(String id);

    public Optional<RatingDto> getRatingByMenuAndSession(Menu menu, String id);
}