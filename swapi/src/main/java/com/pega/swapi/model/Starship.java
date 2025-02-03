package com.pega.swapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "starships")
public class Starship {
    @Id
    private String name;
    private String model;
    private String manufacturer;
    private String costInCredits;
    private String length;
    private String maxAtmospheringSpeed;
    private String crew;
    private String passengers;
    private String cargoCapacity;
    private String starshipClass;
    private String imageUrl; // Added Image URL

    // Default Constructor
    public Starship() {}

    // Constructor for Name & Image
    public Starship(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    // Constructor with all details
    public Starship(String name, String model, String manufacturer, String costInCredits, String length,
                    String maxAtmospheringSpeed, String crew, String passengers, String cargoCapacity,
                    String starshipClass, String imageUrl) {
        this.name = name;
        this.model = model;
        this.manufacturer = manufacturer;
        this.costInCredits = costInCredits;
        this.length = length;
        this.maxAtmospheringSpeed = maxAtmospheringSpeed;
        this.crew = crew;
        this.passengers = passengers;
        this.cargoCapacity = cargoCapacity;
        this.starshipClass = starshipClass;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public String getCostInCredits() { return costInCredits; }
    public void setCostInCredits(String costInCredits) { this.costInCredits = costInCredits; }

    public String getLength() { return length; }
    public void setLength(String length) { this.length = length; }

    public String getMaxAtmospheringSpeed() { return maxAtmospheringSpeed; }
    public void setMaxAtmospheringSpeed(String maxAtmospheringSpeed) { this.maxAtmospheringSpeed = maxAtmospheringSpeed; }

    public String getCrew() { return crew; }
    public void setCrew(String crew) { this.crew = crew; }

    public String getPassengers() { return passengers; }
    public void setPassengers(String passengers) { this.passengers = passengers; }

    public String getCargoCapacity() { return cargoCapacity; }
    public void setCargoCapacity(String cargoCapacity) { this.cargoCapacity = cargoCapacity; }

    public String getStarshipClass() { return starshipClass; }
    public void setStarshipClass(String starshipClass) { this.starshipClass = starshipClass; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
