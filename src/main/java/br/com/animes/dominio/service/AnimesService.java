package br.com.animes.dominio.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.animes.api.dto.AnimePostDTO;
import br.com.animes.api.dto.AnimePutDTO;
import br.com.animes.api.exception.api.BadRequestExcecao;
import br.com.animes.api.mapper.AnimeMapper;
import br.com.animes.dominio.model.Anime;
import br.com.animes.dominio.repository.AnimesRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnimesService {
	
	@Autowired
	private AnimesRepository repository;

	public Page<Anime> listar(Pageable paginacao) {
		log.info("Listar: " + LocalDateTime.now());
		return repository.findAll(paginacao);
	}
	
	public List<Anime> listarSemPaginacao() {
		return repository.findAll();
	}

	public Anime buscaPorIdOuLancaException(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new BadRequestExcecao("Anime não encontrado!"));
	}

	public Anime incluir(AnimePostDTO animeDTO) {
		Anime anime = Anime.builder().nome(animeDTO.getNome()).build();
		return repository.save(anime);
	}

	public void excluir(Long id) {
		repository.delete(buscaPorIdOuLancaException(id));
	}

	public void atualiza(AnimePutDTO animePutDTO) {
		Anime animePesquisa = buscaPorIdOuLancaException(animePutDTO.getId());
		animePesquisa.setNome(animePutDTO.getNome());
		repository.save(buscaPorIdOuLancaException(animePutDTO.getId()));
	}

	public Anime buscaPorNome(String nome) {
		return repository.findByNome(nome);
	}

	public void incluirVariosAnimes(List<AnimePostDTO> animes) {
		animes.forEach(anime -> {
			Anime animePOST = AnimeMapper.INSTANCIA.paraAnime(anime);
			repository.save(animePOST);
		});
	}


}
