package com.pega.swapi.service;

import com.pega.swapi.model.Character;
import com.pega.swapi.repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public class CharacterService {
    private final String BASE_URL = "https://swapi.dev/api/people/";

    @Autowired
    private CharacterRepository characterRepository;

    // Fetch all characters but only store/show name & image
    public List<Character> getAllCharacters() {
        // API se fetch karo, DB me save nahi karna
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(BASE_URL, Map.class);

        if (response != null && response.containsKey("results")) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

            // Sirf Name aur Image fetch karo, aur return karo (save nahi karna)
            return results.stream()
                    .map(data -> new Character(
                            (String) data.get("name"),
                            getImageUrlForCharacter((String) data.get("name"))
                    ))
                    .toList();
        }
        return List.of();
    }

    private List<Character> mapToCharacterList(List<Map<String, Object>> results) {
        return results.stream()
                .map(this::mapToCharacter) // mapToCharacter method ko call kar raha hai
                .toList();
    }


    // Search specific character, fetch details if not present
    public List<Character> searchCharacterByName(String name) {
        // Pehle database se check kar lo
        List<Character> cachedCharacters = characterRepository.findByName(name);
        if (!cachedCharacters.isEmpty()) {
            return cachedCharacters; // Agar pehle se hai, toh wahi return karo
        }

        // Agar DB mein nahi mila toh SWAPI API hit karo
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(BASE_URL + "?search=" + name, Map.class);

        if (response != null && response.containsKey("results")) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
            List<Character> characters = mapToCharacterList(results);

            // Save only if not already present in DB
            for (Character character : characters) {
                boolean exists = characterRepository.findByName(character.getName()).stream().findFirst().isPresent();
                if (!exists) {
                    characterRepository.save(character);
                }
            }

            return characters;
        }

        return List.of();
    }





    // Map API response to Character object
    private Character mapToCharacter(Map<String, Object> data) {
        return new Character(
                (String) data.get("name"),
                getImageUrlForCharacter((String) data.get("name")),
                (String) data.get("height"),
                (String) data.get("mass"),
                (String) data.get("hair_color"),
                (String) data.get("skin_color"),
                (String) data.get("eye_color"),
                (String) data.get("birth_year"),
                (String) data.get("gender")
        );
    }

    // Get character image URL
    private String getImageUrlForCharacter(String name) {
        switch (name) {
            case "Luke Skywalker": return "https://starwars-visualguide.com/assets/img/characters/1.jpg";
            case "Darth Vader": return "https://starwars-visualguide.com/assets/img/characters/4.jpg";
            case "Leia Organa": return "https://upload.wikimedia.org/wikipedia/en/1/1b/Princess_Leia%27s_characteristic_hairstyle.jpg";
            default: return "https://starwars-visualguide.com/assets/img/placeholder.jpg";
        }
    }
}
