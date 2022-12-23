package com.restfulTeste.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.restfulTeste.model.Usuario;
import com.restfulTeste.model.UsuarioDTO;
import com.restfulTeste.repository.UsuarioRepository;

//Arquitetura REST
@CrossOrigin
@RestController
@RequestMapping(value = "/usuario")
public class IndexController {

@Autowired
	private UsuarioRepository usuarioRepository;
	
	//Consulta ID
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<UsuarioDTO> init(@PathVariable(value = "id" ) Long id ) {
		
		Usuario usuario = usuarioRepository.findById(id).get();
		
		return new ResponseEntity<UsuarioDTO>(new UsuarioDTO(usuario), HttpStatus.OK);
	}
	
	
	//Consultar Todos
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Usuario>> usuario() throws InterruptedException{
		
		List<Usuario> list = (List<Usuario>) usuarioRepository.findAll();
		
		//Thread.sleep(6000);
		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}
	//END-POINT 
	//Consultar por Nome
	@GetMapping(value = "/usuarioPorNome/{nome}", produces = "application/json")
	public ResponseEntity<List<Usuario>> usuarioPorNome(@PathVariable("nome") String nome) throws InterruptedException{
		
		List<Usuario> list = (List<Usuario>) usuarioRepository.findUserByNome(nome);
		
		//Thread.sleep(6000);
		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}
	
	//Cadastro
	@PostMapping(value = "/",  produces = "application/json")
	public ResponseEntity<Usuario> cadastra(@RequestBody Usuario usuario) throws Exception{
		
		//Consumindo uma API publica externa
		
		//Fazendo consulta
		URL url = new URL("https://viacep.com.br/ws/"+usuario.getCep()+"/json/");
		URLConnection connection = url.openConnection();
		InputStream is = connection.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		
		//Convertendo para um objeto
		String cep = "";
		StringBuilder jsonCep = new StringBuilder();
		
		while((cep = br.readLine()) != null) {
			jsonCep.append(cep);
		}
		
		System.out.println(jsonCep.toString());
		
		Usuario userAux = new Gson().fromJson(jsonCep.toString(), Usuario.class);//Convertendo 
		
		//Incluindo
		usuario.setCep(userAux.getCep());
		usuario.setLogradouro(userAux.getLogradouro());
		usuario.setComplemento(userAux.getComplemento());
		usuario.setBairro(userAux.getBairro());
		usuario.setLocalidade(userAux.getLocalidade());
		usuario.setUf(userAux.getUf());
		
		
		//Criptografar senha do usuario
		String senhacriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(senhacriptografada);
		
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
	}
	
	//Atualizar
	@PutMapping(value = "/", produces = "application/json" )
	public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario){
		
		Usuario usuarioAtualiza = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuarioAtualiza, HttpStatus.OK);
	}
	
	//Deletar
	@DeleteMapping(value = "/{id}",  produces = "application/text" )
	public String delete(@PathVariable(value = "id")Long id){
		
		usuarioRepository.deleteById(id);
		
		
		return  "Ok";
	}
	
	
	
}