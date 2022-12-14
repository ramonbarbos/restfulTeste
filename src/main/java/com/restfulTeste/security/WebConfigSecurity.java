package com.restfulTeste.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.restfulTeste.service.ImplementUserDetails;

//Mapeia URL, endereços, autoriza ou bloqueia acesso a URL
@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter  {
	
	@Autowired
	private ImplementUserDetails implementUserDetails;
	
	//COnfigura as solicitações de acesso por Http
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
		
		//Ativando a proteção contra usuario que não estão logados
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		
		//Ativando a permissão para acessar a pagina inicial do sistema
		.disable().authorizeRequests().antMatchers("/").permitAll()
		.antMatchers("/index").permitAll()
		
		//URL de Logout -  Redireciona apos o usuario sair do sistema
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
		
		//Mapeia URL de logout e invalida o usuario
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		
		//Filtra requisições de login para autenticação
		.and().addFilterAfter( new JwtLoginFilter("/login", authenticationManager()),  UsernamePasswordAuthenticationFilter.class)
		
		//Fltra demais requisições para verificar a presenção do TOKEN JWT no HEADER HTTP
		
		.addFilterBefore(  new JwtApiAuthenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
		
		
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		//Service que irá consultar o usuario no banco de dados
		auth.userDetailsService(implementUserDetails)
		//padrão de codificação de senha
		.passwordEncoder(new BCryptPasswordEncoder());
		
	}
	

}