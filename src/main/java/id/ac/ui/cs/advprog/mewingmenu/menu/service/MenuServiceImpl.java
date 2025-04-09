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

    }

    @Override
    public Optional<Menu> getMenuById(UUID id) {

    }

    @Override
    public Menu createMenu(Menu menu) {

    }
    @Override
    public Optional<Menu> updateMenu(UUID id, Menu menu) {

    }

    @Override
    public void deleteMenu(UUID id) {

    }
}
