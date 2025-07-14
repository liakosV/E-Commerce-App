package gr.aueb.cf.e_commerce_app.model.static_data;

import gr.aueb.cf.e_commerce_app.model.User;
import gr.aueb.cf.e_commerce_app.model.UserMoreInfo;
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
@Table(name = "regions")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public Region(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Getter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "region")
    private Set<UserMoreInfo> userMoreInfos = new HashSet<>();

    public Set<UserMoreInfo> getAllUserMoreInfos() {
        if (userMoreInfos == null) userMoreInfos = new HashSet<>();
        return Collections.unmodifiableSet(userMoreInfos);
    }

}
