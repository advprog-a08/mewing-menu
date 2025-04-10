package id.ac.ui.cs.advprog.mewingmenu.rating.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import id.ac.ui.cs.advprog.mewingmenu.rating.model.Rating;
import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;

@Repository
public interface RatingRepository extends JpaRepository<Rating, String> {
}
