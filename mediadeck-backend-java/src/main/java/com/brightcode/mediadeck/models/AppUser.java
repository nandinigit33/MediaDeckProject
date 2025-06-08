package com.brightcode.mediadeck.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.ui.context.Theme;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class AppUser extends AbstractEntity {

    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private String role;

    @NotNull
    private boolean enabled;

    private String verificationCode;

    private Theme theme;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final List<Review> reviewsList = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_favourite_movies",
            joinColumns = @JoinColumn(name = "favorite_user_id",referencedColumnName = "id") ,
            inverseJoinColumns = @JoinColumn(name="favorite_movie_id",referencedColumnName = "id")
    )
    private Set<Movie> favoriteMovies = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_to_watch_movies",
            joinColumns = @JoinColumn(name = "to_watch_user_id",referencedColumnName = "id") ,
            inverseJoinColumns = @JoinColumn(name="to_watch_movie_id",referencedColumnName = "id")
    )
    private Set<Movie> toWatchMovies = new HashSet<>();

    public AppUser() {}

    public AppUser(String username, String email, String password, String role, boolean enabled, String verificationCode, Theme theme) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
        this.verificationCode = verificationCode;
        this.theme = theme;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() { return enabled; }

    public void setEnabled(boolean isEnabled) { this.enabled = isEnabled; }

    public void setVerificationCode(String verificationCode) { this.verificationCode = verificationCode; }

    public String getVerificationCode() { return verificationCode; }

    public List<Review> getReviewsList() {
        return reviewsList;
    }

    public Set<Movie> getFavoriteMovies() {
        return favoriteMovies;
    }

    public Set<Movie> getToWatchMovies() {
        return toWatchMovies;
    }

    public void setFavoriteMovies(Set<Movie> favoriteMovies){
        this.favoriteMovies = favoriteMovies;
    }

    public void setToWatchMovies(Set<Movie> toWatchMovies){
        this.toWatchMovies = toWatchMovies;
    }

    public void addFavoriteMovies(Movie movie) {
        this.favoriteMovies.add(movie);
    }

    public void addToWatchMovies(Movie movie) {
        this.toWatchMovies.add(movie);
    }


    public void removeFavoriteMovie(Movie movie){
        if (favoriteMovies.contains(movie)) {
            this.favoriteMovies.remove(movie);
        }
    }

    public void removeToWatchMovie(Movie movie){
        if (toWatchMovies.contains(movie)) {
            this.toWatchMovies.remove(movie);
        }
    }



    public Theme getTheme() { return theme; }

    public void setTheme(Theme theme) { this.theme = theme; }

}
