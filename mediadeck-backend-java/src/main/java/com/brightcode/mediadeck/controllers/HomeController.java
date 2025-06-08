package com.brightcode.mediadeck.controllers;

import com.brightcode.mediadeck.data.AppUserRepository;
import com.brightcode.mediadeck.data.MovieRepository;
import com.brightcode.mediadeck.data.ReviewRepository;
import com.brightcode.mediadeck.models.AppUser;
import com.brightcode.mediadeck.models.Movie;
import com.brightcode.mediadeck.models.Review;
import com.brightcode.mediadeck.movie_rec.Movie_rec;
import com.brightcode.mediadeck.service.PrincipalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class HomeController {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    private PrincipalService principalService;


    @GetMapping("/home")
    public ResponseEntity<Map<String, Object>> getHomeData() throws JsonProcessingException {
        List<String> listOfApiIds = movieRepository.getTopMovies();

        List<Movie> movies = new ArrayList<>();
        for (String apiId : listOfApiIds) {
            Movie movie = new Movie();
            movie.setMovieInfoById(apiId);
            movies.add(movie);
        }

        List<Review> topReviews = reviewRepository.findTopFour();

        Map<String, Object> response = new HashMap<>();
        response.put("movies", movies);
        response.put("reviewsTopFour", topReviews);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<AppUser> getProfile() {
        AppUser user = principalService.getPrincipal();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/profile/recommendations")
    public ResponseEntity<?> getRecommendations() throws IOException {
        AppUser user = principalService.getPrincipal();
        int userId = user.getId();

        Movie_rec mr = new Movie_rec();
        mr.runFromJava();

        List<String> fullStrings = mr.convertCsvToStrings();
        List<String> strippedApis = fullStrings.stream()
                .map(s -> mr.returnAPiOfUser(userId, s))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<Movie> movies = new ArrayList<>();
        for (String api : strippedApis) {
            String searchable = mr.makeApiSearchable(api);
            Movie movie = new Movie();
            movie.setMovieInfoById(searchable);
            movies.add(movie);
        }

        if (movies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("No recommendations found. Rate or like more movies!");
        }

        return ResponseEntity.ok(movies);
    }

    @GetMapping("/search")
    public ResponseEntity<String> search() {
        return ResponseEntity.ok("Search for movies ");
    }

    @GetMapping("/movie-view/{apiId}")
    public ResponseEntity<Movie> getMovieByApiId(@PathVariable String apiId) throws JsonProcessingException {
        Movie movie = new Movie();
        movie.setMovieInfoById(apiId);

        if (movie.getPlot() != null) {
            Optional<Movie> opt = movieRepository.findByApiID(movie.getApiID());

            Movie savedMovie = opt.map(existing -> {
                existing.userView();
                return movieRepository.save(existing);
            }).orElseGet(() -> {
                movie.userView();
                return movieRepository.save(movie);
            });

            return ResponseEntity.ok(savedMovie);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/profile/movie-view/{apiId}")
    public ResponseEntity<Movie> getProfileMovieByApiId(@PathVariable String apiId) throws JsonProcessingException {
        Movie movie = new Movie();
        movie.setMovieInfoById(apiId);

        if (movie.getPlot() != null) {
            Optional<Movie> opt = movieRepository.findByApiID(movie.getApiID());

            Movie savedMovie = opt.map(existing -> {
                existing.userView();
                return movieRepository.save(existing);
            }).orElseGet(() -> {
                movie.userView();
                return movieRepository.save(movie);
            });

            return ResponseEntity.ok(savedMovie);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
