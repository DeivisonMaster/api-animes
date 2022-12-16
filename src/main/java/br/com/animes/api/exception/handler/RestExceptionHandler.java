package br.com.animes.api.exception.handler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.animes.api.exception.BadRequestExcecaoDetalhes;
import br.com.animes.api.exception.ExcecaoValidacaoDetalhes;
import br.com.animes.api.exception.api.BadRequestExcecao;

@ControllerAdvice
public class RestExceptionHandler {
	
	@ExceptionHandler(BadRequestExcecao.class)
	public ResponseEntity<BadRequestExcecaoDetalhes> lancaBadRequestException(BadRequestExcecao bRexception){
		return new ResponseEntity<>(BadRequestExcecaoDetalhes
				.builder()
				.timestamp(LocalDateTime.now())
				.detalhes(bRexception.getMessage())
				.status(HttpStatus.BAD_REQUEST.value())
				.titulo("Erro ao processar requisição")
				.msgDesenvolvedor(bRexception.getClass().getName())
				.build(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExcecaoValidacaoDetalhes> lancaExcecaoDeValidacaoComDetalhes(MethodArgumentNotValidException exception){
		String campo = exception.getBindingResult().getFieldErrors().stream().map(FieldError::getField).collect(Collectors.joining(","));
		String campoMensagem = exception.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));
		
		return new ResponseEntity<>(ExcecaoValidacaoDetalhes.builder()
				.timestamp(LocalDateTime.now())
				.detalhes(exception.getMessage())
				.status(HttpStatus.BAD_REQUEST.value())
				.titulo("Erro")
				.msgDesenvolvedor(exception.getClass().getName())
				.campos(campo)
				.campoMensagem(campoMensagem)
				.build(), HttpStatus.BAD_REQUEST);
	}
	
}
