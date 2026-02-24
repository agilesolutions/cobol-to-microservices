package com.agilesolutions.account.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WithMockJwtSecurityContextFactory implements WithSecurityContextFactory<WithMockJwt> {

    @Override
    public SecurityContext createSecurityContext(WithMockJwt annotation) {
        // 1. Build the JWT claims
        Jwt jwt = Jwt.withTokenValue("mock-token")
                .header("alg", "none")
                .subject(annotation.subject())
                .claim("scope", String.join(" ", annotation.scopes()))
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        // 2. Map roles to authorities (prefixing with ROLE_ is standard)
        List<SimpleGrantedAuthority> authorities = Arrays.stream(annotation.roles())
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        // Add scopes to authorities if needed
        Arrays.stream(annotation.scopes())
                .map(scope -> new SimpleGrantedAuthority("SCOPE_" + scope))
                .forEach(authorities::add);

        // 3. Create the Authentication object
        JwtAuthenticationToken auth = new JwtAuthenticationToken(jwt, authorities);

        // 4. Return the populated context
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        return context;
    }
}