package com.github.moviereservationbe.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final UserDetailsService userDetailsService;
    @Value("${JWT_SECRET_KEY}")
    private String secretKey;
    private String key;

    @PostConstruct
    public void setUp(){
        key= Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
    private long tokenValidMillisecond= 1000L * 60 * 60;

    public String resolveToken(HttpServletRequest request){
        return request.getHeader("Token");
    }

    public String createToken(String myId, List<String> role){
        Claims claims= Jwts.claims().setSubject(myId);
        claims.put("role", role);
        Date now= new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+ tokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }
    public boolean validateToken(String jwtToken){
        try{
            Claims claims= Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken).getBody();
            Date now= new Date();
            return !claims.getExpiration().before(now);
        } catch(Exception e){
            return false;
        }
    }

    public Authentication getAuthentication(String jwtToken){
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserMyId(jwtToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserMyId(String jwtToken) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken).getBody().getSubject();
    }

    //logout
    private Set<String> tokenBlackList = new HashSet<>();
    public void addToBlackList(String currentToken) {
        System.out.println("Token added to blacklist: " + currentToken);
        tokenBlackList.add(currentToken);
    }
    public boolean isTokenBlackListed(String jwtToken){
        return tokenBlackList.contains(jwtToken);
    }

}
