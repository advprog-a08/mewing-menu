package id.ac.ui.cs.advprog.mewingmenu.menu.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, String> {
    Optional<Menu> findByName(String name);
}
