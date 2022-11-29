package com.wesledev.minhasFinancas.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.wesledev.minhasFinancas.model.entity.Usuario;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository repository;

	@Test
	public void hasExistsAnEmail() {
		//cenario
		Usuario usuario = Usuario.builder().nome("User").email("usuario@teste.com").build();
		repository.save(usuario); 
		
		//ação
		boolean result = repository.existsByEmail("usuario@teste.com");
		
		//verificacao
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void hasReturnFalseWhenNotExistUserRegisterWithEmail() {
		
		//cenario
		repository.deleteAll();
		
		//acao
		boolean result = repository.existsByEmail("usuario@teste.com");
		
		//verificacao
		Assertions.assertThat(result).isFalse();
	}
}
