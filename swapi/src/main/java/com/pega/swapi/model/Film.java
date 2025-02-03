package com.pega.swapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "films")
public class Film {
    @Id
    private String id;
    private String title;
    private String director;
    private String producer;
    private String releaseDate;
    private String imageUrl;

    // Default constructor
    public Film() {}

    // Constructor for Title & ImageUrl (for getAllFilms())
    public Film(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    // Full Constructor (for search)
    public Film(String title, String imageUrl, String director, String producer, String releaseDate) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.director = director;
        this.producer = producer;
        this.releaseDate = releaseDate;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
