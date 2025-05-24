package id.ac.ui.cs.advprog.mewingmenu.rating.controller;

import id.ac.ui.cs.advprog.mewingmenu.annotation.AuthenticatedTableSession;
import id.ac.ui.cs.advprog.mewingmenu.annotation.RequireTableSession;
import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import id.ac.ui.cs.advprog.mewingmenu.rating.model.Rating;
import id.ac.ui.cs.advprog.mewingmenu.rating.service.RatingService;
import table_session.TableSessionOuterClass.TableSession;

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

    @RequireTableSession
    @PostMapping
    public ResponseEntity<CompletableFuture<Rating>> addRating(
            @RequestBody Rating rating,
            @AuthenticatedTableSession TableSession tableSession) {
        rating.setSessionId(tableSession.getId());
    
        CompletableFuture<Rating> savedRating = ratingService.addRating(rating);
        return ResponseEntity.ok(savedRating);
    }
    

    @GetMapping("/{ratingId}")
    public ResponseEntity<Rating> getRatingById(@PathVariable String ratingId) {
        return ratingService.findById(ratingId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/menu/{menuId}")
    public ResponseEntity<CompletableFuture<List<Rating>>> getRatingsByMenu(@PathVariable String menuId) {
        Menu menu = new Menu();
        menu.setId(menuId);
        CompletableFuture<List<Rating>> ratings = ratingService.getAllRatingsByMenu(menu);
        return ResponseEntity.ok(ratings);
    }

    @DeleteMapping("/{ratingId}")
    @RequireTableSession
    public ResponseEntity<Void> deleteRating(@PathVariable String ratingId, @AuthenticatedTableSession TableSession tableSession) {
        ratingService.deleteRatingById(ratingId, tableSession.toString());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{ratingId}")
    @RequireTableSession
    public ResponseEntity<CompletableFuture<Rating>> updateRating(@PathVariable String ratingId,
                                               @RequestBody Rating updatedRating,
                                               @AuthenticatedTableSession TableSession tableSession) {
        CompletableFuture<Rating> result = ratingService.updateRating(ratingId, updatedRating, tableSession.getId());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/my-ratings")
    @RequireTableSession
    public ResponseEntity<CompletableFuture<List<Rating>>> getAllBySession(@AuthenticatedTableSession TableSession tableSession) {
        CompletableFuture<List<Rating>> result = ratingService.getAllRatingsBySession(tableSession.getId());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/menu/{menuId}/me")
    public ResponseEntity<Rating> getRatingByMenuAndSession(@PathVariable String menuId,
            @AuthenticatedTableSession TableSession tableSession) {
        Menu menu = new Menu();
        menu.setId(menuId);
        Rating rating = ratingService.getRatingByMenuAndSession(menu, tableSession.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RATING NOT FOUND"));
        
        return ResponseEntity.ok(rating);
    }
}