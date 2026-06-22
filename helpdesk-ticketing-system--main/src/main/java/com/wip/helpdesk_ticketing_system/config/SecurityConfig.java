////package com.wip.helpdesk_ticketing_system.config;
////
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.security.config.annotation.web.builders.HttpSecurity;
////import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
////import org.springframework.security.crypto.password.PasswordEncoder;
////import org.springframework.security.web.SecurityFilterChain;
////
////@Configuration
////public class SecurityConfig {
////
////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        return new BCryptPasswordEncoder();
////    }
////
////    @Bean
////    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////
////        http
////            .csrf(csrf -> csrf.disable())
////            .authorizeHttpRequests(auth -> auth
////
////                // ✅ Swagger FIX (IMPORTANT)
////                .requestMatchers(
////                        "/swagger-ui/**",
////                        "/swagger-ui.html",
////                        "/v3/api-docs/**",
////                        "/v3/api-docs",
////                        "/webjars/**"
////                ).permitAll()
////
////                // your APIs
////                .anyRequest().permitAll()
////            );
////
////        return http.build();
////    }
////}
package com.wip.helpdesk_ticketing_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//            .csrf(csrf -> csrf.disable())
//            .sessionManagement(session ->
//                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            )
//            .authorizeHttpRequests(auth -> auth
//
//                // 🔓 Swagger access
//                .requestMatchers(
//                        "/swagger-ui/**",
//                        "/v3/api-docs/**"
//                ).permitAll()
//
//                // 👤 USER APIs
//                .requestMatchers("/users/**").hasRole("ADMIN")
//
//                // 🎫 TICKET APIs
//                .requestMatchers("/tickets/**").hasAnyRole("ADMIN", "AGENT", "END_USER")
//
//                // 🧑‍💼 ASSIGNMENT APIs
//                .requestMatchers("/assignments/**").hasRole("ADMIN")
//
//                // 🔧 RESOLUTION APIs
//                .requestMatchers("/resolutions/**").hasAnyRole("ADMIN", "AGENT")
//
//                // fallback
//                .anyRequest().authenticated()
//            )
//            .httpBasic();
//
//        return http.build();
//    }
//}

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
 
                // Swagger
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                // ROLE BASED ACCESS
                .requestMatchers("/users/**").hasRole("ADMIN")
//                .requestMatchers("/users/**").permitAll()
                .requestMatchers("/tickets/**").hasAnyRole("ADMIN", "AGENT", "END_USER")
                .requestMatchers("/assignments/**").hasRole("ADMIN")
                .requestMatchers("/resolutions/**").hasAnyRole("ADMIN", "AGENT")



                .anyRequest().authenticated()
            )
            .httpBasic();

        return http.build();
    }
	
	
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//	    http
//	        .csrf(csrf -> csrf.disable())
//	        .authorizeHttpRequests(auth -> auth
//	            .anyRequest().permitAll()
//	        );
//
//	    return http.build();
//	}
	
	
	
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//
//                // Swagger open
//                .requestMatchers(
//                    "/swagger-ui/**",
//                    "/swagger-ui.html",
//                    "/v3/api-docs/**"
//                ).permitAll()
//
//                // ALL APIs OPEN (temporary)
//                .anyRequest().permitAll()
//            );
//
//        return http.build();
//    }
}