package com.restfulTeste.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restfulTeste.model.Usuario;
import com.restfulTeste.repository.UsuarioRepository;

//Arquitetura REST
@CrossOrigin
@RestController
@RequestMapping(value = "/usuario")
public class IndexController {

@Autowired
	private UsuarioRepository usuarioRepository;
	
	//Consulta
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> init(@PathVariable(value = "id" ) Long id ) {
		
		Usuario usuario = usuarioRepository.findById(id).get();
		
		return new ResponseEntity(usuario, HttpStatus.OK);
	}
	
	//Consultar Todos
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Usuario>> usuario(){
		
		List<Usuario> list = (List<Usuario>) usuarioRepository.findAll();
		
		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}
	
	//Cadastro
	@PostMapping(value = "/",  produces = "application/json")
	public ResponseEntity<Usuario> cadastra(@RequestBody Usuario usuario){
		
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