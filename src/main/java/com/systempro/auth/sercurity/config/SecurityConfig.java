package com.systempro.auth.sercurity.config;

import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		return http.authorizeHttpRequests(authorizeConfig -> {
			// liberado acesso sem autenticação para "/public"
			authorizeConfig.requestMatchers("/public").permitAll();
			authorizeConfig.requestMatchers("/logout").permitAll();
			// estamos dizendo que todos devem ser autenticados
			// com esta configuração authorizeConfig.anyRequest().authenticated();
			authorizeConfig.anyRequest().authenticated();
		})
				// .formLogin(Customizer.withDefaults())
				.oauth2Login(Customizer.withDefaults())
				.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
				.build();

	}
}
