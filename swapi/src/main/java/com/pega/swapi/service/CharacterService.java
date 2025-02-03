package com.pega.swapi.service;

import com.pega.swapi.model.Character;
import com.pega.swapi.repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class CharacterService {
    private final String BASE_URL = "https://swapi.dev/api/people/";

    @Autowired
    private CharacterRepository characterRepository;

    public List<Character> getAllCharacters() {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(BASE_URL, Map.class);

        if (response != null && response.containsKey("results")) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

            System.out.println("Fetching characters from SWAPI API...");

            return results.stream()
                    .map(data -> new Character(
                            (String) data.get("name"),
                            getImageUrlForCharacter((String) data.get("name"))
                    ))
                    .toList();
        }
        return List.of();
    }

    public List<Character> searchCharacterByName(String name) {
        List<Character> cachedCharacters = characterRepository.findByName(name);
        if (!cachedCharacters.isEmpty()) {
            System.out.println("Fetching character '" + name + "' from Database...");
            return cachedCharacters;
        }

        System.out.println("Fetching character '" + name + "' from SWAPI API...");

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(BASE_URL + "?search=" + name, Map.class);

        if (response != null && response.containsKey("results")) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
            List<Character> characters = mapToCharacterList(results);

            for (Character character : characters) {
                boolean exists = characterRepository.findByName(character.getName()).stream().findFirst().isPresent();
                if (!exists) {
                    System.out.println("Saving '" + character.getName() + "' to Database...");
                    characterRepository.save(character);
                }
            }

            return characters;
        }

        return List.of();
    }

    private List<Character> mapToCharacterList(List<Map<String, Object>> results) {
        return results.stream()
                .map(this::mapToCharacter)
                .toList();
    }

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

    private String getImageUrlForCharacter(String name) {
        switch (name) {
            case "Luke Skywalker": return "https://starwars-visualguide.com/assets/img/characters/1.jpg";
            case "C-3PO": return "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRO3IO6mCc-DzOcpCWI6V0SNBxzeAq42Yq1Ow&s";
            case "R2-D2": return "https://i.pinimg.com/474x/84/41/b4/8441b4b44ec5c95c371c244d215056ad.jpg";
            case "Owen Lars": return "https://i.namu.wiki/i/95TjCsQ-OAxsCXuM5a-BAh14c3Mm95SKY-5JEMOQkPQjLMwNsKZaHvsYEciW0umb6CenhzNCByg2em1vJDKLXg.webp";
            case "Darth Vader": return "https://starwars-visualguide.com/assets/img/characters/4.jpg";
            case "Beru Whitesun lars": return "https://i.pinimg.com/736x/cd/55/1a/cd551a080b436e9b2a6c618321224de8.jpg";
            case "R5-D4": return "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTaqyT5RQ1c85EymM9JavmMW_R5PCn3MsIZyrP0KoeOMBL2R-GKIHD1lx_kMEwbUjfYSQE&usqp=CAU";
            case "Leia Organa": return "https://upload.wikimedia.org/wikipedia/en/1/1b/Princess_Leia%27s_characteristic_hairstyle.jpg";
            case "Biggs Darklighter": return "https://i.pinimg.com/564x/83/d0/b0/83d0b0dda0067f3aa0c651952d94e21a.jpg";
            case "Obi-Wan Kenobi": return "https://m.media-amazon.com/images/I/71wamZkWakL._AC_UF1000,1000_QL80_.jpg";
            default: return "https://starwars-visualguide.com/assets/img/placeholder.jpg";
        }
    }
}


