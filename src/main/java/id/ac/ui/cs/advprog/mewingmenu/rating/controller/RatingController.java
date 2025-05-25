package id.ac.ui.cs.advprog.mewingmenu.rating.controller;

import id.ac.ui.cs.advprog.mewingmenu.annotation.RequireTableSession;
import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import id.ac.ui.cs.advprog.mewingmenu.rating.dto.RatingDto;
import id.ac.ui.cs.advprog.mewingmenu.rating.model.Rating;
import id.ac.ui.cs.advprog.mewingmenu.rating.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    // Response wrapper class
    public static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;

        public ApiResponse(boolean success, String message, T data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        // Getters and setters
        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }

    @PostMapping
    @RequireTableSession
    public CompletableFuture<ResponseEntity<ApiResponse<RatingDto>>> addRating(
            @RequestBody Rating rating,
            @RequestHeader("X-Session-Id") String sessionId) {
        rating.setSessionId(sessionId);
    
        return ratingService.addRating(rating)
                .thenApply(ratingDto -> ResponseEntity.ok(
                    new ApiResponse<>(true, "Rating added successfully.", ratingDto)))
                .exceptionally(ex -> ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, ex.getCause().getMessage(), null)));
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<ApiResponse<List<RatingDto>>>> getAllRatings() {
        return ratingService.getAll()
                .thenApply(ratings -> ResponseEntity.ok(
                    new ApiResponse<>(true, "Successfully fetched all ratings.", ratings)));
    }
    
    @GetMapping("/{ratingId}")
    public ResponseEntity<ApiResponse<RatingDto>> getRatingById(@PathVariable String ratingId) {
        return ratingService.findById(ratingId)
                .map(ratingDto -> ResponseEntity.ok(
                    new ApiResponse<>(true, "Rating found successfully.", ratingDto)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(false, "Rating not found.", null)));
    }

    @GetMapping("/menu/{menuId}")
    public CompletableFuture<ResponseEntity<ApiResponse<List<RatingDto>>>> getRatingsByMenu(@PathVariable String menuId) {
        Menu menu = new Menu();
        menu.setId(menuId);
        return ratingService.getAllRatingsByMenu(menu)
                .thenApply(ratings -> ResponseEntity.ok(
                    new ApiResponse<>(true, "Successfully fetched ratings for menu.", ratings)));
    }

    @DeleteMapping("/{ratingId}")
    @RequireTableSession
    public ResponseEntity<ApiResponse<Void>> deleteRating(@PathVariable String ratingId, @RequestHeader("X-Session-Id") String sessionId) {
        try {
            ratingService.deleteRatingById(ratingId, sessionId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Rating deleted successfully.", null));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponse<>(false, ex.getMessage(), null));
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ApiResponse<>(false, ex.getMessage(), null));
        }
    }

    @PutMapping("/{ratingId}")
    @RequireTableSession
    public CompletableFuture<ResponseEntity<ApiResponse<RatingDto>>> updateRating(@PathVariable String ratingId,
                                               @RequestBody Rating updatedRating,
                                               @RequestHeader("X-Session-Id") String sessionId) {
        return ratingService.updateRating(ratingId, updatedRating, sessionId)
                .thenApply(ratingDto -> ResponseEntity.ok(
                    new ApiResponse<>(true, "Rating updated successfully.", ratingDto)))
                .exceptionally(ex -> ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, ex.getCause().getMessage(), null)));
    }

    @GetMapping("/my-ratings")
    @RequireTableSession
    public ResponseEntity<ApiResponse<List<RatingDto>>> getAllBySession(
            @RequestHeader("X-Session-Id") String sessionId) {
        List<RatingDto> ratings = ratingService.getAllRatingsBySession(sessionId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Successfully fetched user ratings.", ratings));
    }
    
    @GetMapping("/menu/{menuId}/me")
    @RequireTableSession
    public ResponseEntity<ApiResponse<RatingDto>> getRatingByMenuAndSession(@PathVariable String menuId,
            @RequestHeader("X-Session-Id") String sessionId) {
        Menu menu = new Menu();
        menu.setId(menuId);
        
        try {
            RatingDto rating = ratingService.getRatingByMenuAndSession(menu, sessionId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rating not found"));
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Rating found successfully.", rating));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponse<>(false, "Rating not found for this menu and user.", null));
        }
    }
}