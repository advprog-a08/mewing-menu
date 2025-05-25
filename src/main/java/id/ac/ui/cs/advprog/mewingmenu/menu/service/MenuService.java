package id.ac.ui.cs.advprog.mewingmenu.menu.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;

public interface MenuService {
    List<Menu> getAllMenus();
    Optional<Menu> getMenuById(String id);
    Menu createMenu(Menu menu);
    Optional<Menu> updateMenu(String id, Menu menu);
    void deleteMenu(String id);
    Optional<Menu> reduceQuantity(String id, BigDecimal quantity);
}
