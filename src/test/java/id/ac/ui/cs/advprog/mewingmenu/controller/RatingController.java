package id.ac.ui.cs.advprog.mewingmenu.controller;

import id.ac.ui.cs.advprog.mewingmenu.annotation.AuthenticatedTableSession;
import id.ac.ui.cs.advprog.mewingmenu.annotation.RequireTableSession;
import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import id.ac.ui.cs.advprog.mewingmenu.rating.controller.RatingController;
import id.ac.ui.cs.advprog.mewingmenu.rating.model.Rating;
import id.ac.ui.cs.advprog.mewingmenu.rating.service.RatingService;
import table_session.TableSessionOuterClass.TableSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RatingControllerTest {

    @Mock
    private RatingService ratingService;

    @InjectMocks
    private RatingController ratingController;

    private Rating testRating;
    private TableSession testTableSession;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testTableSession = TableSession.newBuilder()
                .setId("session-123")
                .build();
        
        testRating = new Rating();
        testRating.setId("rating-1");
        testRating.setSessionId("session-123");
        testRating.setRating(5);
        testRating.setReview("Great food!");
    }

    @Test
    void testAddRating_Success() throws ExecutionException, InterruptedException {
        when(ratingService.addRating(any(Rating.class)))
                .thenReturn(CompletableFuture.completedFuture(testRating));
        
        ResponseEntity<CompletableFuture<Rating>> response = 
                ratingController.addRating(testRating, testTableSession);
        
        assertNotNull(response.getBody());
        assertEquals(testRating, response.getBody().get());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(ratingService, times(1)).addRating(any(Rating.class));
    }

    @Test
    void testGetRatingById_Found() {
        when(ratingService.findById("rating-1")).thenReturn(Optional.of(testRating));
        
        ResponseEntity<Rating> response = ratingController.getRatingById("rating-1");
        
        assertEquals(testRating, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    @Test
    void testGetRatingById_NotFound() {
        when(ratingService.findById("nonexistent-id")).thenReturn(Optional.empty());
        
        ResponseEntity<Rating> response = ratingController.getRatingById("nonexistent-id");
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetRatingsByMenu() throws ExecutionException, InterruptedException {
        List<Rating> ratings = List.of(testRating);
        when(ratingService.getAllRatingsByMenu(any(Menu.class)))
                .thenReturn(CompletableFuture.completedFuture(ratings));
        
        ResponseEntity<CompletableFuture<List<Rating>>> response = 
                ratingController.getRatingsByMenu("menu-1");
        
        assertEquals(ratings, response.getBody().get());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteRating_Success() {
        doNothing().when(ratingService).deleteRatingById(anyString(), anyString());
        
        ResponseEntity<Void> response = 
                ratingController.deleteRating("rating-1", testTableSession);
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(ratingService, times(1)).deleteRatingById(anyString(), anyString());
    }

    @Test
    void testUpdateRating_Success() throws ExecutionException, InterruptedException {
        when(ratingService.updateRating(anyString(), any(Rating.class), anyString()))
                .thenReturn(CompletableFuture.completedFuture(testRating));
        
        ResponseEntity<CompletableFuture<Rating>> response = 
                ratingController.updateRating("rating-1", testRating, testTableSession);
        
        assertEquals(testRating, response.getBody().get());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAllBySession() throws ExecutionException, InterruptedException {
        List<Rating> ratings = List.of(testRating);
        when(ratingService.getAllRatingsBySession(anyString()))
                .thenReturn(CompletableFuture.completedFuture(ratings));
        
        ResponseEntity<CompletableFuture<List<Rating>>> response = 
                ratingController.getAllBySession(testTableSession);
        
        assertEquals(ratings, response.getBody().get());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetRatingByMenuAndSession_Found() {
        when(ratingService.getRatingByMenuAndSession(any(Menu.class), anyString()))
                .thenReturn(Optional.of(testRating));
        
        ResponseEntity<Rating> response = 
                ratingController.getRatingByMenuAndSession("menu-1", testTableSession);
        
        assertEquals(testRating, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetRatingByMenuAndSession_NotFound() {
        when(ratingService.getRatingByMenuAndSession(any(Menu.class), anyString()))
                .thenReturn(Optional.empty());
        
        assertThrows(ResponseStatusException.class, () -> 
                ratingController.getRatingByMenuAndSession("menu-1", testTableSession));
    }

    @Test
    void testAddRating_Exception() {
        CompletableFuture<Rating> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("Service error"));
        
        when(ratingService.addRating(any(Rating.class))).thenReturn(failedFuture);
        
        ResponseEntity<CompletableFuture<Rating>> response = 
                ratingController.addRating(testRating, testTableSession);
        
        assertThrows(ExecutionException.class, () -> response.getBody().get());
    }
}