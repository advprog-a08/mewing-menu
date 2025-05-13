package id.ac.ui.cs.advprog.mewingmenu.rating.model;
import java.time.LocalDateTime;

import id.ac.ui.cs.advprog.mewingmenu.menu.model.Menu;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Rating {
    @Id
    @GeneratedValue(generator = "uuid4")
    @GenericGenerator(name = "uuid4",  strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    @NotNull(message = "Menu can't be empty")
    @ManyToOne(optional = false)
    @JoinColumn(name = "menu_id", referencedColumnName = "id", nullable = false)
    private Menu menu;

    @NotBlank(message = "Session Id can't be empty")
    @Column(columnDefinition = "VARCHAR(36)")
    private String sessionId;

    @NotNull(message = "Rating can't be empty")
    @Min(value=1, message= "Rating must be at least 1")
    @Max(value=5, message = "Rating must be 5 or lower")
    private Integer rating;

    @Size(max=255, message = "Review can't exceed 255 characters")
    @Column(nullable = false, length = 255)
    private String review;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
