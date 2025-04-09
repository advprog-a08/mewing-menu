package id.ac.ui.cs.advprog.mewingmenu.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.MenuCategory;
import id.ac.ui.cs.advprog.mewingmenu.menu.repository.MenuCategoryRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MenuCategoryRepositoryTest {

    @Autowired
    private MenuCategoryRepository menuCategoryRepository;

    private MenuCategory category;

    @BeforeEach
    public void setUp() {
        menuCategoryRepository.deleteAll();

        // Create a menu category
        MenuCategory category = new MenuCategory();
        category.setName("Main Course");
        category.setDescription("Main course meals");

        menuCategoryRepository.save(category);
        this.category = category;
    }

    @Test
    @DisplayName("It should save and find a MenuCategory")
    void testSaveAndFindMenuCategory() {
        MenuCategory category = new MenuCategory();
        category.setName("Drinks");

        menuCategoryRepository.save(category);

        Optional<MenuCategory> result = menuCategoryRepository.findById(category.getId());
        assertTrue(result.isPresent());
        assertEquals("Drinks", result.get().getName());
    }

    @Test
    @DisplayName("It should delete a MenuCategory")
    void testDeleteMenuCategory() {
        MenuCategory category = new MenuCategory();
        category.setName("Desserts");

        menuCategoryRepository.save(category);
        String categoryId = category.getId();

        menuCategoryRepository.deleteById(categoryId);

        Optional<MenuCategory> result = menuCategoryRepository.findById(categoryId);
        assertFalse(result.isPresent());
    }
    
    @Test
    @DisplayName("It should return empty when MenuCategory is not found")
    void testFindNonExistingCategory() {

        String nonExistingId = this.category.getId();

        // Delete the category
        menuCategoryRepository.deleteById(nonExistingId);
        
        Optional<MenuCategory> result = menuCategoryRepository.findById(nonExistingId);
        assertTrue(result.isEmpty());
    }
}
