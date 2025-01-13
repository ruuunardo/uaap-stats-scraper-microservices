package com.teamr.runardo.uaapdatawebapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(configurer -> configurer
//                        .requestMatchers(HttpMethod.POST,"/uaap-games/save").hasRole("ADMIN")
                        .requestMatchers("/login", "/", "/error", "/uaap-games/home", "/images/**").permitAll()
                        .requestMatchers("/uaap-games", "/uaap-games/gamelist/**").permitAll()
                        .requestMatchers("/uaap-games/update/**", "/uaap-games/export-to-csv").authenticated()
//                        .requestMatchers("/uaap-games/delete/**", "/uaap-games/edit/**", "/uaap-games/show-form", "/uaap-games/save").permitAll()
                        .requestMatchers("/uaap-games/delete/**", "/uaap-games/edit/**", "/uaap-games/show-form", "/uaap-games/save").hasRole("ADMIN")
                        .requestMatchers("/uaap-games/checkUrl/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/uaap-games", true)
                        .permitAll())
                .oauth2Login(config -> config
                        .loginPage("/login")
                        .defaultSuccessUrl("/uaap-games", true)
                        .permitAll())

                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll())
                .exceptionHandling(config -> config.accessDeniedPage("/access-denied"))
        ;

        return httpSecurity.build();
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails defaultUser = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("admin123"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(defaultUser);
//    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

        userDetailsManager
                .setUsersByUsernameQuery("select username, password, active from users where username = ?");
        userDetailsManager
                .setAuthoritiesByUsernameQuery("select username, role from roles where username = ?");

        return userDetailsManager;
    }
}

