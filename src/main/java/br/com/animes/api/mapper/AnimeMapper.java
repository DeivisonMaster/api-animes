package br.com.animes.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.animes.api.dto.AnimePostDTO;
import br.com.animes.api.dto.AnimePutDTO;
import br.com.animes.dominio.model.Anime;

@Mapper(componentModel = "spring")
public abstract class AnimeMapper {
	public static final AnimeMapper INSTANCIA = Mappers.getMapper(AnimeMapper.class);
	
	public abstract Anime paraAnime(AnimePostDTO animeDTO);
	public abstract Anime paraAnime(AnimePutDTO animeDTO);
}
