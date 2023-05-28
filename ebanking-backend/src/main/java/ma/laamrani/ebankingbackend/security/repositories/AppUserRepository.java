package ma.laamrani.ebankingbackend.security.repositories;

import ma.laamrani.ebankingbackend.security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository  extends JpaRepository<AppUser,String> {

    AppUser findByUsername(String username);


}
