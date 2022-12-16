package br.com.animes.api.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AnimePutDTO {
	
	@NotNull(message = "O campo id não pode estar vazio")
	private Long id;
	
	@NotEmpty(message = "O campo nome não pode estar vazio")
	private String nome;
}
