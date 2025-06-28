package com.example.MyMovieBox.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.MyMovieBox.Entity.MovieEntity;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {
    // Custom query methods (if needed) can be defined here

    
}
