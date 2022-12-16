package br.com.animes.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import br.com.animes.dominio.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SegurancaConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UsuarioService usuarioService;
	
	/**
	 * Gerenciador de autorização
	 * */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
			.disable()
//			.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
			.authorizeHttpRequests()
			.antMatchers("/animes/admin/**").hasRole("ADMIN")
			.antMatchers("/animes/**").hasRole("USER")
			.anyRequest()
			.authenticated()
			.and()
			.formLogin()
			.and()
			.httpBasic();
	}
	
	/**
	 * Gerenciador de autenticação
	 * */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		log.info("Senha criptografada {}", passwordEncoder.encode("teste"));
		
		auth.userDetailsService(usuarioService).passwordEncoder(passwordEncoder);
		
		//autenticaEmMemoria(auth, passwordEncoder);
	}

	private void autenticaEmMemoria(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder)
			throws Exception {
		auth.inMemoryAuthentication()
			.withUser("teste").password(passwordEncoder.encode("123")).roles("USER")
			.and()
			.withUser("admin").password(passwordEncoder.encode("321")).roles("USER", "ADMIN");
	}
	
}
