package id.ac.ui.cs.advprog.mewingmenu.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import id.ac.ui.cs.advprog.mewingmenu.menu.model.MenuCategory;
import id.ac.ui.cs.advprog.mewingmenu.menu.repository.MenuCategoryRepository;
import id.ac.ui.cs.advprog.mewingmenu.menu.repository.MenuRepository;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuCategoryRepository menuCategoryRepository;

    private MenuCategory category;

    @BeforeEach
    public void setUp() {
        menuRepository.deleteAll();
        menuCategoryRepository.deleteAll();

        // Create a menu category
        MenuCategory category = new MenuCategory();
        category.setName("Main Course");
        category.setDescription("Main course meals");
        menuCategoryRepository.save(category);

        this.category = category;
    }

    @Test
    @DisplayName("It should save and retrieve a Menu")
    public void testSaveAndFindMenu() {
        // Create a menu
        Menu menu = new Menu();
        menu.setName("Spaghetti Carbonara");
        menu.setDescription("Delicious creamy pasta");
        menu.setImageUrl("http://example.com/image.jpg");
        menu.setPrice(BigDecimal.valueOf(65000));
        menu.setCategory(category);

        // Save the menu
        Menu savedMenu = menuRepository.save(menu);

        // Retrieve the menu
        Optional<Menu> retrieved = menuRepository.findById(savedMenu.getId());

        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getName()).isEqualTo("Spaghetti Carbonara");
        assertThat(retrieved.get().getCategory().getName()).isEqualTo("Main Course");
    }

    @Test
    @DisplayName("It should delete a Menu")
    public void testDeleteMenu() {
        Menu menu = new Menu();
        menu.setName("Nasi Goreng Spesial");
        menu.setDescription("Fried rice with extra toppings");
        menu.setImageUrl("http://example.com/nasgor.jpg");
        menu.setPrice(BigDecimal.valueOf(40000));
        menu.setCategory(category);
        menu = menuRepository.save(menu);

        String menuId = menu.getId();
        menuRepository.deleteById(menuId);

        Optional<Menu> deleted = menuRepository.findById(menuId);
        assertThat(deleted).isNotPresent();
    }

    @Test
    @DisplayName("It should update a Menu")
    public void testUpdateMenu() {
        Menu menu = new Menu();
        menu.setName("Sushi");
        menu.setDescription("Japanese rice dish");
        menu.setImageUrl("http://example.com/sushi.jpg");
        menu.setPrice(BigDecimal.valueOf(80000));
        menu.setCategory(category);
        menu = menuRepository.save(menu);

        // Update the menu
        menu.setName("Sushi Deluxe");
        menu.setPrice(BigDecimal.valueOf(90000));
        Menu updatedMenu = menuRepository.save(menu);

        // Retrieve the updated menu
        Optional<Menu> retrieved = menuRepository.findById(updatedMenu.getId());

        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getName()).isEqualTo("Sushi Deluxe");
        assertThat(retrieved.get().getPrice()).isEqualTo(BigDecimal.valueOf(90000));
    }
}
