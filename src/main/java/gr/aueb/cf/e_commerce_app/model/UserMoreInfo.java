package gr.aueb.cf.e_commerce_app.model;

import gr.aueb.cf.e_commerce_app.core.enums.Gender;
import gr.aueb.cf.e_commerce_app.model.static_data.Region;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_more_information")
public class UserMoreInfo extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_photo_url")
    private String profilePhotoUrl;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    private String address;

    @Column(name = "address_number")
    private Integer addressNumber;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
