package id.ac.ui.cs.advprog.mewingmenu.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import id.ac.ui.cs.advprog.mewingmenu.menu.model.MenuCategory;
import id.ac.ui.cs.advprog.mewingmenu.menu.repository.MenuCategoryRepository;
import id.ac.ui.cs.advprog.mewingmenu.menu.service.MenuCategoryImpl;

@ExtendWith(MockitoExtension.class)
public class MenuCategoryServiceTest {

    @Mock
    private MenuCategoryRepository menuCategoryRepository;

    @InjectMocks
    private MenuCategoryImpl menuCategoryService;

    private MenuCategory menuCategory;
    private String categoryId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        menuCategory = new MenuCategory();
        menuCategory.setId(categoryId);
        menuCategory.setName("Category 1");
        menuCategory.setDescription("Description 1");
    }

    @Test
    void testGetAllMenuCategories() {
        when(menuCategoryRepository.findAll()).thenReturn(List.of(menuCategory));

        List<MenuCategory> categories = menuCategoryService.getAllMenuCategories();

        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals(menuCategory, categories.get(0));
        verify(menuCategoryRepository, times(1)).findAll();
    }

    @Test
    void testGetMenuCategoryById() {
        when(menuCategoryRepository.findById(categoryId)).thenReturn(Optional.of(menuCategory));

        Optional<MenuCategory> result = menuCategoryService.getMenuCategoryById(categoryId);

        assertTrue(result.isPresent());
        assertEquals(menuCategory, result.get());
        verify(menuCategoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void testCreateMenuCategory() {
        when(menuCategoryRepository.save(menuCategory)).thenReturn(menuCategory);

        MenuCategory result = menuCategoryService.createMenuCategory(menuCategory);

        assertNotNull(result);
        assertEquals(menuCategory, result);
        verify(menuCategoryRepository, times(1)).save(menuCategory);
    }

    @Test
    void testCreateDuplicateMenuCategory() {
        when(menuCategoryRepository.findByName(menuCategory.getName())).thenReturn(Optional.of(menuCategory));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            menuCategoryService.createMenuCategory(menuCategory);
        });

        assertEquals("Menu category with name " + menuCategory.getName() + " already exists", exception.getMessage());
        verify(menuCategoryRepository, times(1)).findByName(menuCategory.getName());
    }

    @Test
    void testUpdateMenuCategory() {
        MenuCategory updatedCategory = new MenuCategory();
        updatedCategory.setName("Updated Name");
        updatedCategory.setDescription("Updated Description");

        when(menuCategoryRepository.findById(categoryId)).thenReturn(Optional.of(menuCategory));
        when(menuCategoryRepository.save(menuCategory)).thenReturn(menuCategory);

        Optional<MenuCategory> result = menuCategoryService.updateMenuCategory(categoryId, updatedCategory);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
        assertEquals("Updated Description", result.get().getDescription());
        verify(menuCategoryRepository, times(1)).findById(categoryId);
        verify(menuCategoryRepository, times(1)).save(menuCategory);
    }

    @Test
    void testDeleteMenuCategoryWithoutMenus() {
        menuCategory.setMenus(List.of());
        when(menuCategoryRepository.findById(categoryId)).thenReturn(Optional.of(menuCategory));

        assertDoesNotThrow(() -> menuCategoryService.deleteMenuCategory(categoryId));
        verify(menuCategoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    void testDeleteMenuCategoryWithMenus() {
        Menu menu = new Menu();
        menuCategory.setMenus(List.of(menu));
        when(menuCategoryRepository.findById(categoryId)).thenReturn(Optional.of(menuCategory));

        Exception exception = assertThrows(IllegalStateException.class, () -> menuCategoryService.deleteMenuCategory(categoryId));
        assertEquals("Cannot delete category with existing menus", exception.getMessage());
        verify(menuCategoryRepository, never()).deleteById(categoryId);
    }
}
