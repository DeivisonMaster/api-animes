package br.com.animes.api.controller;


import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.animes.api.dto.AnimePostDTO;
import br.com.animes.api.dto.AnimePutDTO;
import br.com.animes.dominio.model.Anime;
import br.com.animes.dominio.service.AnimesService;
import br.com.animes.util.AnimePostDTOUtil;
import br.com.animes.util.AnimePutDTOUtil;
import br.com.animes.util.GeraAnimes;

/**
 * @ExtendWith Habilita o JUnit com Spring não iniciando todo o contexto da aplicação com em @SpringBootTest
 * */
@ExtendWith(SpringExtension.class)
class AnimesControllerTest {
	
	@InjectMocks
	private AnimesController animesController;
	
	@Mock
	private AnimesService animesService;

	@BeforeEach
	void setUp() {
		PageImpl<Anime> animePage = new PageImpl<>(listaAnimes());
		BDDMockito.when(animesService.listar(ArgumentMatchers.any())).thenReturn(animePage);
		
		BDDMockito.when(animesService.listarSemPaginacao()).thenReturn(animePage.toList());
		
		BDDMockito.when(animesService.buscaPorIdOuLancaException(ArgumentMatchers.anyLong())).thenReturn(GeraAnimes.criaNovoAnimeValido());
		
		BDDMockito.when(animesService.buscaPorNome(ArgumentMatchers.anyString())).thenReturn(GeraAnimes.criaNovoAnimeValido());
		
		BDDMockito.when(animesService.incluir(ArgumentMatchers.any(AnimePostDTO.class))).thenReturn(GeraAnimes.criaNovoAnimeValido());
		
		BDDMockito.doNothing().when(animesService).atualiza(ArgumentMatchers.any(AnimePutDTO.class));
		
		BDDMockito.doNothing().when(animesService).excluir(ArgumentMatchers.anyLong());
	}
	
	@Test
	@DisplayName("Listar - Deve listar animes com paginação")
	void deveListarAnimesComPaginacao() {
		String nomeAnime = GeraAnimes.criaNovoAnimeValido().getNome();
		Page<Anime> animePage = animesController.listar(null).getBody();
		
		Assertions.assertThat(animePage).isNotNull();
		Assertions.assertThat(animePage.toList()).isNotEmpty();
		Assertions.assertThat(animePage.toList()).hasSize(1);
		Assertions.assertThat(animePage.toList().get(0).getNome()).isEqualTo(nomeAnime);
	}
	
	@Test
	void deveListarAnimesSemPaginacao() {
		List<Anime> animes = animesController.listar().getBody();
		
		Assertions.assertThat(animes)
			.isNotNull()
			.isNotEmpty()
			.hasSize(1);
	}
	
	@Test
	@DisplayName("Listar - Deve buscar anime pelo Id informado")
	void deveBuscarAnimePorIdInformado() {
		Long id = GeraAnimes.criaNovoAnimeValido().getId();
		Anime anime = animesController.buscaPorId(id).getBody();
		
		Assertions.assertThat(anime).isNotNull();
		Assertions.assertThat(anime.getId()).isEqualTo(id);
	}
	
	@Test
	void deveBuscarUmAnimePeloNomeInformado() {
		String nomeAnime = GeraAnimes.criaNovoAnimeValido().getNome();
		Anime anime = animesController.buscaPorNome(nomeAnime).getBody();
		
		Assertions.assertThat(anime).isNotNull();
		Assertions.assertThat(anime.getNome()).isEqualTo(nomeAnime);
	}
	
	@Test
	void deveRetornarNullNaBuscaPorAnimeInexistente() {
		BDDMockito.when(animesService.buscaPorNome(ArgumentMatchers.anyString())).thenReturn(null);
		
		Anime animePesquisa = animesController.buscaPorNome("XXX").getBody();
		
		Assertions.assertThat(animePesquisa).isNull();
	}
	
	@Test
	void devePersistirUmAnimeNoBanco() {
		Long id = GeraAnimes.criaNovoAnimeValido().getId();
		Anime animeSalvo = animesController.incluir(AnimePostDTOUtil.criaNovoAnimeParaSerSalvo()).getBody();
		
		Assertions.assertThat(animeSalvo).isNotNull();
		Assertions.assertThat(animeSalvo.getId()).isNotNull();
		Assertions.assertThat(animeSalvo.getId()).isEqualTo(id);
	}
	
	@Test
	void deveAtualizarUmAnimeExistenteNoBanco() {
		ResponseEntity<Void> animeAtualizado = animesController.atualiza(AnimePutDTOUtil.criaNovoAnimeParaSerAtualizado());
		
		Assertions.assertThat(animeAtualizado.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		Assertions.assertThatCode(() -> animesController.atualiza(AnimePutDTOUtil.criaNovoAnimeParaSerAtualizado())).doesNotThrowAnyException();
	}
	
	@Test
	void deveExcluirUmAnimeExistenteNoBanco() {
		ResponseEntity<Void> animeAtualizado = animesController.excluir(1L);
		
		Assertions.assertThat(animeAtualizado.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		Assertions.assertThatCode(() -> animesController.excluir(1L)).doesNotThrowAnyException();
	}
	

	private List<Anime> listaAnimes() {
		List<Anime> animes = new ArrayList<>();
		animes.add(GeraAnimes.criaNovoAnimeValido());
		return animes;
	}

}
