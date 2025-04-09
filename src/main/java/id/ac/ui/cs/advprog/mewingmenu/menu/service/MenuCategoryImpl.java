package id.ac.ui.cs.advprog.mewingmenu.menu.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import id.ac.ui.cs.advprog.mewingmenu.menu.model.MenuCategory;
import id.ac.ui.cs.advprog.mewingmenu.menu.repository.MenuCategoryRepository;

@Service
public class MenuCategoryImpl implements MenuCategoryService {
    @Autowired
    private MenuCategoryRepository menuCategoryRepository;

    @Override
    public List<MenuCategory> getAllMenuCategories() {
    }

    @Override
    public Optional<MenuCategory> getMenuCategoryById(UUID id) {
    }

    @Override
    public MenuCategory createMenuCategory(MenuCategory menuCategory) {
    }

    @Override
    public Optional<MenuCategory> updateMenuCategory(UUID id, MenuCategory menuCategory) {

    }

    @Override
    public void deleteMenuCategory(UUID id) {

    }
}
