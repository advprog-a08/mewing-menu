package id.ac.ui.cs.advprog.mewingmenu.rating.service;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import id.ac.ui.cs.advprog.mewingmenu.rating.model.Rating;
import id.ac.ui.cs.advprog.mewingmenu.rating.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    @Async
    public CompletableFuture<Rating> addRating(Rating rating) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Rating> existingRating = ratingRepository.findBySessionIdAndMenu(rating.getSessionId(), rating.getMenu());
            if (existingRating.isPresent()) {
                throw new IllegalStateException("User has already reviewed this menu");
            }
            return ratingRepository.save(rating);
        });
    }

    @Override
    public Optional<Rating> findById(String ratingId) {
        return ratingRepository.findById(ratingId);
    }

    @Override
    @Async
    public CompletableFuture<List<Rating>> getAllRatingsByMenu(Menu menu) {
        return CompletableFuture.supplyAsync(() -> ratingRepository.findAllByMenu(menu));
    }

    @Override
    public void deleteRatingById(String ratingId, String sessionid) {
        Optional<Rating> ratingOpt = ratingRepository.findById(ratingId);
        if (ratingOpt.isEmpty()) {
            throw new IllegalStateException("Rating not found");
        }

        Rating rating = ratingOpt.get();
        if (!rating.getSessionId().equals(sessionid)) {
            throw new SecurityException("User not authorized to delete this rating");
        }

        ratingRepository.deleteById(ratingId);
    }

    @Override
    @Async
    public CompletableFuture<Rating> updateRating(String ratingId, Rating updatedRating, String sessionId) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Rating> ratingOpt = ratingRepository.findById(ratingId);
            if (ratingOpt.isEmpty()) {
                throw new IllegalStateException("Rating not found");
            }

            Rating existingRating = ratingOpt.get();

            if (!existingRating.getSessionId().equals(sessionId)) {
                throw new IllegalStateException("You are not allowed to update this rating");
            }

            existingRating.setRating(updatedRating.getRating());
            existingRating.setReview(updatedRating.getReview());

            return ratingRepository.save(existingRating);
        });
    }

    @Override
    @Async
    public CompletableFuture<List<Rating>> getAllRatingsBySession(String id) {
        return CompletableFuture.supplyAsync(() -> ratingRepository.findAllBySessionId(id));
    }

    @Override
    public Optional<Rating> getRatingByMenuAndSession(Menu menu, String id) {
        Optional<Rating> ratingOpt = ratingRepository.findById(id);
        if (ratingOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rating not found");
        }
        return ratingOpt;
    }
}