package com.brightcode.mediadeck.models.dto;

import com.brightcode.mediadeck.models.AppUser;
import com.brightcode.mediadeck.models.Movie;
import jakarta.validation.constraints.NotNull;

public class UserMovieDTO {

    @NotNull
    private AppUser user;

    @NotNull
    private Movie movie;

    public UserMovieDTO() {
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
