package com.klymb.quiz_service.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class CustomAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final JwtGrantedAuthoritiesConverter scopesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = new ArrayList<>(scopesConverter.convert(jwt));
        List<String> roles = jwt.getClaim("roles");

        if (roles != null) {
            roles.forEach(role -> {
                if (!role.startsWith("ROLE_")) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                } else {
                    authorities.add(new SimpleGrantedAuthority(role));
                }
            });
        }
        System.out.println("Final roles"+authorities);
        return authorities;
    }
}
