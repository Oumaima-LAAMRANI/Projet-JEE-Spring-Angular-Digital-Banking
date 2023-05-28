package ma.laamrani.ebankingbackend.security.entities;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class AppRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long roleId;
    @Column(unique = true)
    private String roleName;
    private String description;
}
