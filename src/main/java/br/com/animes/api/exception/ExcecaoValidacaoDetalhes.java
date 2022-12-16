package br.com.animes.api.exception;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ExcecaoValidacaoDetalhes extends SuperExcecaoDetalhes{
	private final String campos;
	private final String campoMensagem;
}
