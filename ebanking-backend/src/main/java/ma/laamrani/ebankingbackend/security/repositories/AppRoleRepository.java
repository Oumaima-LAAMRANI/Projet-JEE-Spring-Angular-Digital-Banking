package ma.laamrani.ebankingbackend.security.repositories;

import ma.laamrani.ebankingbackend.security.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole,String> {

    AppRole findByRoleName(String rolename);


}
