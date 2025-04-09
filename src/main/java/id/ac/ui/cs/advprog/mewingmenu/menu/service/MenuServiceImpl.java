package id.ac.ui.cs.advprog.mewingmenu.menu.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public Optional<Menu> getMenuById(UUID id) {
        return menuRepository.findById(id);
    }

    @Override
    public Menu createMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    public Optional<Menu> updateMenu(UUID id, Menu menu) {
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
    public void deleteMenu(UUID id) {
        menuRepository.deleteById(id);
    }
}
