package com.brightcode.mediadeck.models.dto;

import com.brightcode.mediadeck.models.Movie;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

public class MovieHelper {


    ArrayList<Movie> movies = new ArrayList<Movie>();


    public String getMoviesBySearch(String s){
        String url = "http://www.omdbapi.com/?apikey=b0901f52&s="+s;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }



    public String[] makeMovieList(String stringToParse) {
        String parsedString = getMoviesBySearch(stringToParse);
        parsedString = parsedString.substring(parsedString.indexOf("[")+1);
        System.out.println(parsedString);

        String[] unparsedMovies = parsedString.split("}",0);


        //remove everything before the imdb id
        for (int i = 0; i < unparsedMovies.length; i++){
            unparsedMovies[i] =  unparsedMovies[i].substring(unparsedMovies[i].indexOf("imdbID")+9);
        }

        //remove everything after the imdb id
        for (int i = 0; i < unparsedMovies.length; i++){
            unparsedMovies[i] =  unparsedMovies[i].substring(0, unparsedMovies[i].indexOf('"'));
            //System.out.println(unparsedMovies[i]);
        }

        String[] parsedMovies = Arrays.copyOf(unparsedMovies, unparsedMovies.length-1);
        //System.out.println("everything after this shouldn't contain extra keyword");


        for (int i = 0; i < parsedMovies.length; i++){
            //System.out.println(parsedMovies[i]);
        }

        return parsedMovies;
    }


}
