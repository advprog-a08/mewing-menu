package id.ac.ui.cs.advprog.mewingmenu.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import id.ac.ui.cs.advprog.mewingmenu.menu.model.MenuCategory;
import id.ac.ui.cs.advprog.mewingmenu.menu.repository.MenuRepository;
import id.ac.ui.cs.advprog.mewingmenu.menu.service.MenuServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuServiceImpl menuService;

    private Menu mockMenu;
    private String mockId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        MenuCategory category = new MenuCategory();
        category.setId(UUID.randomUUID().toString());
        category.setName("Food");

        mockMenu = new Menu();
        mockMenu.setId(mockId);
        mockMenu.setName("Nasi Goreng");
        mockMenu.setDescription("Fried rice");
        mockMenu.setPrice(new BigDecimal("25000"));
        mockMenu.setCategory(category);
    }

    @Test
    @DisplayName("It should return all menus")
    void testGetAllMenus() {
        List<Menu> menus = Arrays.asList(mockMenu);
        when(menuRepository.findAll()).thenReturn(menus);

        List<Menu> result = menuService.getAllMenus();
        assertEquals(1, result.size());
        assertEquals("Nasi Goreng", result.get(0).getName());
    }

    @Test
    @DisplayName("It should return a menu by ID")
    void testGetMenuById() {
        when(menuRepository.findById(mockId)).thenReturn(Optional.of(mockMenu));

        Optional<Menu> result = menuService.getMenuById(mockId);
        assertTrue(result.isPresent());
        assertEquals("Nasi Goreng", result.get().getName());
    }

    @Test
    @DisplayName("It should create a menu")
    void testCreateMenu() {
        when(menuRepository.save(mockMenu)).thenReturn(mockMenu);

        Menu result = menuService.createMenu(mockMenu);
        assertNotNull(result);
        assertEquals("Nasi Goreng", result.getName());
    }

    @Test
    @DisplayName("It should not create a duplicate menu")
    void testCreateDuplicateMenu() {
        when(menuRepository.findByName(mockMenu.getName())).thenReturn(Optional.of(mockMenu));

        try {
            menuService.createMenu(mockMenu);
        } catch (IllegalStateException e) {
            assertEquals("Menu with name Nasi Goreng already exists", e.getMessage());
        }
    }

    @Test
    @DisplayName("It should update an existing menu")
    void testUpdateMenu() {
        Menu updatedMenu = new Menu();
        updatedMenu.setName("Nasi Uduk");
        updatedMenu.setDescription("Rice with coconut milk");
        updatedMenu.setPrice(new BigDecimal("27000"));
        updatedMenu.setCategory(mockMenu.getCategory());

        when(menuRepository.findById(mockId)).thenReturn(Optional.of(mockMenu));
        when(menuRepository.save(any(Menu.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Menu> result = menuService.updateMenu(mockId, updatedMenu);
        assertTrue(result.isPresent());
        assertEquals("Nasi Uduk", result.get().getName());
    }

    @Test
    @DisplayName("It should delete a menu by ID")
    void testDeleteMenu() {
        String menuId = UUID.randomUUID().toString();
        doNothing().when(menuRepository).deleteById(menuId);

        menuService.deleteMenu(menuId);
        verify(menuRepository, times(1)).deleteById(menuId);
    }

    @Test
    @DisplayName("It should return empty when menu is not found")
    void testGetMenuByIdNotFound() {
        when(menuRepository.findById(mockId)).thenReturn(Optional.empty());

        Optional<Menu> result = menuService.getMenuById(mockId);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("It should return empty when menu is not found for update")
    void testUpdateMenuNotFound() {
        Menu updatedMenu = new Menu();
        updatedMenu.setName("Nasi Uduk");
        updatedMenu.setDescription("Rice with coconut milk");
        updatedMenu.setPrice(new BigDecimal("27000"));
        updatedMenu.setCategory(mockMenu.getCategory());

        when(menuRepository.findById(mockId)).thenReturn(Optional.empty());

        Optional<Menu> result = menuService.updateMenu(mockId, updatedMenu);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("It should return error when menu field is invalid")
    void testCreateMenuWithInvalidField() {
        Menu invalidMenu = new Menu();
        invalidMenu.setName(""); // Invalid name
        invalidMenu.setDescription("Fried rice");
        invalidMenu.setPrice(new BigDecimal("25000"));
        invalidMenu.setCategory(mockMenu.getCategory());

        when(menuRepository.save(invalidMenu)).thenThrow(new IllegalArgumentException("Invalid menu name"));

        try {
            menuService.createMenu(invalidMenu);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid menu name", e.getMessage());
        }
    }

    @Test
    @DisplayName("It should reduce menu quantity successfully")
    void testReduceQuantity() {
        String menuId = UUID.randomUUID().toString();
        Menu existingMenu = new Menu();
        existingMenu.setId(menuId);
        existingMenu.setName("Test Menu");
        existingMenu.setQuantity(BigDecimal.valueOf(10));

        BigDecimal quantityToReduce = BigDecimal.valueOf(3);

        when(menuRepository.findById(menuId)).thenReturn(Optional.of(existingMenu));
        when(menuRepository.save(any(Menu.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Menu> result = menuService.reduceQuantity(menuId, quantityToReduce);

        assertTrue(result.isPresent());
        assertEquals(BigDecimal.valueOf(7), result.get().getQuantity());
        verify(menuRepository).findById(menuId);
        verify(menuRepository).save(existingMenu);
    }

    @Test
    @DisplayName("It should return empty when menu is not found for quantity reduction")
    void testReduceQuantityMenuNotFound() {
        String menuId = UUID.randomUUID().toString();
        BigDecimal quantityToReduce = BigDecimal.valueOf(3);

        when(menuRepository.findById(menuId)).thenReturn(Optional.empty());

        Optional<Menu> result = menuService.reduceQuantity(menuId, quantityToReduce);

        assertTrue(result.isEmpty());
        verify(menuRepository).findById(menuId);
        verify(menuRepository, times(0)).save(any(Menu.class));
    }

    @Test
    @DisplayName("It should throw IllegalArgumentException when quantity is zero")
    void testReduceQuantityWithZero() {
        BigDecimal zeroQuantity = BigDecimal.ZERO;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            menuService.reduceQuantity(mockId, zeroQuantity);
        });

        assertEquals("Quantity must be greater than zero", exception.getMessage());
        verify(menuRepository, never()).findById(any());
        verify(menuRepository, never()).save(any());
    }

    @Test
    @DisplayName("It should throw IllegalArgumentException when quantity is negative")
    void testReduceQuantityWithNegative() {
        BigDecimal negativeQuantity = new BigDecimal("-5");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            menuService.reduceQuantity(mockId, negativeQuantity);
        });

        assertEquals("Quantity must be greater than zero", exception.getMessage());

        verify(menuRepository, never()).findById(any());
        verify(menuRepository, never()).save(any());
    }
}
