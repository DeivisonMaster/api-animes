package br.com.animes.dominio.repository;


import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.animes.dominio.model.Anime;
import br.com.animes.util.GeraAnimes;

/**
 * @DataJpaTest Habilita testes com Spring Data
 * */
@DataJpaTest
@DisplayName("Testes para o Repositório")
class AnimesRepositoryTest {
	
	@Autowired
	private AnimesRepository repository;

	@Test
	@DisplayName("Deve salvar novo Anime no Banco")
	void deveSalvarNoAnime() {
		Anime anime = GeraAnimes.criaNovoAnimeValido();
		Anime animeSalvo = this.repository.save(anime);
		
		Assertions.assertThat(animeSalvo).isNotNull();
		Assertions.assertThat(animeSalvo.getId()).isNotNull();
		Assertions.assertThat(animeSalvo.getNome()).isEqualTo(anime.getNome());
	}
	
	@Test
	@DisplayName("Deve lançar exceção ao tentar salvar novo Anime no Banco")
	void deveLancaConstraintViolationExceptionAoSalvarAnimeSemNomeInformado() {
		Anime anime = new Anime();
		
		Assertions.assertThatThrownBy(() -> this.repository.save(anime)).isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	@DisplayName("Deve atualizar um Anime no Banco")
	void deveAtualizarUmAnime() {
		Anime anime = GeraAnimes.criaNovoAnimeValido();
		Anime animeSalvo = this.repository.save(anime);
		
		animeSalvo.setNome("Digimon");
		Anime animeAtualizado = this.repository.save(animeSalvo);
		
		Assertions.assertThat(animeAtualizado).isNotNull();
		Assertions.assertThat(animeAtualizado.getId()).isNotNull();
		Assertions.assertThat(animeAtualizado.getNome()).isEqualTo(animeSalvo.getNome());
	}
	
	@Test
	@DisplayName("Deve excluir um Anime no Banco")
	void deveExcluirUmAnime() {
		Anime anime = GeraAnimes.criaNovoAnimeValido();
		this.repository.save(anime);
		this.repository.delete(anime);
		
		Optional<Anime> optionalAnime = this.repository.findById(anime.getId());
		
		Assertions.assertThat(optionalAnime).isEmpty();
	}
	
	@Test
	@DisplayName("Deve buscar um Anime pelo nome informado")
	void deveBuscarUmAnimePeloNome() {
		Anime anime = GeraAnimes.criaNovoAnimeValido();
		Anime animeSalvo = this.repository.save(anime);
		Anime animePesquisa = this.repository.findByNome(animeSalvo.getNome());
		
		Assertions.assertThat(animePesquisa).isNotNull();
		Assertions.assertThat(anime.getNome()).isEqualTo(animePesquisa.getNome());
	}
	
	@Test
	@DisplayName("Deve retornar null quando nome do anime informado não for encontrado")
	void deveRetornarNullNaConsultaDoNomeDoAnime() {
		Anime animePesquisa = this.repository.findByNome("Dragon Ball");
		
		Assertions.assertThat(animePesquisa).isNull();
	}
	
}
