package id.ac.ui.cs.advprog.mewingmenu.service;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import id.ac.ui.cs.advprog.mewingmenu.rating.model.Rating;
import id.ac.ui.cs.advprog.mewingmenu.rating.repository.RatingRepository;
import id.ac.ui.cs.advprog.mewingmenu.rating.service.RatingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

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
    void testUserCanSuccessfullyAddRating() {
        when(ratingRepository.findBySessionIdAndMenu(sessionId1, menu1))
                .thenReturn(Optional.empty());

        when(ratingRepository.save(rating)).thenReturn(rating);

        Rating result = ratingService.addRating(rating);

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

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            ratingService.addRating(duplicateRating);
        });

        assertEquals("User has already reviewed this menu", exception.getMessage());
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
    void testGetAllRatingsByMenu() {
        when(ratingRepository.findAllByMenu(menu1)).thenReturn(List.of(rating, rating2));

        List<Rating> result = ratingService.getAllRatingsByMenu(menu1);

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
    void testUpdateRatingByOwnerSuccess() {
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

        Rating result = ratingService.updateRating(ratingId, updatedData, userId);

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

        Exception ex = assertThrows(IllegalStateException.class, () ->
                ratingService.updateRating(ratingId, updatedData, attackerUserId)
        );

        assertEquals("You are not allowed to update this rating", ex.getMessage());
        verify(ratingRepository, never()).save(any());
    }

}
