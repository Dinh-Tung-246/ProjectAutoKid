package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public CustomAuthenticatuonsuccessHandler successHanlder() {
        return new CustomAuthenticatuonsuccessHandler();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/admin/**").authenticated()
                        .requestMatchers("/autokid/**").permitAll()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/admin/login").permitAll()
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/admin/staff/home", true)
                        .failureUrl("/admin/login?error=true")
                        .successHandler(successHanlder())
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/admin/login")
                        .permitAll()
                )
                //cấu hình session
                .sessionManagement()
                .invalidSessionUrl("/admin/login")
                .maximumSessions(1)
                .expiredUrl("/admin/login");


        http.csrf().disable();
        return http.build();
    }

}