package id.ac.ui.cs.advprog.mewingmenu.controller;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import id.ac.ui.cs.advprog.mewingmenu.rating.controller.RatingController;
import id.ac.ui.cs.advprog.mewingmenu.rating.dto.RatingDto;
import id.ac.ui.cs.advprog.mewingmenu.rating.model.Rating;
import id.ac.ui.cs.advprog.mewingmenu.rating.service.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
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
    private RatingService ratingService;

    @InjectMocks
    private RatingController ratingController;

    private Rating testRating;
    private RatingDto testRatingDto;
    private String sessionId;
    private String menuId;
    private String ratingId;

    @BeforeEach
    void setUp() {
        sessionId = "test-session-123";
        menuId = "menu-123";
        ratingId = "rating-123";

        testRating = new Rating();
        testRating.setId(ratingId);
        testRating.setSessionId(sessionId);
        testRating.setRating(5);
        testRating.setReview("Great food!");

        testRatingDto = new RatingDto();
        testRatingDto.setId(ratingId);
        testRatingDto.setSessionId(sessionId);
        testRatingDto.setRating(5);
        testRatingDto.setReview("Great food!");
    }

    @Test
    void addRating_Success() throws ExecutionException, InterruptedException {
        when(ratingService.addRating(any(Rating.class)))
                .thenReturn(CompletableFuture.completedFuture(testRatingDto));

        CompletableFuture<ResponseEntity<RatingController.ApiResponse<RatingDto>>> result = 
                ratingController.addRating(testRating, sessionId);

        ResponseEntity<RatingController.ApiResponse<RatingDto>> response = result.get();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Rating added successfully.", response.getBody().getMessage());
        assertEquals(testRatingDto, response.getBody().getData());

        verify(ratingService).addRating(any(Rating.class));
    }


    @Test
    void getAllRatings_Success() throws ExecutionException, InterruptedException {
        List<RatingDto> ratings = Arrays.asList(testRatingDto);
        when(ratingService.getAll()).thenReturn(CompletableFuture.completedFuture(ratings));

        CompletableFuture<ResponseEntity<RatingController.ApiResponse<List<RatingDto>>>> result = 
                ratingController.getAllRatings();

        ResponseEntity<RatingController.ApiResponse<List<RatingDto>>> response = result.get();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Successfully fetched all ratings.", response.getBody().getMessage());
        assertEquals(ratings, response.getBody().getData());

        verify(ratingService).getAll();
    }

    @Test
    void getRatingById_Success() {
        when(ratingService.findById(ratingId)).thenReturn(Optional.of(testRatingDto));

        ResponseEntity<RatingController.ApiResponse<RatingDto>> response = 
                ratingController.getRatingById(ratingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Rating found successfully.", response.getBody().getMessage());
        assertEquals(testRatingDto, response.getBody().getData());

        verify(ratingService).findById(ratingId);
    }

    @Test
    void getRatingById_NotFound() {
        when(ratingService.findById(ratingId)).thenReturn(Optional.empty());

        ResponseEntity<RatingController.ApiResponse<RatingDto>> response = 
                ratingController.getRatingById(ratingId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Rating not found.", response.getBody().getMessage());
        assertNull(response.getBody().getData());

        verify(ratingService).findById(ratingId);
    }

    @Test
    void getRatingsByMenu_Success() throws ExecutionException, InterruptedException {
        List<RatingDto> ratings = Arrays.asList(testRatingDto);
        when(ratingService.getAllRatingsByMenu(any(Menu.class)))
                .thenReturn(CompletableFuture.completedFuture(ratings));

        CompletableFuture<ResponseEntity<RatingController.ApiResponse<List<RatingDto>>>> result = 
                ratingController.getRatingsByMenu(menuId);

        ResponseEntity<RatingController.ApiResponse<List<RatingDto>>> response = result.get();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Successfully fetched ratings for menu.", response.getBody().getMessage());
        assertEquals(ratings, response.getBody().getData());

        verify(ratingService).getAllRatingsByMenu(any(Menu.class));
    }

    @Test
    void deleteRating_Success() {
        doNothing().when(ratingService).deleteRatingById(ratingId, sessionId);

        ResponseEntity<RatingController.ApiResponse<Void>> response = 
                ratingController.deleteRating(ratingId, sessionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Rating deleted successfully.", response.getBody().getMessage());
        assertNull(response.getBody().getData());

        verify(ratingService).deleteRatingById(ratingId, sessionId);
    }

    @Test
    void deleteRating_NotFound() {
        doThrow(new IllegalStateException("Rating not found"))
                .when(ratingService).deleteRatingById(ratingId, sessionId);

        ResponseEntity<RatingController.ApiResponse<Void>> response = 
                ratingController.deleteRating(ratingId, sessionId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Rating not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());

        verify(ratingService).deleteRatingById(ratingId, sessionId);
    }

    @Test
    void deleteRating_Forbidden() {
        doThrow(new SecurityException("Access denied"))
                .when(ratingService).deleteRatingById(ratingId, sessionId);

        ResponseEntity<RatingController.ApiResponse<Void>> response = 
                ratingController.deleteRating(ratingId, sessionId);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Access denied", response.getBody().getMessage());
        assertNull(response.getBody().getData());

        verify(ratingService).deleteRatingById(ratingId, sessionId);
    }

    @Test
    void updateRating_Success() throws ExecutionException, InterruptedException {
        Rating updatedRating = new Rating();
        updatedRating.setRating(4);
        updatedRating.setReview("Updated review");

        RatingDto updatedRatingDto = new RatingDto();
        updatedRatingDto.setId(ratingId);
        updatedRatingDto.setRating(4);
        updatedRatingDto.setReview("Updated review");

        when(ratingService.updateRating(eq(ratingId), any(Rating.class), eq(sessionId)))
                .thenReturn(CompletableFuture.completedFuture(updatedRatingDto));

        CompletableFuture<ResponseEntity<RatingController.ApiResponse<RatingDto>>> result = 
                ratingController.updateRating(ratingId, updatedRating, sessionId);

        ResponseEntity<RatingController.ApiResponse<RatingDto>> response = result.get();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Rating updated successfully.", response.getBody().getMessage());
        assertEquals(updatedRatingDto, response.getBody().getData());

        verify(ratingService).updateRating(eq(ratingId), any(Rating.class), eq(sessionId));
    }


    @Test
    void getAllBySession_Success() {
        List<RatingDto> ratings = Arrays.asList(testRatingDto);
        when(ratingService.getAllRatingsBySession(sessionId)).thenReturn(ratings);

        ResponseEntity<RatingController.ApiResponse<List<RatingDto>>> response = 
                ratingController.getAllBySession(sessionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Successfully fetched user ratings.", response.getBody().getMessage());
        assertEquals(ratings, response.getBody().getData());

        verify(ratingService).getAllRatingsBySession(sessionId);
    }

    @Test
    void getRatingByMenuAndSession_Success() {
        when(ratingService.getRatingByMenuAndSession(any(Menu.class), eq(sessionId)))
                .thenReturn(Optional.of(testRatingDto));

        ResponseEntity<RatingController.ApiResponse<RatingDto>> response = 
                ratingController.getRatingByMenuAndSession(menuId, sessionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Rating found successfully.", response.getBody().getMessage());
        assertEquals(testRatingDto, response.getBody().getData());

        verify(ratingService).getRatingByMenuAndSession(any(Menu.class), eq(sessionId));
    }

    @Test
    void getRatingByMenuAndSession_NotFound() {
        when(ratingService.getRatingByMenuAndSession(any(Menu.class), eq(sessionId)))
                .thenReturn(Optional.empty());

        ResponseEntity<RatingController.ApiResponse<RatingDto>> response = 
                ratingController.getRatingByMenuAndSession(menuId, sessionId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Rating not found for this menu and user.", response.getBody().getMessage());
        assertNull(response.getBody().getData());

        verify(ratingService).getRatingByMenuAndSession(any(Menu.class), eq(sessionId));
    }

    @Test
    void apiResponse_Constructor_Success() {
        String message = "Test message";
        String data = "Test data";

        RatingController.ApiResponse<String> response = 
                new RatingController.ApiResponse<>(true, message, data);

        assertTrue(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());
    }

    @Test
    void apiResponse_Setters_Success() {
        RatingController.ApiResponse<String> response = 
                new RatingController.ApiResponse<>(false, "Initial", null);

        response.setSuccess(true);
        response.setMessage("Updated message");
        response.setData("Updated data");

        assertTrue(response.isSuccess());
        assertEquals("Updated message", response.getMessage());
        assertEquals("Updated data", response.getData());
    }
}