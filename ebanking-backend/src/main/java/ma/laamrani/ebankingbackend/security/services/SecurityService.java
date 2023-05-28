package ma.laamrani.ebankingbackend.security.services;

import ma.laamrani.ebankingbackend.security.entities.AppRole;
import ma.laamrani.ebankingbackend.security.entities.AppUser;

public interface SecurityService {
AppUser saveNewUser(String userName, String password, String rePssword);
AppRole saveNewRole(String roleName, String description);
void addRoleToUser(String userName,String roleName);
AppUser loadUserByUserName(String userName);


}
