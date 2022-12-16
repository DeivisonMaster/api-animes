package br.com.animes.api.dto;

import javax.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimePostDTO {
	
	@NotEmpty(message = "O campo nome deve ser preenchido")
	@Schema(description = "Este é o nome do Anime", example = "Pokémon", required = true)
	private String nome;
	
}
