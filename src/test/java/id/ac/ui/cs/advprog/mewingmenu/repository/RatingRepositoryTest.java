package id.ac.ui.cs.advprog.mewingmenu.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import id.ac.ui.cs.advprog.mewingmenu.rating.model.Rating;
import id.ac.ui.cs.advprog.mewingmenu.rating.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;



@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RatingRepositoryTest {

    @Autowired
    private RatingRepository ratingRepository;

    private String userId;
    private Menu menu;


    private static Menu createValidMenu() {
        Menu menu = new Menu();
        menu.setName("Delicious Burger");
        menu.setDescription("A super tasty burger with special sauce.");
        menu.setImageUrl("http://example.com/burger.jpg");
        menu.setPrice(BigDecimal.valueOf(45000));
        return menu;
    }

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setUp() {
        ratingRepository.deleteAll();
        userId = UUID.randomUUID().toString();
        this.menu = createValidMenu();
        entityManager.persist(menu); // <- penting!
        entityManager.flush();
    }

    @Test
    @DisplayName("It should save and retrieve a Rating")
    public void testSaveAndFindRating() {
        Rating rating = new Rating();
        rating.setRating(5);
        rating.setReview("Rasanya enak :)");
        rating.setUserId(userId);
        rating.setMenu(menu);

        Rating savedRating = ratingRepository.save(rating);

        Optional<Rating> retrieved = ratingRepository.findById(savedRating.getId());

        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getRating()).isEqualTo(5);
        assertThat(retrieved.get().getReview()).isEqualTo("Rasanya enak :)");
        assertThat(retrieved.get().getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("It should update a Rating")
    public void testUpdateRating() {
        Rating rating = new Rating();
        rating.setRating(5);
        rating.setReview("Rasanya enak :)");
        rating.setUserId(userId);
        rating.setMenu(menu);

        rating = ratingRepository.save(rating);

        rating.setRating(4);
        rating.setReview("Rasanya oke:)");
        Rating updatedRating = ratingRepository.save(rating);

        Optional<Rating> retrieved = ratingRepository.findById(updatedRating.getId());

        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getRating()).isEqualTo(4);
        assertThat(retrieved.get().getReview()).isEqualTo("Rasanya oke:)");
    }

    @Test
    @DisplayName("It should delete a Rating")
    public void testDeleteRating() {
        Rating rating = new Rating();
        rating.setRating(5);
        rating.setReview("Rasanya enak :)");
        rating.setUserId(userId);
        rating.setMenu(menu);

        Rating savedRating = ratingRepository.save(rating);
        String ratingId = savedRating.getId();

        ratingRepository.deleteById(ratingId);

        Optional<Rating> deleted = ratingRepository.findById(ratingId);
        assertThat(deleted).isNotPresent();
    }
}
