package id.ac.ui.cs.advprog.mewingmenu.menu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.MenuCategory;
import id.ac.ui.cs.advprog.mewingmenu.menu.service.MenuCategoryService;
import id.ac.ui.cs.advprog.mewingmenu.utils.ResponseUtil;


@RestController
@RequestMapping("/api/menu-categories")
public class MenuCategoryController {

    @Autowired
    private MenuCategoryService menuCategoryService;

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<MenuCategory> categories = menuCategoryService.getAllMenuCategories();
        return ResponseUtil.success(categories, "Fetched all menu categories");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable String id) {
        return menuCategoryService.getMenuCategoryById(id)
                .map(category -> ResponseUtil.success(category, "Fetched menu category"))
                .orElse(ResponseUtil.notFound("Menu category not found"));
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody MenuCategory category) {
        MenuCategory created = menuCategoryService.createMenuCategory(category);
        return ResponseUtil.created(created, "Menu category created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable String id, @RequestBody MenuCategory category) {
        return menuCategoryService.updateMenuCategory(id, category)
                .map(updated -> ResponseUtil.success(updated, "Menu category updated successfully"))
                .orElse(ResponseUtil.notFound("Menu category not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id) {
        try {
            menuCategoryService.deleteMenuCategory(id);
            return ResponseUtil.success(null, "Menu category deleted successfully");
        } catch (IllegalStateException e) {
            return ResponseUtil.error(e.getMessage(), org.springframework.http.HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseUtil.error("Menu category not found", org.springframework.http.HttpStatus.NOT_FOUND);
        }
    }
}
