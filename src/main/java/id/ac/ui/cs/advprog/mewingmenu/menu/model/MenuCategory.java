package id.ac.ui.cs.advprog.mewingmenu.menu.model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MenuCategory {

    private UUID id;

    private String name;


    private String description;


    private List<Menu> menus;
}