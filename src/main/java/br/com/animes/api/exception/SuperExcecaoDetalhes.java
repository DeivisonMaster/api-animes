package br.com.animes.api.exception;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class SuperExcecaoDetalhes {
	private String titulo;
	private int status;
	private String detalhes;
	private String msgDesenvolvedor;
	private LocalDateTime timestamp;
	
	
}
