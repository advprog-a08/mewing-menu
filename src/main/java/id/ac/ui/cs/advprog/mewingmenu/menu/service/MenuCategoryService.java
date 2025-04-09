package id.ac.ui.cs.advprog.mewingmenu.menu.service;

import java.util.List;
import java.util.Optional;


import id.ac.ui.cs.advprog.mewingmenu.menu.model.MenuCategory;

public interface MenuCategoryService {
    List<MenuCategory> getAllMenuCategories();
    Optional<MenuCategory> getMenuCategoryById(String id);
    MenuCategory createMenuCategory(MenuCategory menuCategory);
    Optional<MenuCategory> updateMenuCategory(String id, MenuCategory menuCategory);
    void deleteMenuCategory(String id);
}
