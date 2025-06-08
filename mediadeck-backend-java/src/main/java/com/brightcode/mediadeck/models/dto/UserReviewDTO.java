package com.brightcode.mediadeck.models.dto;

import com.brightcode.mediadeck.models.AppUser;
import com.brightcode.mediadeck.models.Movie;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserReviewDTO {

    @NotNull
    private AppUser user;

    @NotNull
    private Movie movie;


//    @NotNull
////    @Valid
//    private Review review;

    @NotBlank(message = "Review cannot be empty")
    @Size(max = 500, message = "Review cannot exceed 500 characters")
    private String review_text;

    @NotNull
    private int star_rating;

    public UserReviewDTO() {
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

//    public Review getReview() {
//        return review;
//    }
//
//    public void setReview(Review review) {
//        this.review = review;
//    }


    public String getReview_text() {
        return review_text;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }

    public int getStar_rating() {
        return star_rating;
    }

    public void setStar_rating(int star_rating) {
        this.star_rating = star_rating;
    }
}
