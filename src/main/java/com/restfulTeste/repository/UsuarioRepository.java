package com.restfulTeste.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.restfulTeste.model.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long>{


	@Query("select u from Usuario u where u.login = ?1")
	Usuario findUserByLogin(String login);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value="update usuario set token = ?1 where login = ?2 ")
	void atualizarTokenUser(String login, String token);
	
}
