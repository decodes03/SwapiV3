package com.pega.swapi.repository;

import com.pega.swapi.model.Film;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FilmRepository extends MongoRepository<Film, String> {
    List<Film> findByTitle(String title);
    boolean existsByTitle(String title);
}
