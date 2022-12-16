package br.com.animes.api.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.com.animes.api.dto.AnimePostDTO;
import br.com.animes.api.dto.AnimePutDTO;
import br.com.animes.api.mapper.AnimeMapper;
import br.com.animes.dominio.model.Anime;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SpringClient {
	private static final String ENDPOINT = "http://localhost:8080/animes/";
	private static final String ENDPOINT_ID = "http://localhost:8080/animes/{id}";

	public static void main(String[] args) {
		ResponseEntity<Anime> animeGet = new RestTemplate().getForEntity(ENDPOINT_ID, Anime.class, 2);
		log.info(animeGet);
		log.info("**************************");
		
		Anime animeObject = new RestTemplate().getForObject(ENDPOINT_ID, Anime.class, 2);
		log.info(animeObject);
		log.info("**************************");
		
		ResponseEntity<List<Anime>> exchange = new RestTemplate().exchange(ENDPOINT + "listar", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
		});
		log.info(exchange.getBody());
		
		AnimePostDTO novoAnime = AnimePostDTO.builder().nome("Anime 2023").build();
//		ResponseEntity<Anime> animePost = new RestTemplate().postForEntity(ENDPOINT, novoAnime, Anime.class);
//		log.info(animePost);
		
		ResponseEntity<Anime> animePostExchange = new RestTemplate().exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<>(novoAnime, getJsonHeader()), Anime.class);
		log.info(animePostExchange);
		
		ResponseEntity<Anime> animePesquisa = new RestTemplate().exchange(ENDPOINT_ID, HttpMethod.GET, null, Anime.class, 32);
		Anime animeEdicao = animePesquisa.getBody();
		animeEdicao.setNome("Anime 20233");
		AnimePutDTO animePutDTO = AnimePutDTO.builder().id(animeEdicao.getId()).nome(animeEdicao.getNome()).build();
		
		ResponseEntity<Void> animePut = new RestTemplate().exchange(ENDPOINT, HttpMethod.PUT, new HttpEntity<>(animePutDTO, getJsonHeader()), Void.class);
		log.info(animePut);
		
		ResponseEntity<Void> animeDelete = new RestTemplate().exchange(ENDPOINT_ID, HttpMethod.DELETE, null, Void.class, 32);
		log.info(animeDelete);
		
		ResponseEntity<Anime> animePorNome = new RestTemplate().exchange(ENDPOINT + "/porNome/{nome}", HttpMethod.GET, null, Anime.class, "Pokémon");
		log.info(animePorNome.getBody());
	}

	private static HttpHeaders getJsonHeader() {
		HttpHeaders httpHeader = new HttpHeaders();
		httpHeader.setContentType(MediaType.APPLICATION_JSON);
		return httpHeader;
	}
	
}
