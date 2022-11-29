package com.wesledev.minhasFinancas.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wesledev.minhasFinancas.exception.ErrorAutenticacao;
import com.wesledev.minhasFinancas.exception.RegraNegocioException;
import com.wesledev.minhasFinancas.model.entity.Usuario;
import com.wesledev.minhasFinancas.model.repository.UsuarioRepository;
import com.wesledev.minhasFinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErrorAutenticacao("Usuario não encontrado para o email informado.");
		}
		
		if(usuario.get().getSenha().equals(senha)) {
			throw new ErrorAutenticacao("Senha inválida.");
		}
		
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
		}

	}

}
