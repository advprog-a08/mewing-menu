package id.ac.ui.cs.advprog.mewingmenu.rating.controller;

import id.ac.ui.cs.advprog.mewingmenu.annotation.AuthenticatedTableSession;
import id.ac.ui.cs.advprog.mewingmenu.annotation.RequireTableSession;
import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import id.ac.ui.cs.advprog.mewingmenu.model.TableSession;
import id.ac.ui.cs.advprog.mewingmenu.rating.model.Rating;
import id.ac.ui.cs.advprog.mewingmenu.rating.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    public ResponseEntity<Rating> addRating(@RequestBody Rating rating) {
        Rating savedRating = ratingService.addRating(rating);
        return ResponseEntity.ok(savedRating);
    }

    @GetMapping("/{ratingId}")
    public ResponseEntity<Rating> getRatingById(@PathVariable String ratingId) {
        return ratingService.findById(ratingId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/menu")
    public ResponseEntity<List<Rating>> getRatingsByMenu(@RequestParam String menuId) {
        Menu menu = new Menu();
        menu.setId(menuId);
        List<Rating> ratings = ratingService.getAllRatingsByMenu(menu);
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
    public ResponseEntity<Rating> updateRating(@PathVariable String ratingId,
                                               @RequestBody Rating updatedRating,
                                               @AuthenticatedTableSession TableSession tableSession) {
        Rating result = ratingService.updateRating(ratingId, updatedRating, tableSession.toString());
        return ResponseEntity.ok(result);
    }
}