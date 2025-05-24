package id.ac.ui.cs.advprog.mewingmenu.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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

import id.ac.ui.cs.advprog.mewingmenu.menu.controller.MenuCategoryController;
import id.ac.ui.cs.advprog.mewingmenu.menu.model.MenuCategory;
import id.ac.ui.cs.advprog.mewingmenu.menu.service.MenuCategoryService;

@ExtendWith(MockitoExtension.class)
class MenuCategoryControllerTest {

    @Mock
    private MenuCategoryService menuCategoryService;

    @InjectMocks
    private MenuCategoryController menuCategoryController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private MenuCategory testCategory;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(menuCategoryController).build();
        objectMapper = new ObjectMapper();
        
        testCategory = new MenuCategory();
        testCategory.setId("1");
        testCategory.setName("Appetizers");
        testCategory.setDescription("Delicious appetizers to start your meal");
    }

    @Test
    void getAllCategories_ShouldReturnAllCategories_WhenCategoriesExist() throws Exception {
        MenuCategory category1 = new MenuCategory();
        category1.setId("1");
        category1.setName("Appetizers");
        
        MenuCategory category2 = new MenuCategory();
        category2.setId("2");
        category2.setName("Main Course");
        
        List<MenuCategory> categories = Arrays.asList(category1, category2);
        when(menuCategoryService.getAllMenuCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/menu-categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Fetched all menu categories"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value("1"))
                .andExpect(jsonPath("$.data[0].name").value("Appetizers"))
                .andExpect(jsonPath("$.data[1].id").value("2"))
                .andExpect(jsonPath("$.data[1].name").value("Main Course"));

        verify(menuCategoryService, times(1)).getAllMenuCategories();
    }

    @Test
    void getAllCategories_ShouldReturnEmptyList_WhenNoCategoriesExist() throws Exception {
        when(menuCategoryService.getAllMenuCategories()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/menu-categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Fetched all menu categories"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));

        verify(menuCategoryService, times(1)).getAllMenuCategories();
    }

    @Test
    void getCategoryById_ShouldReturnCategory_WhenCategoryExists() throws Exception {
        String categoryId = "1";
        when(menuCategoryService.getMenuCategoryById(categoryId)).thenReturn(Optional.of(testCategory));

        mockMvc.perform(get("/api/menu-categories/{id}", categoryId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Fetched menu category"))
                .andExpect(jsonPath("$.data.id").value(testCategory.getId()))
                .andExpect(jsonPath("$.data.name").value(testCategory.getName()))
                .andExpect(jsonPath("$.data.description").value(testCategory.getDescription()));

        verify(menuCategoryService, times(1)).getMenuCategoryById(categoryId);
    }

    @Test
    void getCategoryById_ShouldReturnNotFound_WhenCategoryDoesNotExist() throws Exception {
        String categoryId = "999";
        when(menuCategoryService.getMenuCategoryById(categoryId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/menu-categories/{id}", categoryId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Menu category not found"));

        verify(menuCategoryService, times(1)).getMenuCategoryById(categoryId);
    }

    @Test
    void createCategory_ShouldReturnCreatedCategory_WhenValidCategoryProvided() throws Exception {
        MenuCategory newCategory = new MenuCategory();
        newCategory.setName("Desserts");
        newCategory.setDescription("Sweet treats to end your meal");

        MenuCategory createdCategory = new MenuCategory();
        createdCategory.setId("3");
        createdCategory.setName("Desserts");
        createdCategory.setDescription("Sweet treats to end your meal");

        when(menuCategoryService.createMenuCategory(any(MenuCategory.class))).thenReturn(createdCategory);

        mockMvc.perform(post("/api/menu-categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Menu category created successfully"))
                .andExpect(jsonPath("$.data.id").value("3"))
                .andExpect(jsonPath("$.data.name").value("Desserts"))
                .andExpect(jsonPath("$.data.description").value("Sweet treats to end your meal"));

        verify(menuCategoryService, times(1)).createMenuCategory(any(MenuCategory.class));
    }

    @Test
    void updateCategory_ShouldReturnUpdatedCategory_WhenCategoryExists() throws Exception {
        String categoryId = "1";
        MenuCategory updateRequest = new MenuCategory();
        updateRequest.setName("Updated Appetizers");
        updateRequest.setDescription("Updated description");

        MenuCategory updatedCategory = new MenuCategory();
        updatedCategory.setId(categoryId);
        updatedCategory.setName("Updated Appetizers");
        updatedCategory.setDescription("Updated description");

        when(menuCategoryService.updateMenuCategory(eq(categoryId), any(MenuCategory.class)))
                .thenReturn(Optional.of(updatedCategory));

        mockMvc.perform(put("/api/menu-categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Menu category updated successfully"))
                .andExpect(jsonPath("$.data.id").value(categoryId))
                .andExpect(jsonPath("$.data.name").value("Updated Appetizers"))
                .andExpect(jsonPath("$.data.description").value("Updated description"));

        verify(menuCategoryService, times(1)).updateMenuCategory(eq(categoryId), any(MenuCategory.class));
    }

    @Test
    void updateCategory_ShouldReturnNotFound_WhenCategoryDoesNotExist() throws Exception {
        String categoryId = "999";
        MenuCategory updateRequest = new MenuCategory();
        updateRequest.setName("Updated Category");

        when(menuCategoryService.updateMenuCategory(eq(categoryId), any(MenuCategory.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/menu-categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Menu category not found"));

        verify(menuCategoryService, times(1)).updateMenuCategory(eq(categoryId), any(MenuCategory.class));
    }

    @Test
    void deleteCategory_ShouldReturnSuccess_WhenCategoryDeletedSuccessfully() throws Exception {
        String categoryId = "1";
        doNothing().when(menuCategoryService).deleteMenuCategory(categoryId);

        mockMvc.perform(delete("/api/menu-categories/{id}", categoryId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Menu category deleted successfully"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(menuCategoryService, times(1)).deleteMenuCategory(categoryId);
    }

    @Test
    void deleteCategory_ShouldReturnBadRequest_WhenIllegalStateExceptionThrown() throws Exception {
        String categoryId = "1";
        String errorMessage = "Cannot delete category with existing menu items";
        doThrow(new IllegalStateException(errorMessage))
                .when(menuCategoryService).deleteMenuCategory(categoryId);

        mockMvc.perform(delete("/api/menu-categories/{id}", categoryId))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(errorMessage));

        verify(menuCategoryService, times(1)).deleteMenuCategory(categoryId);
    }

    @Test
    void deleteCategory_ShouldReturnNotFound_WhenOtherExceptionThrown() throws Exception {
        String categoryId = "999";
        doThrow(new RuntimeException("Some other error"))
                .when(menuCategoryService).deleteMenuCategory(categoryId);

        mockMvc.perform(delete("/api/menu-categories/{id}", categoryId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Menu category not found"));

        verify(menuCategoryService, times(1)).deleteMenuCategory(categoryId);
    }

    @Test
    void createCategory_ShouldHandleEmptyRequestBody() throws Exception {
        mockMvc.perform(post("/api/menu-categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated());

        verify(menuCategoryService, times(1)).createMenuCategory(any(MenuCategory.class));
    }

    @Test
    void updateCategory_ShouldHandleEmptyRequestBody() throws Exception {
        String categoryId = "1";
        when(menuCategoryService.updateMenuCategory(eq(categoryId), any(MenuCategory.class)))
                .thenReturn(Optional.of(testCategory));

        mockMvc.perform(put("/api/menu-categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());

        verify(menuCategoryService, times(1)).updateMenuCategory(eq(categoryId), any(MenuCategory.class));
    }

    @Test
    void getAllEndpoints_ShouldHaveCorrectMapping() throws Exception {
        mockMvc.perform(get("/api/menu-categories")).andExpect(status().isOk());
        mockMvc.perform(get("/api/menu-categories/1")).andExpect(status().isNotFound());
        mockMvc.perform(post("/api/menu-categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated());
        mockMvc.perform(put("/api/menu-categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound()); 
        mockMvc.perform(delete("/api/menu-categories/1")).andExpect(status().isOk());
    }
}