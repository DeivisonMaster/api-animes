package br.com.animes.util;

import br.com.animes.api.dto.AnimePutDTO;

public class AnimePutDTOUtil {
	
	public static AnimePutDTO criaNovoAnimeParaSerAtualizado() {
		return AnimePutDTO.builder()
				.id(GeraAnimes.criaNovoAnimeParaSerAtualizado().getId())
				.nome(GeraAnimes.criaNovoAnimeParaSerAtualizado().getNome())
				.build();
	}
}
