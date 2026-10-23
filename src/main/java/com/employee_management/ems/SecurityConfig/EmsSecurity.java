package com.employee_management.ems.SecurityConfig;

import org.aspectj.weaver.patterns.HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class EmsSecurity {
    @Bean
    public SecurityFilterChain filterchain(HttpSecurity http) throws Exception {
        http
            //.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/","/login","/register","/css/**").permitAll()
                .requestMatchers("/register").hasRole("ADMIN")
//                .requestMatchers("/saveEmployee").hasRole("ADMIN")
//                .requestMatchers("/showFormForUpdate/**").hasRole("ADMIN")
//                .requestMatchers("/deleteEmployee/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/employeelist", true)
                .failureUrl("/?error=true")
                .permitAll()
            )
            .logout((logout) -> logout.permitAll())
            .exceptionHandling((handling) -> handling.accessDeniedPage("/access-denied"));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
