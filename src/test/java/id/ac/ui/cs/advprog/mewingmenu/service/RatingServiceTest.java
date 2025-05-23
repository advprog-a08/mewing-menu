package id.ac.ui.cs.advprog.mewingmenu.service;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import id.ac.ui.cs.advprog.mewingmenu.menu.repository.MenuRepository;
import id.ac.ui.cs.advprog.mewingmenu.rating.model.Rating;
import id.ac.ui.cs.advprog.mewingmenu.rating.repository.RatingRepository;
import id.ac.ui.cs.advprog.mewingmenu.rating.service.RatingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private RatingServiceImpl ratingService;

    private Rating rating;
    private Rating rating2;
    private Rating rating3;
    private String sessionId1;
    private String sessionId2;
    private Menu menu1;
    private Menu menu2;

    private static Menu createValidMenu(String name) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setDescription("A super tasty burger with special sauce.");
        menu.setImageUrl("http://example.com/burger.jpg");
        menu.setPrice(BigDecimal.valueOf(45000));
        return menu;
    }

    @BeforeEach
    void setUp() {
        menu1 = createValidMenu("Bakso");
        menu2 = createValidMenu("Mie Ayang");

        sessionId1 = UUID.randomUUID().toString();
        sessionId2 = UUID.randomUUID().toString();

        rating = new Rating();
        rating.setRating(3);
        rating.setReview("Mid");
        rating.setSessionId(sessionId1);
        rating.setMenu(menu1);

        rating2 = new Rating();
        rating2.setRating(5);
        rating2.setReview("Top tier");
        rating2.setSessionId(sessionId2);
        rating2.setMenu(menu1);

        rating3 = new Rating();
        rating3.setRating(1);
        rating3.setReview("Zonk");
        rating3.setSessionId(sessionId2);
        rating3.setMenu(menu2);
    }

    @Test
    void testUserCanSuccessfullyAddRating() throws ExecutionException, InterruptedException {
        when(ratingRepository.findBySessionIdAndMenu(sessionId1, menu1))
                .thenReturn(Optional.empty());

        when(ratingRepository.save(rating)).thenReturn(rating);

        CompletableFuture<Rating> resultFuture = ratingService.addRating(rating);
        Rating result = resultFuture.get(); 

        assertNotNull(result);
        assertEquals(rating, result);
        assertEquals(3, result.getRating());
        assertEquals("Mid", result.getReview());
        assertEquals(sessionId1, result.getSessionId());
        assertEquals(menu1, result.getMenu());

        verify(ratingRepository, times(1)).findBySessionIdAndMenu(sessionId1, menu1);
        verify(ratingRepository, times(1)).save(rating);
    }

    @Test
    void testUserCannotAddDuplicateRatingToSameMenu() {
        when(ratingRepository.findBySessionIdAndMenu(sessionId1, menu1))
                .thenReturn(Optional.of(rating));

        Rating duplicateRating = new Rating();
        duplicateRating.setRating(4);
        duplicateRating.setReview("Pretty good");
        duplicateRating.setSessionId(sessionId1);
        duplicateRating.setMenu(menu1);

        CompletableFuture<Rating> resultFuture = ratingService.addRating(duplicateRating);

        ExecutionException exception = assertThrows(ExecutionException.class, () -> {
            resultFuture.get(); 
        });

        assertTrue(exception.getCause() instanceof IllegalStateException);
        assertEquals("User has already reviewed this menu", exception.getCause().getMessage());
    }

    @Test
    void testFindRatingByIdSuccess() {
        String ratingId = UUID.randomUUID().toString();
        rating.setId(ratingId);

        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(rating));

        Optional<Rating> result = ratingService.findById(ratingId);

        assertTrue(result.isPresent());
        assertEquals(ratingId, result.get().getId());
        assertEquals(sessionId1, result.get().getSessionId());
        assertEquals(menu1, result.get().getMenu());
        verify(ratingRepository, times(1)).findById(ratingId);
    }

    @Test
    void testFindRatingByIdNotFound() {
        String ratingId = UUID.randomUUID().toString();

        when(ratingRepository.findById(ratingId)).thenReturn(Optional.empty());

        Optional<Rating> result = ratingService.findById(ratingId);

        assertTrue(result.isEmpty());
        verify(ratingRepository, times(1)).findById(ratingId);
    }

    @Test
    void testGetAllRatingsByMenu() throws ExecutionException, InterruptedException {
        when(ratingRepository.findAllByMenu(menu1)).thenReturn(List.of(rating, rating2));

        CompletableFuture<List<Rating>> resultFuture = ratingService.getAllRatingsByMenu(menu1);
        List<Rating> result = resultFuture.get();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(rating));
        assertTrue(result.contains(rating2));
        verify(ratingRepository, times(1)).findAllByMenu(menu1);
    }

    @Test
    void testDeleteRatingByIdWhenUserIsOwner() {
        String ratingId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        Rating existingRating = new Rating();
        existingRating.setId(ratingId);
        existingRating.setSessionId(userId);

        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(existingRating));

        assertDoesNotThrow(() -> ratingService.deleteRatingById(ratingId, userId));
        verify(ratingRepository, times(1)).deleteById(ratingId);
    }

    @Test
    void testDeleteRatingByIdWhenUserIsNotOwner() {
        String ratingId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        String differentUserId = UUID.randomUUID().toString();

        Rating existingRating = new Rating();
        existingRating.setId(ratingId);
        existingRating.setSessionId(differentUserId);

        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(existingRating));

        Exception exception = assertThrows(SecurityException.class, () ->
                ratingService.deleteRatingById(ratingId, userId)
        );

        assertEquals("User not authorized to delete this rating", exception.getMessage());
        verify(ratingRepository, never()).deleteById(ratingId);
    }

    @Test
    void testUpdateRatingByOwnerSuccess() throws ExecutionException, InterruptedException {
        String ratingId = UUID.randomUUID().toString();
        String userId = sessionId1;

        Rating existingRating = new Rating();
        existingRating.setId(ratingId);
        existingRating.setSessionId(userId);
        existingRating.setMenu(menu1);
        existingRating.setRating(3);
        existingRating.setReview("Mid");

        Rating updatedData = new Rating();
        updatedData.setRating(5);
        updatedData.setReview("Updated review");

        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(existingRating));
        when(ratingRepository.save(existingRating)).thenReturn(existingRating);

        CompletableFuture<Rating> resultFuture = ratingService.updateRating(ratingId, updatedData, userId);
        Rating result = resultFuture.get();

        assertNotNull(result);
        assertEquals(5, result.getRating());
        assertEquals("Updated review", result.getReview());
        verify(ratingRepository, times(1)).save(existingRating);
    }

    @Test
    void testUpdateRatingByNonOwnerThrowsException() {
        String ratingId = UUID.randomUUID().toString();
        String actualOwnerId = sessionId1;
        String attackerUserId = sessionId2;

        Rating existingRating = new Rating();
        existingRating.setId(ratingId);
        existingRating.setSessionId(actualOwnerId);
        existingRating.setMenu(menu1);
        existingRating.setRating(3);
        existingRating.setReview("Mid");

        Rating updatedData = new Rating();
        updatedData.setRating(1);
        updatedData.setReview("test gaming");

        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(existingRating));

        CompletableFuture<Rating> resultFuture = ratingService.updateRating(ratingId, updatedData, attackerUserId);

        ExecutionException ex = assertThrows(ExecutionException.class, () ->
                resultFuture.get() 
        );

        assertTrue(ex.getCause() instanceof IllegalStateException);
        assertEquals("You are not allowed to update this rating", ex.getCause().getMessage());
        verify(ratingRepository, never()).save(any());
    }

    @Test
    void testAddRatingAsyncBehavior() {
        when(ratingRepository.findBySessionIdAndMenu(sessionId1, menu1))
                .thenReturn(Optional.empty());
        when(ratingRepository.save(rating)).thenReturn(rating);

        CompletableFuture<Rating> resultFuture = ratingService.addRating(rating);

        assertNotNull(resultFuture);

        assertDoesNotThrow(() -> {
            Rating result = resultFuture.get();
            assertEquals(rating, result);
        });
    }

    @Test
    void testGetAllRatingsByMenuAsyncBehavior() {
        when(ratingRepository.findAllByMenu(menu1)).thenReturn(List.of(rating, rating2));

        CompletableFuture<List<Rating>> resultFuture = ratingService.getAllRatingsByMenu(menu1);

        assertNotNull(resultFuture);

        assertDoesNotThrow(() -> {
            List<Rating> result = resultFuture.get();
            assertEquals(2, result.size());
        });
    }

    @Test
    void testUpdateRatingAsyncBehavior() {
        String ratingId = UUID.randomUUID().toString();
        String userId = sessionId1;

        Rating existingRating = new Rating();
        existingRating.setId(ratingId);
        existingRating.setSessionId(userId);
        existingRating.setMenu(menu1);
        existingRating.setRating(3);
        existingRating.setReview("Mid");

        Rating updatedData = new Rating();
        updatedData.setRating(5);
        updatedData.setReview("Updated review");

        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(existingRating));
        when(ratingRepository.save(existingRating)).thenReturn(existingRating);

        CompletableFuture<Rating> resultFuture = ratingService.updateRating(ratingId, updatedData, userId);

        assertNotNull(resultFuture);
        assertDoesNotThrow(() -> {
            Rating result = resultFuture.get();
            assertEquals(5, result.getRating());
        });
    }

    @Test
    void testGetAllRatingsBySession() {
        Rating rating = new Rating();
        rating.setRating(5);
        rating.setReview("Enak bangeet");
        rating.setSessionId(sessionId1);
        rating.setMenu(menu1);

        Rating rating2 = new Rating();
        rating2.setRating(3);
        rating2.setReview("Menurut aku sih mid");
        rating2.setSessionId(sessionId1);
        rating2.setMenu(menu2);

        when(ratingRepository.findAllBySessionId(sessionId1))
            .thenReturn(List.of(rating,rating2));
        List<Rating> ratings = ratingService.getAllRatingsBySession(sessionId1).join();

        assertThat(ratings).hasSize(2);
        assertThat(ratings.get(0).getSessionId()).isEqualTo(sessionId1);
        assertThat(ratings.get(1).getSessionId()).isEqualTo(sessionId1);
    }

    @Test
    void testGetRatingByMenuAndSessionWhenExists() {
        Rating rating = new Rating();
        rating.setRating(5);
        rating.setReview("Enak bangeet");
        rating.setSessionId(sessionId1);
        rating.setMenu(menu1);

        when(ratingRepository.findById(sessionId1)).thenReturn(Optional.of(rating));

        Optional<Rating> result = ratingService.getRatingByMenuAndSession(menu1, sessionId1);

        assertTrue(result.isPresent());
        assertEquals(rating, result.get());
    }

    @Test
    void testGetRatingByMenuAndSessionWhenNotExists() {
        when(ratingRepository.findById(sessionId1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
        () -> ratingService.getRatingByMenuAndSession(menu1, sessionId1)
        );

        assertEquals("Rating not found", exception.getReason());
    }
}