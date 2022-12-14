package com.restfulTeste.security;

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.restfulTeste.ApplicationContextLoad;
import com.restfulTeste.model.Usuario;
import com.restfulTeste.repository.UsuarioRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

//Criar a authenticacao e retornar a autenticacao
@Service
@Component
public class JwtTokenAutenticacaoService{

		//Tempo de validadção do TOKEN
		private static final long EXPIRATION_TIME = 172800000;
		
		//Uma senha unica para compor a authenticação
		private static final String SECRET = "senhaExtremamenteSecreta";
		
		//Prefixo padão de TOKEN
		private static final String TOKEN_PREFIX = "Bearer";
		
		private static final String HEADER_STRING = "Authorization";
		
		//Gera o token de resposta para o cliente com JWT
		public void addAuthentication(javax.servlet.http.HttpServletResponse response, String username) throws Exception {
			String JWT = Jwts.builder() //Chama o gerador de token
					.setSubject(username) //Adiciona o user
					.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) //Tempo de Expiração
					.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		
			//Junta o TOKEN com o Prefixo
			String token = TOKEN_PREFIX + " " + JWT; //Bearer 
			
			//Adiciona o cabeçalho HTTP
			response.addHeader(HEADER_STRING, token); //Authorization: Bearer
			
			//Escreve Token como resposta no corpo http
			response.getWriter().write("{\"Authorization\": \""+token+"\"}");
		
		}
		
		//Retorna o usuario validado  com token ou caso seja valido retorna null
		public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
			String token = request.getHeader(HEADER_STRING);
			
			if(token != null) {
				
				String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();
				
				//Faz a validação do token do usuario na requisição e obtem o USER
				String user = Jwts.parser()
						.setSigningKey(SECRET)
						.parseClaimsJws(tokenLimpo)
						.getBody().getSubject();
				
				if(user != null) {
					Usuario usuario = ApplicationContextLoad.getApplicationContext()
							.getBean(UsuarioRepository.class).findUserByLogin(user);
				
					if(usuario != null) {
						return new UsernamePasswordAuthenticationToken(
								usuario.getLogin(), usuario.getSenha(), usuario.getAuthorities());
					}
				}
				
			}
			
			liberacaoCors(response);
			return null;
		}
		
		//Fazendo liberação contra erro do Cors no navegador
		private void liberacaoCors(HttpServletResponse response) {
			
			if(response.getHeader("Access-Control-Allow-Origin") == null) {
				response.addHeader("Access-Control-Allow-Origin", "*");
			}
			
			if(response.getHeader("Access-Control-Allow-Headers") == null) {
				response.addHeader("Access-Control-Allow-Headers", "*");
			}
			
			if(response.getHeader("Access-Control-Request-Headers") == null) {
				response.addHeader("Access-Control-Request-Headers", "*");
			}
			if(response.getHeader("Access-Control-Allow-Methods") == null) {
				response.addHeader("Access-Control-Allow-Methods", "*");
			}
			
		}
		
}