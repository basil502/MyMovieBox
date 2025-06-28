package com.example.MyMovieBox.Service;

import org.springframework.beans.factory.annotation.Autowired;
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
        userRepository.save(user);
        return ResponseEntity.ok("User added successfully");
    }
    public ResponseEntity<String> loginUser(String username, String password) {
        UserEntity user = userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        return ResponseEntity.ok("Login successful");
    }



public ResponseEntity<String> addMovie(MovieEntity movie, Long userId) {
    UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

    movie.setUser(user); // Associate user as foreign key
    movieRepository.save(movie); // Save movie

    return ResponseEntity.ok("Movie added successfully by user ID: " + userId);
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
    
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }



}
