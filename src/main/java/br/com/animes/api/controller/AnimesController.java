package br.com.animes.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.animes.api.dto.AnimePostDTO;
import br.com.animes.api.dto.AnimePutDTO;
import br.com.animes.dominio.model.Anime;
import br.com.animes.dominio.service.AnimesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("animes")
public class AnimesController {
	
	@Autowired
	private AnimesService service;
	
	@GetMapping
	@Operation(summary = "Lista de todos os Animes paginado", description = "O tamanho padrão é 5, use o parametro size para mudar o valor padrão")
	public ResponseEntity<Page<Anime>> listar(@Parameter(hidden = false) @ParameterObject Pageable paginacao){
		return ResponseEntity.ok(service.listar(paginacao));
	}
	
	@GetMapping(path = "/admin/listar")
	@Operation(summary = "Lista de todos os Animes sem paginação")
	public ResponseEntity<List<Anime>> listar(){
		return ResponseEntity.ok(service.listarSemPaginacao());
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Busca Anime pelo Id informado")
	public ResponseEntity<Anime> buscaPorId(@PathVariable Long id){
		return ResponseEntity.ok(service.buscaPorIdOuLancaException(id));
	}
	
	@GetMapping(path = "/porNome")
	@Operation(summary = "Busca Anime pelo nome informado")
	public ResponseEntity<Anime> buscaPorNome(@RequestParam String nome){
		return ResponseEntity.ok(service.buscaPorNome(nome));
	}
	
	@PostMapping(path = "/admin")
	@Operation(summary = "Cria novo Anime")
	public ResponseEntity<Anime> incluir(@Valid @RequestBody AnimePostDTO animeDTO) {
		return new ResponseEntity<>(service.incluir(animeDTO), HttpStatus.CREATED);
	}
	
	@PostMapping(path = "/incluiLista")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Cria vários Animes")
	public ResponseEntity<Void> incluirLista(@Valid @RequestBody List<AnimePostDTO> lista) {
		service.incluirVariosAnimes(lista);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/admin/{id}")
	@Operation(summary = "Exclui um Anime existente pelo id informado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Operação com sucesso!"),
			@ApiResponse(responseCode = "400", description = "Anime inexistente no banco")
	})
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		service.excluir(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(path = "/admin")
	@Operation(summary = "Atualiza um Anime existente")
	public ResponseEntity<Void> atualiza(@Valid @RequestBody AnimePutDTO animePutDTO){
		service.atualiza(animePutDTO);
		return ResponseEntity.noContent().build();
	}
	
}
