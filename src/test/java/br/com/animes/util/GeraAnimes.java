package br.com.animes.util;

import br.com.animes.dominio.model.Anime;

public class GeraAnimes {
		
	public static Anime criaNovoAnimeParaSerSalvo() {
		return Anime.builder().nome("Pokémon").build();
	}
	
	public static Anime criaNovoAnimeValido() {
		return Anime.builder().id(1L).nome("Pokémon").build();
	}
	
	public static Anime criaNovoAnimeParaSerAtualizado() {
		return Anime.builder().id(1L).nome("Pokémon 2").build();
	}
}
