package id.ac.ui.cs.advprog.mewingmenu.menu.controller;

import java.util.List;
import java.util.Optional;

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

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import id.ac.ui.cs.advprog.mewingmenu.menu.service.MenuService;
import id.ac.ui.cs.advprog.mewingmenu.utils.ApiResponse;
import id.ac.ui.cs.advprog.mewingmenu.utils.ResponseUtil;


@RestController
@RequestMapping("/api/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Menu>>> getAllMenus() {
        List<Menu> menus = menuService.getAllMenus();
        return ResponseUtil.success(menus, "Successfully fetched all menus.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Menu>> getMenuById(@PathVariable String id) {
        Optional<Menu> menu = menuService.getMenuById(id);
        return menu
                .map(m -> ResponseUtil.success(m, "Menu found."))
                .orElse(ResponseUtil.notFound("Menu not found with ID: " + id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Menu>> createMenu(@RequestBody Menu menu) {
        Menu created = menuService.createMenu(menu);
        return ResponseUtil.created(created, "Menu created successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Menu>> updateMenu(@PathVariable String id, @RequestBody Menu menu) {
        Optional<Menu> updated = menuService.updateMenu(id, menu);
        return updated
                .map(m -> ResponseUtil.success(m, "Menu updated successfully."))
                .orElse(ResponseUtil.notFound("Menu not found for update with ID: " + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMenu(@PathVariable String id) {
        Optional<Menu> menu = menuService.getMenuById(id);
        if (menu.isPresent()) {
            menuService.deleteMenu(id);
            return ResponseUtil.success(null, "Menu deleted successfully.");
        } else {
            return ResponseUtil.notFound("Menu not found for deletion with ID: " + id);
        }
    }
}
