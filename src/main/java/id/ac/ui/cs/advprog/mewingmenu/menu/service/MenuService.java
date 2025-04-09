package id.ac.ui.cs.advprog.mewingmenu.menu.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;

public interface MenuService {
    List<Menu> getAllMenus();
    Optional<Menu> getMenuById(UUID id);
    Menu createMenu(Menu menu);
    Optional<Menu> updateMenu(UUID id, Menu menu);
    void deleteMenu(UUID id);
}
