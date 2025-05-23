package id.ac.ui.cs.advprog.mewingmenu.controller;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import id.ac.ui.cs.advprog.mewingmenu.model.TableSession;
import id.ac.ui.cs.advprog.mewingmenu.rating.controller.RatingController;
import id.ac.ui.cs.advprog.mewingmenu.rating.model.Rating;
import id.ac.ui.cs.advprog.mewingmenu.rating.service.RatingServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingControllerTest {

    @Mock
    private RatingServiceImpl ratingService;

    @InjectMocks
    private RatingController ratingController;

    private Rating testRating;
    private Menu testMenu;
    private TableSession testTableSession;

    @BeforeEach
    void setUp() {
        testMenu = new Menu();
        testMenu.setId("menu1");

        testRating = new Rating();
        testRating.setId("1");
        testRating.setMenu(testMenu);
        testRating.setSessionId("session1");
        testRating.setRating(5);
        testRating.setReview("Great food!");

        testTableSession = new TableSession("session 1", "table 1", true);
        testTableSession.setId("session1");
    }

    @Test
    void testAddRating() throws ExecutionException, InterruptedException {
        when(ratingService.addRating(any(Rating.class)))
            .thenReturn(CompletableFuture.completedFuture(testRating));

        ResponseEntity<CompletableFuture<Rating>> response = ratingController.addRating(testRating);
        Rating result = response.getBody().get();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testRating, result);
        verify(ratingService).addRating(any(Rating.class));
    }

    @Test
    void testGetRatingById_Found() {
        when(ratingService.findById("1")).thenReturn(Optional.of(testRating));

        ResponseEntity<Rating> response = ratingController.getRatingById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testRating, response.getBody());
        verify(ratingService).findById("1");
    }

    @Test
    void testGetRatingById_NotFound() {
        when(ratingService.findById("2")).thenReturn(Optional.empty());

        ResponseEntity<Rating> response = ratingController.getRatingById("2");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(ratingService).findById("2");
    }

    @Test
    void testGetRatingsByMenu() throws ExecutionException, InterruptedException {
        List<Rating> ratings = List.of(testRating);
        when(ratingService.getAllRatingsByMenu(any(Menu.class)))
            .thenReturn(CompletableFuture.completedFuture(ratings));

        ResponseEntity<CompletableFuture<List<Rating>>> response = 
            ratingController.getRatingsByMenu("menu1");
        List<Rating> result = response.getBody().get();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, result.size());
        assertEquals(testRating, result.get(0));
        verify(ratingService).getAllRatingsByMenu(any(Menu.class));
    }

    @Test
    void testDeleteRating() {
        doNothing().when(ratingService).deleteRatingById(eq("1"), anyString());
    
        ResponseEntity<Void> response = ratingController.deleteRating("1", testTableSession);
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(ratingService).deleteRatingById(eq("1"), anyString());
    }

    @Test
    void testUpdateRating() throws ExecutionException, InterruptedException {
        Rating updatedRating = new Rating();
        updatedRating.setId("1");
        updatedRating.setRating(4);
        updatedRating.setReview("Good but could be better");

        when(ratingService.updateRating(eq("1"), any(Rating.class), eq("session1")))
            .thenReturn(CompletableFuture.completedFuture(updatedRating));

        ResponseEntity<CompletableFuture<Rating>> response = 
            ratingController.updateRating("1", updatedRating, testTableSession);
        Rating result = response.getBody().get();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedRating, result);
        verify(ratingService).updateRating("1", updatedRating, "session1");
    }

    @Test
    void testGetAllBySession() throws ExecutionException, InterruptedException {
        List<Rating> ratings = List.of(testRating);
        when(ratingService.getAllRatingsBySession("session1"))
            .thenReturn(CompletableFuture.completedFuture(ratings));

        ResponseEntity<CompletableFuture<List<Rating>>> response = 
            ratingController.getAllBySession(testTableSession);
        List<Rating> result = response.getBody().get();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, result.size());
        assertEquals(testRating, result.get(0));
        verify(ratingService).getAllRatingsBySession("session1");
    }

    @Test
    void testGetRatingByMenuAndSession_Found() {
        when(ratingService.getRatingByMenuAndSession(argThat(menu -> "menu1".equals(menu.getId())), eq("session1")))
            .thenReturn(Optional.of(testRating));

        ResponseEntity<Rating> response = 
            ratingController.getRatingByMenuAndSession("menu1", testTableSession);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testRating, response.getBody());
        verify(ratingService).getRatingByMenuAndSession(argThat(menu -> "menu1".equals(menu.getId())), eq("session1"));
    }

    @Test
    void testGetRatingByMenuAndSession_NotFound() {
        when(ratingService.getRatingByMenuAndSession(argThat(menu -> "menu1".equals(menu.getId())), eq("session1")))
            .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            ratingController.getRatingByMenuAndSession("menu1", testTableSession);
        });

        verify(ratingService).getRatingByMenuAndSession(argThat(menu -> "menu1".equals(menu.getId())), eq("session1"));
    }
}