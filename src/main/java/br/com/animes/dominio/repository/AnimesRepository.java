package br.com.animes.dominio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.animes.dominio.model.Anime;

@Repository
public interface AnimesRepository extends JpaRepository<Anime, Long>{

	Anime findByNome(String nome);

}
