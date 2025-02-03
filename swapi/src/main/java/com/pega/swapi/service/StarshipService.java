package com.pega.swapi.service;

import com.pega.swapi.model.Starship;
import com.pega.swapi.repository.StarshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class StarshipService {
    private final String BASE_URL = "https://swapi.dev/api/starships/";

    @Autowired
    private StarshipRepository starshipRepository;

    // Fetch all starships but do not store them in DB
    public List<Starship> getAllStarships() {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(BASE_URL, Map.class);

        if (response != null && response.containsKey("results")) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

            return results.stream()
                    .map(data -> new Starship(
                            (String) data.get("name"),
                            getImageUrlForStarship((String) data.get("name"))
                    ))
                    .toList();
        }
        return List.of();
    }

    // Search specific starship, fetch details if not present
    public List<Starship> searchStarshipByName(String name) {
        List<Starship> cachedStarships = StarshipRepository.findByName(name);
        if (!cachedStarships.isEmpty()) {
            return cachedStarships; // If exists, return from DB
        }

        // If not found in DB, fetch from SWAPI
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(BASE_URL + "?search=" + name, Map.class);

        if (response != null && response.containsKey("results")) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
            List<Starship> starships = results.stream().map(this::mapToStarship).toList();

            for (Starship starship : starships) {
                boolean exists = StarshipRepository.findByName(starship.getName()).stream().findFirst().isPresent();
                if (!exists) {
                    starshipRepository.save(starship);
                }
            }
            return starships;
        }

        return List.of();
    }

    // Map API response to Starship object
    private Starship mapToStarship(Map<String, Object> data) {
        return new Starship(
                (String) data.get("name"),
                (String) data.get("model"),
                (String) data.get("manufacturer"),
                (String) data.get("cost_in_credits"),
                (String) data.get("length"),
                (String) data.get("max_atmosphering_speed"),
                (String) data.get("crew"),
                (String) data.get("passengers"),
                (String) data.get("cargo_capacity"),
                (String) data.get("starship_class"),
                getImageUrlForStarship((String) data.get("name"))
        );
    }

    // Get starship image URL
    private String getImageUrlForStarship(String name) {
        switch (name) {
            case "Millennium Falcon": return "https://starwars-visualguide.com/assets/img/starships/10.jpg";
            case "X-wing": return "https://starwars-visualguide.com/assets/img/starships/12.jpg";
            default: return "https://starwars-visualguide.com/assets/img/placeholder.jpg";
        }
    }
}
