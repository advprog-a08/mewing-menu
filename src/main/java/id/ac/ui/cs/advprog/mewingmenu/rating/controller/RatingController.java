package id.ac.ui.cs.advprog.mewingmenu.rating.controller;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
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
    public ResponseEntity<Void> deleteRating(@PathVariable String ratingId, @RequestParam String userId) {
        ratingService.deleteRatingById(ratingId, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{ratingId}")
    public ResponseEntity<Rating> updateRating(@PathVariable String ratingId,
                                               @RequestBody Rating updatedRating,
                                               @RequestParam String userId) {
        Rating result = ratingService.updateRating(ratingId, updatedRating, userId);
        return ResponseEntity.ok(result);
    }
}
