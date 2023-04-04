# Oauth2-google

Para prover o acesso á API do google, é necessário abri uma conta no google cloud, 

ir a aba credentials

Criar ID do cliente do OAuth
tipo de app
passar o name;

posterior passar a uri de seu app,

URIs de redirecionamento autorizados

conforme os acessos de uri de sua app

http://localhost:8080/login/oauth2/code/google

http://localhost:8080 é a minha URI do server, o login/oauth2/code/google a a API do google para fazer a validação!

posterior a isto aperte em criar, posterior retornará um client_id e client_secret, guarde em segurança

prefiro sempre usar o application.yaml devido ser mais semantico, 
configutações do app.yaml para o OAUTH2 do google.


dependendias de security

<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.security</groupId>
<artifactId>spring-security-test</artifactId>
<scope>test</scope>
</dependency>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
</dependencies>
 
conforme ja falei acima, passe para seu app.yaml a client_id e client_secret

para fazer a integração do OAUTH2

precisamos da seguinte dependencia

<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>


configuração padrão do oauth2
.oauth2Login(Customizer.withDefaults())
				.build();
        
        
veja ela dentro do SecurityConfig nossa customização de acesso!

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

para gerenciar o JWT

<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>

	.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
  
  esta e a anotação de configuração do JWT, 

para gerenciar o JWT


esta é a config padão do app.yml

spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: id-to-google
            client-secret: secret-to-google
     
     
     
     
 agora inserindo um JWT na nossa configuração
 
 esta é a anotação
  resourceserver:
        jwt:
          issuer-uri: https://accounts.google.com  
          
  precisa esta certamente na mesma identação do client:
 
 spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: id-to-google
            client-secret: secret-to-google
      resourceserver:
        jwt:
          issuer-uri: https://accounts.google.com  
          
          
  pegando as propriedade o 
          
 	@GetMapping("/public")
	String publicRoute() {
		return "<H1> public route, feel free to look arround! lock-open <h1/>";
	}
	
  
  esta singela anotção é do OPEN ID OidcUser,
  
  ele vai tratar de buscar @AuthenticationPrincipal os dados de authenticação  conforme a string de retorno a baixo....
  
  
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
          
          
