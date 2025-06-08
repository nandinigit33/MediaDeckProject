package com.brightcode.mediadeck.data;

import com.brightcode.mediadeck.models.Movie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Integer> {

    Optional<Movie> findByApiID(String apiID);

    @Query(value ="SELECT apiid FROM movie ORDER BY view_count DESC LIMIT 5",nativeQuery = true)
    List<String> getTopMovies();

}

