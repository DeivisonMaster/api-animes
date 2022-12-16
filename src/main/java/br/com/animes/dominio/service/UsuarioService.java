package br.com.animes.dominio.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.animes.dominio.model.Usuario;
import br.com.animes.dominio.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String nomeUsuario) throws UsernameNotFoundException {
		return Optional.ofNullable(usuarioRepository.findByNomeUsuario(nomeUsuario))
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));
	}

}
