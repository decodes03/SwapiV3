package com.pega.swapi.repository;

import com.pega.swapi.model.Character;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CharacterRepository extends MongoRepository<Character, String> {
    List<Character> findByName(String name);
    boolean existsByName(String name); // ✅ Check if character already exists
}
