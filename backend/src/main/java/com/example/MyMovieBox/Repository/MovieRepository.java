package com.example.MyMovieBox.Repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.MyMovieBox.Entity.MovieEntity;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {

    Optional<MovieEntity> findByStatus(String status);

    List<MovieEntity> findAllByStatusIn(List<String> status);

    List<MovieEntity> findByUserIdAndStatusIn(Long userId, List<String> statuses);

}
