package com.example.elblog.security.config;

import com.example.elblog.security.service.UserDetailsServiceImpl;
import lombok.Data;
import org.elasticsearch.core.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@ConfigurationProperties(prefix = "front-end-config")
@EnableWebSecurity
@Data
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private String[] url;
    @Override
    protected void configure(HttpSecurity security) throws Exception
    {
        security.cors()
                .and().httpBasic().disable()
                .csrf().disable();

        security.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        security.headers()
                .frameOptions()
                .disable();

        /*security.authorizeRequests()
                .antMatchers("/blog","/blog/**").permitAll()
                .antMatchers("/blogger/about").permitAll()
                .antMatchers("/home").permitAll()
                .antMatchers("/comment").permitAll()
                .antMatchers("/auth","/auth/**").permitAll()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated();*/
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    // 解决前端请求跨域问题
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(url));// if your front end running on localhost:5000
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS","PATCH"));
        configuration.setAllowedHeaders(List.of(CorsConfiguration.ALL));
        configuration.setExposedHeaders(Arrays.asList("Content-Type","X-Requested-With","accept","Origin","Access-Control-Request-Method","Access-Control-Request-Headers"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
