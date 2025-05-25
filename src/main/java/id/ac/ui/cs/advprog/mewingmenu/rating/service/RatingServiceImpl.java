package id.ac.ui.cs.advprog.mewingmenu.rating.service;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import id.ac.ui.cs.advprog.mewingmenu.rating.dto.RatingDto;
import id.ac.ui.cs.advprog.mewingmenu.rating.mapper.RatingMapper;
import id.ac.ui.cs.advprog.mewingmenu.rating.model.Rating;
import id.ac.ui.cs.advprog.mewingmenu.rating.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public CompletableFuture<RatingDto> addRating(Rating rating) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Rating> existingRating = ratingRepository.findBySessionIdAndMenu(rating.getSessionId(), rating.getMenu());
            if (existingRating.isPresent()) {
                throw new IllegalStateException("User has already reviewed this menu");
            }
            Rating savedRating = ratingRepository.save(rating);
            return RatingMapper.toDTO(savedRating);
        });
    }

    @Override
    public Optional<RatingDto> findById(String ratingId) {
        Optional<Rating> rating = ratingRepository.findById(ratingId);
        return rating.map(RatingMapper::toDTO);
    }
    
    @Override
    @Async
    public CompletableFuture<List<RatingDto>> getAllRatingsByMenu(Menu menu) {
        return CompletableFuture.supplyAsync(() -> {
            List<Rating> ratings = ratingRepository.findAllByMenu(menu);
            return ratings.stream()
                          .map(RatingMapper::toDTO)
                          .toList();
        });
    }

    @Override
    @Async
    public CompletableFuture<List<RatingDto>> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            List<Rating> ratings = ratingRepository.findAll();
            return ratings.stream()
                          .map(RatingMapper::toDTO)
                          .toList();
        });
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
    public CompletableFuture<RatingDto> updateRating(String ratingId, Rating updatedRating, String sessionId) {
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

            Rating savedRating = ratingRepository.save(existingRating);
            return RatingMapper.toDTO(savedRating);
        });
    }

    @Override
    public List<RatingDto> getAllRatingsBySession(String id) {
        List<Rating> ratings = ratingRepository.findAllBySessionId(id);
        return ratings.stream()
                      .map(RatingMapper::toDTO)
                      .toList();
    }

    @Override
    public Optional<RatingDto> getRatingByMenuAndSession(Menu menu, String id) {
        Optional<Rating> ratingOpt = ratingRepository.findBySessionIdAndMenu(id, menu);
        return ratingOpt.map(RatingMapper::toDTO);
    }
}