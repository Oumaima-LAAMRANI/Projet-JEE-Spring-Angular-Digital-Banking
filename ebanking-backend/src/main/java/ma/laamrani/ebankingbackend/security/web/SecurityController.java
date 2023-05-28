package ma.laamrani.ebankingbackend.security.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import ma.laamrani.ebankingbackend.security.entities.AppUser;
import ma.laamrani.ebankingbackend.security.repositories.AppUserRepository;
import ma.laamrani.ebankingbackend.security.services.SecurityService;
import ma.laamrani.ebankingbackend.security.entities.AppRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController("/security")
@CrossOrigin("*")
@AllArgsConstructor
public class SecurityController {

    private final SecurityService securityService;
    private final AppUserRepository appUserRepository;

    @GetMapping("/users")
    @CrossOrigin("*")
    public List<AppUser> getUsers() {
        return appUserRepository.findAll();
    }


    @GetMapping("/refreshToken")
    public void getRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("authorizationHeader = " + authorizationHeader);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String jwtRefreshToken = authorizationHeader.substring(7);
                System.out.println("jwtRefreshToken = " + jwtRefreshToken);
                Algorithm hmac = Algorithm.HMAC256("secret");
                JWTVerifier verifier = JWT.require(hmac).build();
                DecodedJWT decodedJWT = verifier.verify(jwtRefreshToken);
                String username = decodedJWT.getSubject();
                AppUser appUser = securityService.loadUserByUserName(username);

                String jwtAccessToken = JWT
                        .create()
                        .withSubject(appUser.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 1 * 60 * 1000))
                        .withIssuer("Auth Service at " + request.getRequestURL())
                        .withClaim("roles", appUser
                                .getAppRoles()
                                .stream()
                                .map(AppRole::getRoleName)
                                .collect(Collectors.toList()))
                        .sign(hmac);
                System.out.println("jwtAccessToken = " + jwtAccessToken);
                Map<String, String> body = new HashMap<>(2);
                body.put("accessToken", jwtAccessToken);
                body.put("refreshToken", jwtRefreshToken);
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), body);
            } catch (Exception e) {
                response.setHeader("jwt-error", e.getMessage());
                response.sendError(HttpStatus.FORBIDDEN.value());
            }
        } else {
            throw new RuntimeException("Bad request");
        }

    }
    //@Bean
    CommandLineRunner runner(
            SecurityService securityService
    ) {
        return args -> {

            securityService.saveNewUser("Oumaima", "1234", "1234");
            securityService.saveNewUser("Hiba", "1234", "1234");
            securityService.saveNewUser("Amine", "1234", "1234");

            securityService.saveNewRole("USER", "");
            securityService.saveNewRole("ADMIN", "");

            securityService.addRoleToUser("Oumaima", "ADMIN");
            securityService.addRoleToUser("Oumaima", "USER");
            securityService.addRoleToUser("Hiba", "USER");
            securityService.addRoleToUser("Amine", "USER");

        };
    }
}
