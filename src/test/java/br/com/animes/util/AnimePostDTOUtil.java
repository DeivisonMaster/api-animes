package br.com.animes.util;

import br.com.animes.api.dto.AnimePostDTO;

public class AnimePostDTOUtil {
	
	public static AnimePostDTO criaNovoAnimeParaSerSalvo() {
		return AnimePostDTO.builder().nome(GeraAnimes.criaNovoAnimeParaSerSalvo().getNome()).build();
	}
}
