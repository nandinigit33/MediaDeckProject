package com.brightcode.mediadeck.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Movie extends AbstractEntity{



    private int search_count;

    @OneToMany(
            mappedBy = "movie",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final List<Review> reviewsList = new ArrayList<>();

    @ManyToMany(mappedBy = "favoriteMovies")
    private Set<AppUser> favoriteUsers = new HashSet<>();


    @ManyToMany(mappedBy = "toWatchMovies")
    private Set<AppUser> toWatchUsers = new HashSet<>();


    public Movie() {
    }

    public Movie(String name, int search_count) {
        super();
        this.name = name;
        this.search_count = search_count;
    }


    //adding in code that was originally in apimovie class

    private String name;
    private String title;
    private String director;
    private String plot;
    private String poster;
    private String year;
    private String apiID;

    private int viewCount;


    public String getMovieByName(String t) {
        String url = "http://www.omdbapi.com/?apikey=b0901f52&t="+t;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }

    public String getMovieById(String i){
        String url = "http://www.omdbapi.com/?apikey=b0901f52&i="+i;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }





    public String getMoviesBySearch(String s){
        String url = "http://www.omdbapi.com/?apikey=b0901f52&s="+s;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }

    public void setMovieInfoByName(String t) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(this.getMovieByName(t));

        if (node.get("Title")!=null) {
            this.name = node.get("Title").asText();
            this.director = node.get("Director").asText();
            this.plot = node.get("Plot").asText();
            this.poster = node.get("Poster").asText();
            this.year = node.get("Year").asText();
            this.apiID = node.get("imdbID").asText();
        }
    }

    public void setMovieInfoById(String i) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(this.getMovieById(i));

        if (node.get("Title")!=null) {
            this.name = node.get("Title").asText();
            this.director = node.get("Director").asText();
            this.plot = node.get("Plot").asText();
            this.poster = node.get("Poster").asText();
            this.year = node.get("Year").asText();
            this.apiID = node.get("imdbID").asText();
        }
    }


    //end of code that was originally in apimovie class

    public void userView(){
        this.viewCount++;
    }

    public String getName() {
        return name;
    }

    //leave this in to keep display working properly
    public String getTitle() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSearch_count() {
        return search_count;
    }

    public void setSearch_count(int search_count) {
        this.search_count = search_count;
    }

    public List<Review> getReviewsList() {
        return reviewsList;
    }

    public Set<AppUser> getFavUsers() {
        return favoriteUsers;
    }

    public Set<AppUser> getToWatchUsers() {
        return toWatchUsers;
    }

    public void setFavoriteUsers(Set<AppUser> favoriteUsers){
        this.favoriteUsers = favoriteUsers;
    }

    public void setToWatchUsers(Set<AppUser> toWatchUsers){
        this.toWatchUsers = toWatchUsers;
    }

    public void addFavoriteUser(AppUser user){
        this.favoriteUsers.add(user);
    }

    public void addToWatchUser(AppUser user) {
        this.toWatchUsers.add(user);
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getApiID() {
        return apiID;
    }

    public void setApiID(String apiID) {
        this.apiID = apiID;
    }

    @Override
    public String toString() {
        return name;
    }
}

