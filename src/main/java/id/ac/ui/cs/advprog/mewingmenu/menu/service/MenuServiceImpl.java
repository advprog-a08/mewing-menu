package id.ac.ui.cs.advprog.mewingmenu.menu.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import id.ac.ui.cs.advprog.mewingmenu.menu.repository.MenuRepository;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    @Override
    public Optional<Menu> getMenuById(String id) {
        return menuRepository.findById(id);
    }

    @Override
    public Menu createMenu(Menu menu) {
        Optional<Menu> existingMenu = menuRepository.findByName(menu.getName());
        if (existingMenu.isPresent()) {
            throw new IllegalStateException("Menu with name " + menu.getName() + " already exists");
        }
        return menuRepository.save(menu);
    }

    @Override
    public Optional<Menu> updateMenu(String id, Menu menu) {
        return menuRepository.findById(id)
                .map(existingMenu -> {
                    existingMenu.setName(menu.getName());
                    existingMenu.setDescription(menu.getDescription());
                    existingMenu.setPrice(menu.getPrice());
                    existingMenu.setCategory(menu.getCategory());
                    return menuRepository.save(existingMenu);
                });
    }

    @Override
    public Optional<Menu> reduceQuantity(String id, BigDecimal quantity) {
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        return menuRepository.findById(id)
                .map(existingMenu -> {
                    existingMenu.setQuantity(BigDecimal.valueOf(existingMenu.getQuantity().intValue() - quantity.intValue()));
                    return menuRepository.save(existingMenu);
                });
    }

    @Override
    public void deleteMenu(String id) {
        menuRepository.deleteById(id);
    }
}
