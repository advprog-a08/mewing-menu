package id.ac.ui.cs.advprog.mewingmenu.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class MenuTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private static Menu createValidMenu() {
        Menu menu = new Menu();
        menu.setName("Delicious Burger");
        menu.setDescription("A super tasty burger with special sauce.");
        menu.setImageUrl("http://example.com/burger.jpg");
        menu.setPrice(BigDecimal.valueOf(45000));
        return menu;
    }

    @Test
    void testValidMenuShouldPassValidation() {
        Menu menu = createValidMenu();
        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertTrue(violations.isEmpty(), "Expected no constraint violations");
    }

    @Test
    void testInvalidNameTooShort() {
        Menu menu = createValidMenu();
        menu.setName("A");

        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void testInvalidBlankDescription() {
        Menu menu = createValidMenu();
        menu.setDescription("");

        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void testPriceCannotBeNegative() {
        Menu menu = createValidMenu();
        menu.setPrice(BigDecimal.valueOf(-5000));

        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("price")));
    }

    @Test 
    void testPriceTooHigh() {
        Menu menu = createValidMenu();
        menu.setPrice(BigDecimal.valueOf(1000001));

        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("price")));
    }

    @Test
    void testMissingRequiredFields() {
        Menu menu = new Menu();

        Set<ConstraintViolation<Menu>> violations = validator.validate(menu);
        assertFalse(violations.isEmpty());
        assertEquals(4, violations.size());
    }
}
