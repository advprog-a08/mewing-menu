package id.ac.ui.cs.advprog.mewingmenu.model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Set;

import id.ac.ui.cs.advprog.mewingmenu.rating.model.Rating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;


public class RatingTest {
    private Validator validator;

    private static Menu createValidMenu() {
        Menu menu = new Menu();
        menu.setName("Delicious Burger");
        menu.setDescription("A super tasty burger with special sauce.");
        menu.setImageUrl("http://example.com/burger.jpg");
        menu.setPrice(BigDecimal.valueOf(45000));
        return menu;
    }

    private static Rating createValidRating() {
        Rating rating = new Rating();
        rating.setUserId("8efedadb-bd8a-4b1c-901c-05e344047142");
        rating.setMenu(createValidMenu());
        rating.setRating(5);
        rating.setReview("Mantap banget");
        return rating;
    }

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidRatingShouldPassValidation() {
        Rating rating = createValidRating();
        Set<ConstraintViolation<Rating>> violations = validator.validate(rating);
        assertTrue(violations.isEmpty(), "Expected no constraint violations");
    }

    @Test
    void testInvalidRatingRangeTooBig() {
        Rating rating = createValidRating();
        rating.setRating(6);
        Set<ConstraintViolation<Rating>> violations = validator.validate(rating);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("rating")));
    }

    @Test
    void testInvalidRatingRangeTooSmall() {
        Rating rating = createValidRating();
        rating.setRating(0);
        Set<ConstraintViolation<Rating>> violations = validator.validate(rating);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("rating")));
    }

    @Test
    void testReviewTooLong() {
        Rating rating = createValidRating();
        rating.setReview("A".repeat(257));
        Set<ConstraintViolation<Rating>> violations = validator.validate(rating);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("review")));
    }

    @Test
    void testMissingRequiredField() {
        Rating rating = new Rating();

        Set<ConstraintViolation<Rating>> violations = validator.validate(rating);
        assertFalse(violations.isEmpty());
        assertEquals(3, violations.size());
    }
}
