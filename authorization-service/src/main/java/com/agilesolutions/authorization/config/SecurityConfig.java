package com.agilesolutions.authorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Configuration
public class SecurityConfig {

    @Bean
    public RegisteredClient registeredClient() {
        return RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("gateway-client-id")
                .clientSecret("gateway-client-secret")
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scopes(s-> s.addAll(List.of("read", "write")))
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .build())
                .build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {

            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {

                String clientId = context.getRegisteredClient().getClientId();

                // 👇 Assign roles (hardcoded or from DB)
                Set<String> roles = new HashSet<>();

                if ("gateway-client-id".equals(clientId)) {
                    roles.add("ROLE_ADMIN");
                    roles.add("ROLE_USER");
                } else {
                    roles.add("ROLE_USER");
                }

                // 👇 Add roles claim
                context.getClaims().claim("roles", roles);

                // 👇 Add scope claim (already present, but ensure format)
                Set<String> scopes = context.getAuthorizedScopes();
                context.getClaims().claim("scope", scopes);
            }
        };
    }

}
