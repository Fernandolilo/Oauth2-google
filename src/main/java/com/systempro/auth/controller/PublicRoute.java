package com.systempro.auth.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicRoute {

	@GetMapping("/public")
	String publicRoute() {
		return "<H1> public route, feel free to look arround! lock-open <h1/>";
	}
	
	@GetMapping("/private")
	String privateRoute(@AuthenticationPrincipal OidcUser principal) {
		
				return String.format("""
						<h1>Oauth2 🔐  </h1>
					<h3>Principal: %s</h3>
					<h3>Email attribute: %s</h3>
					<h3>Authorities: %s</h3>
					<h3>JWT: %s</h3>
					""", principal, principal.getAttribute("email"), principal.getAuthorities(),
					principal.getIdToken().getTokenValue());
	}
	
	@GetMapping("/jwt")
	String jwt(@AuthenticationPrincipal Jwt jwt) {
		return String.format("""
				Principal: %s\n
				Email attribute: %s\n
				JWT: %s\n
				""", jwt.getClaims(), jwt.getClaim("email"), jwt.getTokenValue());
	}
}
