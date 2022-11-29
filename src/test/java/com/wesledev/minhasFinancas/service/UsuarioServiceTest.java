package com.wesledev.minhasFinancas.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.wesledev.minhasFinancas.exception.RegraNegocioException;
import com.wesledev.minhasFinancas.model.entity.Usuario;
import com.wesledev.minhasFinancas.model.repository.UsuarioRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@Autowired
	UsuarioService service;
	
	@Autowired
	UsuarioRepository repository;
	
	@Test(expected = Test.None.class)
	public void hasVerifyEmail() {
		
		//cenario
		repository.deleteAll();
		
		//acao
		service.validarEmail("email@email.com");
	}
	
	@Test(expected = RegraNegocioException.class)
	public void hasReturnErrorWithExistsEmailRegistered() {
		//cenario
		Usuario usuario = Usuario.builder().nome("usuario").email("email@email.com").build();
		repository.save(usuario);
		
		//acao
		service.validarEmail("email@email.com");
	}
}
