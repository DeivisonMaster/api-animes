package br.com.animes.api.integracao;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import br.com.animes.api.dto.AnimePostDTO;
import br.com.animes.api.dto.AnimePutDTO;
import br.com.animes.api.wrapper.PageableResponse;
import br.com.animes.dominio.model.Anime;
import br.com.animes.dominio.model.Usuario;
import br.com.animes.dominio.repository.AnimesRepository;
import br.com.animes.dominio.repository.UsuarioRepository;
import br.com.animes.util.AnimePostDTOUtil;
import br.com.animes.util.GeraAnimes;

/**
 * @SpringBootTest Inicia o contexto da aplicação Spring
 * @AutoConfigureTestDatabase Habilita o uso de banco em memória
 * @DirtiesContext	Para cada método de teste excluir os inserts anterior no banco
 * */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AnimesControllerIT {
	
	@Autowired
	@Qualifier(value = "restTemplateRegraUsuario")
	private TestRestTemplate restTemplateRegraUsuario;
	
	@Autowired
	@Qualifier(value = "restTemplateRegraAdmin")
	private TestRestTemplate restTemplateRegraAdmin;
	
	@Autowired
	private AnimesRepository animesRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private static final Usuario USUARIO = Usuario.builder()
			.nome("teste")
			.senha("{bcrypt}$2a$10$ZqysVSoBuAYCHCgTWPGI6uESMCi3Ppfm2Pu2/7sW6Hq9bOrw3djcO")
			.nomeUsuario("teste")
			.permissoes("ROLE_USER")
			.build();
	
	private static final Usuario ADMIN = Usuario.builder()
			.nome("admin")
			.senha("{bcrypt}$2a$10$ZqysVSoBuAYCHCgTWPGI6uESMCi3Ppfm2Pu2/7sW6Hq9bOrw3djcO")
			.nomeUsuario("admin")
			.permissoes("ROLE_USER,ROLE_ADMIN")
			.build();
	
	@TestConfiguration
	@Lazy
	static class Config{
		
		@Bean(name = "restTemplateRegraUsuario")
		public TestRestTemplate restTemplateRegraUsuario(@Value("${local.server.port}") int porta) {
			RestTemplateBuilder builder = new RestTemplateBuilder()
					.rootUri("http://localhost:" + porta)
					.basicAuthentication("teste", "teste");
			return new TestRestTemplate(builder);
		}
		
		@Bean(name = "restTemplateRegraAdmin")
		public TestRestTemplate restTemplateRegraAdmin(@Value("${local.server.port}") int porta) {
			RestTemplateBuilder builder = new RestTemplateBuilder()
					.rootUri("http://localhost:" + porta)
					.basicAuthentication("admin", "teste");
			return new TestRestTemplate(builder);
		}
	}
	
	@Test
	@DisplayName("Listar - Deve listar animes com paginação")
	void deveListarAnimesComPaginacao() {
		Anime animeSalvo = animesRepository.save(GeraAnimes.criaNovoAnimeParaSerSalvo());
		usuarioRepository.save(USUARIO);
		
		PageableResponse<Anime> animePage = restTemplateRegraUsuario
				.exchange("/animes", HttpMethod.GET, null, new ParameterizedTypeReference<PageableResponse<Anime>>() {}).getBody();
		
		Assertions.assertThat(animePage).isNotNull();
		Assertions.assertThat(animePage.toList())
			.isNotEmpty()
			.hasSize(1);
		Assertions.assertThat(animePage.toList().get(0).getNome()).isEqualTo(animeSalvo.getNome());
	}
	
	@Test
	void deveListarAnimesSemPaginacao() {
		Anime animeSalvo = animesRepository.save(GeraAnimes.criaNovoAnimeParaSerSalvo());
		usuarioRepository.save(ADMIN);
		
		List<Anime> animes = restTemplateRegraAdmin
				.exchange("/animes/admin/listar", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {}).getBody();
		
		Assertions.assertThat(animes)
			.isNotNull()
			.isNotEmpty()
			.hasSize(1);
		Assertions.assertThat(animes.get(0).getNome()).isEqualTo(animeSalvo.getNome());
	}
	
	@Test
	@DisplayName("Listar - Deve buscar anime pelo Id informado")
	void deveBuscarAnimePorIdInformado() {
		Anime animeSalvo = animesRepository.save(GeraAnimes.criaNovoAnimeParaSerSalvo());
		usuarioRepository.save(USUARIO);
		
		Anime animePesquisa = restTemplateRegraUsuario.exchange("/animes/{id}", HttpMethod.GET, null, Anime.class, animeSalvo.getId()).getBody();
		
		Assertions.assertThat(animePesquisa).isNotNull();
		Assertions.assertThat(animePesquisa.getId()).isNotNull();
		Assertions.assertThat(animePesquisa.getId()).isEqualTo(animeSalvo.getId());
	}
	
	@Test
	void deveBuscarUmAnimePeloNomeInformado() {
		Anime anime = GeraAnimes.criaNovoAnimeValido();
		Anime animeSalvo = animesRepository.save(anime);
		usuarioRepository.save(USUARIO);
		
		String url = String.format("/animes/porNome?nome=%s", animeSalvo.getNome());
		Anime animePesquisa = restTemplateRegraUsuario.exchange(url, HttpMethod.GET, null, Anime.class).getBody();
		
		Assertions.assertThat(animePesquisa).isNotNull();
		Assertions.assertThat(animeSalvo.getNome()).isEqualTo(animePesquisa.getNome());
	}
	
	@Test
	void deveRetornarNullNaBuscaPorAnimeInexistente() {
		usuarioRepository.save(USUARIO);
		
		String url = String.format("/animes/porNome?nome=%s", "xxx");
		Anime animePesquisa = restTemplateRegraUsuario.exchange(url, HttpMethod.GET, null, Anime.class).getBody();
		
		Assertions.assertThat(animePesquisa).isNull();
	}
	
	@Test
	void devePersistirUmAnimeNoBanco() {
		usuarioRepository.save(ADMIN);
		
		AnimePostDTO animePOST = AnimePostDTOUtil.criaNovoAnimeParaSerSalvo();
		
		ResponseEntity<Anime> animeSalvo = restTemplateRegraAdmin.exchange("/animes/admin", HttpMethod.POST, new HttpEntity<>(animePOST), Anime.class);
		
		Assertions.assertThat(animeSalvo).isNotNull();
		Assertions.assertThat(animeSalvo.getBody()).isNotNull();
		Assertions.assertThat(animeSalvo.getBody().getId()).isNotNull();
		Assertions.assertThat(animeSalvo.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}
	
	@Test
	void deveAtualizarUmAnimeExistenteNoBanco() {
		Anime animeSalvo = animesRepository.save(GeraAnimes.criaNovoAnimeParaSerSalvo());
		usuarioRepository.save(ADMIN);
		
		animeSalvo.setNome("Pokémon 2");
		
		AnimePutDTO animePUT = AnimePutDTO.builder().id(animeSalvo.getId()).nome(animeSalvo.getNome()).build();
		
		ResponseEntity<Void> animeAtualizado = restTemplateRegraAdmin.exchange("/animes/admin", HttpMethod.PUT, new HttpEntity<>(animePUT), Void.class);
		
		Assertions.assertThat(animeAtualizado).isNotNull();
		Assertions.assertThat(animeAtualizado.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
	
	@Test
	@DisplayName("Deve excluir um Anime e retornar status code 204")
	void deveExcluirUmAnimeExistenteNoBancoComUsuarioComPermissao() {
		Anime animeSalvo = animesRepository.save(GeraAnimes.criaNovoAnimeParaSerSalvo());
		usuarioRepository.save(ADMIN);
		
		ResponseEntity<Void> animeExclusao = restTemplateRegraAdmin.exchange("/animes/admin/{id}", HttpMethod.DELETE, null, Void.class, animeSalvo.getId());
		
		Assertions.assertThat(animeExclusao).isNotNull();
		Assertions.assertThat(animeExclusao.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
	
	@Test
	@DisplayName("Não deve excluir um Anime e retornar status code 403")
	void naoDeveExcluirUmAnimeExistenteNoBancoComUsuarioSemPermissao() {
		Anime animeSalvo = animesRepository.save(GeraAnimes.criaNovoAnimeParaSerSalvo());
		usuarioRepository.save(USUARIO);
		
		ResponseEntity<Void> animeExclusao = restTemplateRegraUsuario.exchange("/animes/admin/{id}", HttpMethod.DELETE, null, Void.class, animeSalvo.getId());
		
		Assertions.assertThat(animeExclusao).isNotNull();
		Assertions.assertThat(animeExclusao.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}
}
