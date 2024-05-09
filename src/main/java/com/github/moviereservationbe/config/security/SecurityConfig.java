package com.github.moviereservationbe.config.security;

import com.github.moviereservationbe.service.exceptions.CustomAccessDeniedHandler;
import com.github.moviereservationbe.service.exceptions.CustomAuthenticationEntryPoint;
import com.github.moviereservationbe.web.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .headers(h -> h.frameOptions(f -> f.sameOrigin()))
                .csrf(c->c.disable())
                .httpBasic(h->h.disable())
                .formLogin(f->f.disable())
                .rememberMe(r->r.disable())
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(c-> c.configurationSource(corsConfig()))
                .authorizeRequests(a->
                        a
                                .requestMatchers("/resources/static/**", "/auth/sign-up", "/auth/login", "/auth/logout").permitAll()
                                .requestMatchers("/test").hasRole("USER")
                )
                .exceptionHandling(e->{
                    e.authenticationEntryPoint(new CustomAuthenticationEntryPoint());
                    e.accessDeniedHandler(new CustomAccessDeniedHandler());
                })
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private CorsConfigurationSource corsConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowedMethods(List.of("GET","PUT","POST","DELETE"));
        corsConfiguration.setMaxAge(1000L*60*60);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        corsConfiguration.setExposedHeaders(Arrays.asList("Authorization", "Authorization-refresh"));
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
}
