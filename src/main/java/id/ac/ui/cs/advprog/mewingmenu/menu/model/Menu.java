package id.ac.ui.cs.advprog.mewingmenu.menu.model;

import java.math.BigDecimal;
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
public class Menu {

    private UUID id;

    private String name;

    private String description;

    private String imageUrl;

    private BigDecimal price;

    private MenuCategory category;
}