package com.pega.swapi.service;

import com.pega.swapi.model.Film;
import com.pega.swapi.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FilmService {
    private final String BASE_URL = "https://swapi.dev/api/films/";

    @Autowired
    private FilmRepository filmRepository;

    // Fetch all films but do not store them in DB
    public List<Film> getAllFilms() {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(BASE_URL, Map.class);

        if (response != null && response.containsKey("results")) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

            // Fetch and return only title and image (do not save in DB)
            return results.stream()
                    .map(data -> new Film(
                            (String) data.get("title"),
                            getImageUrlForFilm((String) data.get("title"))
                    ))
                    .toList();
        }
        return List.of();
    }

    // Search specific film, fetch details if not present
    public List<Film> searchFilmByTitle(String title) {
        // First, check in the database
        List<Film> cachedFilms = filmRepository.findByTitle(title);
        if (!cachedFilms.isEmpty()) {
            return cachedFilms; // If exists, return from DB
        }

        // If not found in DB, fetch from SWAPI
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(BASE_URL + "?search=" + title, Map.class);

        if (response != null && response.containsKey("results")) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
            List<Film> films = results.stream().map(this::mapToFilm).toList();

            // Save only if not already present in DB
            for (Film film : films) {
                boolean exists = filmRepository.findByTitle(film.getTitle()).stream().findFirst().isPresent();
                if (!exists) {
                    filmRepository.save(film);
                }
            }

            return films;
        }

        return List.of();
    }

    // Map API response to Film object
    private Film mapToFilm(Map<String, Object> data) {
        return new Film(
                (String) data.get("title"),
                getImageUrlForFilm((String) data.get("title")),
                (String) data.get("director"),
                (String) data.get("producer"),
                (String) data.get("release_date")
        );
    }

    // Get film image URL
    private String getImageUrlForFilm(String title) {
        switch (title) {
            case "A New Hope": return "https://starwars-visualguide.com/assets/img/films/1.jpg";
            case "The Empire Strikes Back": return "https://starwars-visualguide.com/assets/img/films/2.jpg";
            case "Return of the Jedi": return "https://starwars-visualguide.com/assets/img/films/3.jpg";
            case "The Phantom Menace": return "https://starwars-visualguide.com/assets/img/films/4.jpg";
            case "Attack of the Clones": return "https://starwars-visualguide.com/assets/img/films/5.jpg";
            case "Revenge of the Sith": return "https://starwars-visualguide.com/assets/img/films/6.jpg";
            default: return "https://starwars-visualguide.com/assets/img/placeholder.jpg";
        }
    }
}
