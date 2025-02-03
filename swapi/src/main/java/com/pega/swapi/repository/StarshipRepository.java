package com.pega.swapi.repository;

import com.pega.swapi.model.Starship;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StarshipRepository extends MongoRepository<Starship, String> {
    static List<Starship> findByName(String name) {
        return null;
    }

    boolean existsByName(String name);
}
