package id.ac.ui.cs.advprog.mewingmenu.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import id.ac.ui.cs.advprog.mewingmenu.menu.controller.MenuController;
import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import id.ac.ui.cs.advprog.mewingmenu.menu.model.MenuCategory;
import id.ac.ui.cs.advprog.mewingmenu.menu.service.MenuService;
import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class MenuControllerTest {
    @Mock
    private MenuService menuService;

    @InjectMocks
    private MenuController menuController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Menu testMenu;
    private MenuCategory testCategory;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(menuController).build();
        objectMapper = new ObjectMapper();
        
        testCategory = new MenuCategory();
        testCategory.setId("cat-1");
        testCategory.setName("Main Course");
        testCategory.setDescription("Main course dishes");
        
        testMenu = new Menu();
        testMenu.setId("1");
        testMenu.setName("Pasta Carbonara");
        testMenu.setDescription("Classic Italian pasta with eggs, cheese, and bacon");
        testMenu.setPrice(new BigDecimal("15.99"));
        testMenu.setCategory(testCategory);
    }

    @Test
    void getAllMenus_ShouldReturnAllMenus_WhenMenusExist() throws Exception {
        Menu menu1 = new Menu();
        menu1.setId("1");
        menu1.setName("Pasta Carbonara");
        menu1.setPrice(new BigDecimal("15.99"));
        
        Menu menu2 = new Menu();
        menu2.setId("2");
        menu2.setName("Caesar Salad");
        menu2.setPrice(new BigDecimal("12.50"));
        
        List<Menu> menus = Arrays.asList(menu1, menu2);
        when(menuService.getAllMenus()).thenReturn(menus);

        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Successfully fetched all menus."))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value("1"))
                .andExpect(jsonPath("$.data[0].name").value("Pasta Carbonara"))
                .andExpect(jsonPath("$.data[0].price").value(15.99))
                .andExpect(jsonPath("$.data[1].id").value("2"))
                .andExpect(jsonPath("$.data[1].name").value("Caesar Salad"))
                .andExpect(jsonPath("$.data[1].price").value(12.50));

        verify(menuService, times(1)).getAllMenus();
    }

    @Test
    void getAllMenus_ShouldReturnEmptyList_WhenNoMenusExist() throws Exception {
        when(menuService.getAllMenus()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Successfully fetched all menus."))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));

        verify(menuService, times(1)).getAllMenus();
    }

    @Test
    void getMenuById_ShouldReturnNotFound_WhenMenuDoesNotExist() throws Exception {
        String menuId = "999";
        when(menuService.getMenuById(menuId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/menus/{id}", menuId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Menu not found with ID: " + menuId));

        verify(menuService, times(1)).getMenuById(menuId);
    }

    @Test
    void createMenu_ShouldHandleMenuWithNullValues() throws Exception {
        Menu newMenu = new Menu();
        newMenu.setName("Simple Menu");

        Menu createdMenu = new Menu();
        createdMenu.setId("4");
        createdMenu.setName("Simple Menu");

        when(menuService.createMenu(any(Menu.class))).thenReturn(createdMenu);

        mockMvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newMenu)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Menu created successfully."))
                .andExpect(jsonPath("$.data.id").value("4"))
                .andExpect(jsonPath("$.data.name").value("Simple Menu"));

        verify(menuService, times(1)).createMenu(any(Menu.class));
    }


    @Test
    void updateMenu_ShouldReturnNotFound_WhenMenuDoesNotExist() throws Exception {
        String menuId = "999";
        Menu updateRequest = new Menu();
        updateRequest.setName("Updated Menu");

        when(menuService.updateMenu(eq(menuId), any(Menu.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/menus/{id}", menuId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Menu not found for update with ID: " + menuId));

        verify(menuService, times(1)).updateMenu(eq(menuId), any(Menu.class));
    }


    @Test
    void deleteMenu_ShouldReturnSuccess_WhenMenuExists() throws Exception {
        String menuId = "1";
        when(menuService.getMenuById(menuId)).thenReturn(Optional.of(testMenu));
        doNothing().when(menuService).deleteMenu(menuId);

        mockMvc.perform(delete("/api/menus/{id}", menuId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Menu deleted successfully."))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(menuService, times(1)).getMenuById(menuId);
        verify(menuService, times(1)).deleteMenu(menuId);
    }

    @Test
    void deleteMenu_ShouldReturnNotFound_WhenMenuDoesNotExist() throws Exception {
        String menuId = "999";
        when(menuService.getMenuById(menuId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/menus/{id}", menuId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Menu not found for deletion with ID: " + menuId));

        verify(menuService, times(1)).getMenuById(menuId);
        verify(menuService, never()).deleteMenu(menuId);
    }

    @Test
    void createMenu_ShouldHandleEmptyRequestBody() throws Exception {
        Menu createdMenu = new Menu();
        createdMenu.setId("5");
        when(menuService.createMenu(any(Menu.class))).thenReturn(createdMenu);

        mockMvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Menu created successfully."));

        verify(menuService, times(1)).createMenu(any(Menu.class));
    }

    @Test
    void updateMenu_ShouldHandleEmptyRequestBody() throws Exception {
        String menuId = "1";
        when(menuService.updateMenu(eq(menuId), any(Menu.class)))
                .thenReturn(Optional.of(testMenu));

        mockMvc.perform(put("/api/menus/{id}", menuId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Menu updated successfully."));

        verify(menuService, times(1)).updateMenu(eq(menuId), any(Menu.class));
    }

    @Test
    void getAllEndpoints_ShouldHaveCorrectMapping() throws Exception {
        when(menuService.getAllMenus()).thenReturn(Arrays.asList());
        when(menuService.getMenuById("1")).thenReturn(Optional.empty());
        when(menuService.createMenu(any(Menu.class))).thenReturn(testMenu);
        when(menuService.updateMenu(eq("1"), any(Menu.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/menus")).andExpect(status().isOk());
        
        mockMvc.perform(get("/api/menus/1")).andExpect(status().isNotFound());
        
        mockMvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated());
        
        mockMvc.perform(put("/api/menus/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound());
        
        mockMvc.perform(delete("/api/menus/1")).andExpect(status().isNotFound());
    }
}