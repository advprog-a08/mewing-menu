package id.ac.ui.cs.advprog.mewingmenu.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.MenuCategory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class MenuCategoryTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidMenuCategory() {
        MenuCategory category = new MenuCategory(
                UUID.randomUUID().toString(),
                "Beverages",
                "All kinds of drinks",
                null);

        Set<ConstraintViolation<MenuCategory>> violations = validator.validate(category);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testNameTooShort() {
        MenuCategory category = new MenuCategory(
                UUID.randomUUID().toString(),
                "Food",
                "Some description",
                null);

        Set<ConstraintViolation<MenuCategory>> violations = validator.validate(category);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    public void testNameBlank() {
        MenuCategory category = new MenuCategory(
                UUID.randomUUID().toString(),

                "  ",
                "Some description",
                null);

        Set<ConstraintViolation<MenuCategory>> violations = validator.validate(category);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    public void testDescriptionTooLong() {
        String longDescription = "A".repeat(256);
        MenuCategory category = new MenuCategory(
                UUID.randomUUID().toString(),
                "Desserts",
                longDescription,
                null);

        Set<ConstraintViolation<MenuCategory>> violations = validator.validate(category);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }
}
