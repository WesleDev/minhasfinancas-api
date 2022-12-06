package com.wesledev.minhasFinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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

	@Autowired
	TestEntityManager entityManager;

	@Test
	public void hasExistsAnEmail() {
		// cenario
		Usuario usuario = createUser();
		entityManager.persist(usuario);

		// ação
		boolean result = repository.existsByEmail("usuario@email.com");

		// verificacao
		Assertions.assertThat(result).isTrue();
	}

	@Test
	public void hasReturnFalseWhenNotExistUserRegisterWithEmail() {

		// cenario

		// acao
		boolean result = repository.existsByEmail("usuario@teste.com");

		// verificacao
		Assertions.assertThat(result).isFalse();
	}

	@Test
	public void hasPersistUserInDatabase() {
		// cenario
		Usuario usuario = createUser();
		// acao
		Usuario usuarioSalvo = repository.save(usuario);

		// verificacao
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}

	@Test
	public void hasSearchUserByEmail() {
		// cenario
		Usuario usuario = createUser();
		entityManager.persist(usuario);

		// verificacao
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");

		Assertions.assertThat(result.isPresent()).isTrue();
	}

	@Test
	public void hasReturnEmptyUserEmailWhenNotExistsInDatabase() {
		// cenario

		// verificacao
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");

		Assertions.assertThat(result.isPresent()).isFalse();
	}

	public static Usuario createUser() {
		return Usuario.builder().nome("usuario").email("usuario@email.com").senha("senha").build();
	}
}
