package id.ac.ui.cs.advprog.mewingmenu.menu.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.MenuCategory;

public interface MenuCategoryService {
    List<MenuCategory> getAllMenuCategories();
    Optional<MenuCategory> getMenuCategoryById(UUID id);
    MenuCategory createMenuCategory(MenuCategory menuCategory);
    Optional<MenuCategory> updateMenuCategory(UUID id, MenuCategory menuCategory);
    void deleteMenuCategory(UUID id);
}
