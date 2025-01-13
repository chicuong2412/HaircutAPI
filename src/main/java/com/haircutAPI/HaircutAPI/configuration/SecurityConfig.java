package com.haircutAPI.HaircutAPI.configuration;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final String[] PublicEndpoints = { "/workers", "/customers", "/auth/workers/login", "/auth/introspect",
            "/auth/customers/login" };
    private final String[] publicGetEndpoints = { "/locations/getLocations", "/locations/getLocation/**",
            "/services/getServiceByID/**",
            "/services/getAllServices", "/services/getAllPublicServices" ,
            "/products/getProductByID/**", "/products/getProducts", "/combos/getComboByID/**", "/combos/getAllCombos",
            "/combos/getComboByID/**" };
    @Value("${jwt.SIGNED_KEY}")
    protected String SIGNED_KEY;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(
                rq -> rq.requestMatchers(HttpMethod.POST, PublicEndpoints)
                        .permitAll().requestMatchers(HttpMethod.GET, publicGetEndpoints).permitAll()
                        .anyRequest().authenticated());

        httpSecurity.oauth2ResourceServer(oauth2 -> {
            oauth2.jwt(jwt -> {
                jwt.decoder(jwtDecoder());
            });
        });
        httpSecurity.csrf(t -> t.disable());
        httpSecurity.addFilter(corsFilter());
        return httpSecurity.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNED_KEY.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }
}
