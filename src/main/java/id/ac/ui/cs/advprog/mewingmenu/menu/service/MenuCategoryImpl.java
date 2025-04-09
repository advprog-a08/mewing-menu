package id.ac.ui.cs.advprog.mewingmenu.menu.service;

import java.util.List;
import java.util.Optional;


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
        return menuCategoryRepository.findAll();
    }

    @Override
    public Optional<MenuCategory> getMenuCategoryById(String id) {
        return menuCategoryRepository.findById(id);
    }

    @Override
    public MenuCategory createMenuCategory(MenuCategory menuCategory) {
        Optional<MenuCategory> existingCategory = menuCategoryRepository.findByName(menuCategory.getName());

        if (existingCategory.isPresent()) {
            throw new IllegalStateException("Menu category with name " + menuCategory.getName() + " already exists");
        }

        return menuCategoryRepository.save(menuCategory);
    }

    @Override
    public Optional<MenuCategory> updateMenuCategory(String id, MenuCategory menuCategory) {
        return menuCategoryRepository.findById(id)
                .map(existingMenuCategory -> {
                    existingMenuCategory.setName(menuCategory.getName());
                    existingMenuCategory.setDescription(menuCategory.getDescription());
                    return menuCategoryRepository.save(existingMenuCategory);
                });
    }

    @Override
    public void deleteMenuCategory(String id) {
        // Check if theres any menu in this category
        List<Menu> menus = menuCategoryRepository.findById(id).get().getMenus();
        if (menus.isEmpty()) {
            menuCategoryRepository.deleteById(id);
        } else {
            throw new IllegalStateException("Cannot delete category with existing menus");
        }
    }
}
