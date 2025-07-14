package gr.aueb.cf.e_commerce_app.model.static_data;

import gr.aueb.cf.e_commerce_app.model.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Getter(value = AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "category")
    private Set<Product> products = new HashSet<>();

    public Set<Product> getAllProducts() {
        if (products == null) products = new HashSet<>();
        return Collections.unmodifiableSet(products);
    }
}
