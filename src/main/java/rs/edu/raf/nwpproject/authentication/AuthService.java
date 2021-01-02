package rs.edu.raf.nwpproject.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.edu.raf.nwpproject.Util.Util;
import rs.edu.raf.nwpproject.models.User;
import rs.edu.raf.nwpproject.services.UserService;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthService {

    static Key key = MacProvider.generateKey();

    @Autowired
    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public String generateJWT(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("password", user.getPassword());
        claims.put("userType", user.getUserType());
        claims.put("user", user);

        return Jwts.builder()
                .setSubject(user.getUsername())
//                .setExpiration(new Date(new Date().getTime() + 1000000*60*60L))
//                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, key)
                .claim("userType", user.getUserType())
                .compact();
    }

    public boolean isAuthorized(String token) {

        if(!Util.isEmpty(token) && token.contains("Bearer ")) {

            try{
                String jwt = token.substring(token.indexOf("Bearer ") + 7);

                Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt);

                if(userService.findByUsername(claims.getBody().getSubject()) != null) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }

        }

        return false;
    }

    public boolean isAdmin(String token) {

        if(!Util.isEmpty(token) && token.contains("Bearer ")) {
            try{
                String jwt = token.substring(token.indexOf("Bearer ") + 7);

                Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt);


                if(claims.getBody().get("userType").toString().equals("2")) {
                    return true;
                }

            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    public boolean isUser(String token) {

        if(!Util.isEmpty(token) && token.contains("Bearer ")) {
            try{
                String jwt = token.substring(token.indexOf("Bearer ") + 7);

                Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt);

                if(claims.getBody().get("userType") == "1") {
                    return true;
                }

            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    public Long getUserIdFromAuth(String token) {

        if(!Util.isEmpty(token) && token.contains("Bearer ")) {
            String jwt = token.substring(token.indexOf("Bearer ") + 7);

            Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt);

            User tmpUser = userService.findByUsername(claims.getBody().getSubject());
            if(tmpUser != null) {
                return tmpUser.getId();
            }
        }

        return Long.valueOf(-1);
    }
}
