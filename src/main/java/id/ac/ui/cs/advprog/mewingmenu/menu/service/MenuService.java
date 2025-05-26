package id.ac.ui.cs.advprog.mewingmenu.menu.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import id.ac.ui.cs.advprog.mewingmenu.utils.PaginatedData;

public interface MenuService {
    List<Menu> getAllMenus();
    PaginatedData<Menu> getAllMenus(int page, int size);
    Optional<Menu> getMenuById(String id);
    Menu createMenu(Menu menu);
    Optional<Menu> updateMenu(String id, Menu menu);
    void deleteMenu(String id);
    Optional<Menu> reduceQuantity(String id, BigDecimal quantity);
}
