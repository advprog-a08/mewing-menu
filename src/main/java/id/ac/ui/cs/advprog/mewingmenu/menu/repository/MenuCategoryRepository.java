package id.ac.ui.cs.advprog.mewingmenu.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.MenuCategory;


@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, String> {}
