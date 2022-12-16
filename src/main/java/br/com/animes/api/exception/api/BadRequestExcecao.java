package br.com.animes.api.exception.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestExcecao extends RuntimeException{

	public BadRequestExcecao(String msg) {
		super(msg);
	}
}
