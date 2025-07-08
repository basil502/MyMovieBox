package com.example.MyMovieBox.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.MyMovieBox.Entity.MovieEntity;
import com.example.MyMovieBox.Entity.UserEntity;
import com.example.MyMovieBox.Repository.MovieRepository;
import com.example.MyMovieBox.Repository.UserRepository;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<String> SaveUser(UserEntity user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    public Map<String, Object> loginUser(String username, String password) {
        Optional<UserEntity> optionalUser = userRepository.findByUsernameAndPassword(username, password);

        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            Map<String, Object> map = new HashMap<>();
            map.put("userId", user.getId());
            map.put("username", user.getUsername());
            return map;
        } else {
            throw new RuntimeException("Invalid username or password");
        }
    }

    public ResponseEntity<String> addMovie(MovieEntity movie) {
        movieRepository.save(movie);
        return ResponseEntity.ok("Movie added successfully");
    }

    public MovieEntity getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
    }

    public ResponseEntity<String> updateMovie(Long id, String status, String description) {
        MovieEntity movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));

        movie.setStatus(status);
        movie.setDescription(description);
        movieRepository.save(movie);

        return ResponseEntity.ok("Movie updated successfully");
    }

    public List<MovieEntity> getAllMovies() {
        return movieRepository.findAll();
    }

    public List<MovieEntity> getMoviesByReview(List<String> status) {
        return movieRepository.findAllByStatusIn(status);
    }

    public List<MovieEntity> getMoviesByStatusForUser(Long userId, List<String> statuses) {
        return movieRepository.findByUserIdAndStatusIn(userId, statuses);
    }

    public List<MovieEntity> getCompletedMovies(Long userId, List<String> statuses) {
        return movieRepository.findByUserIdAndStatusIn(userId, statuses);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

}
