package com.example.MyMovieBox.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.MyMovieBox.Entity.MovieEntity;
import com.example.MyMovieBox.Entity.UserEntity;
import com.example.MyMovieBox.Service.MovieService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@CrossOrigin(origins = "*")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserEntity user) {
        return movieService.SaveUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        try {
            Map<String, Object> result = movieService.loginUser(username, password);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/addMovie")
    public ResponseEntity<String> addMovie(@RequestBody MovieEntity movie) {
        return movieService.addMovie(movie);
    }

    @PostMapping("/updateMovie/{id}")
    public ResponseEntity<String> updateMovie(
            @PathVariable Long id,
            @RequestBody Map<String, String> data) {
        String status = data.get("status");
        String description = data.get("description");
        return movieService.updateMovie(id, status, description);
    }

    @DeleteMapping("/deleteMovie/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id); // throws exception if not found
        return ResponseEntity.ok("Movie deleted successfully.");
    }

    @GetMapping("/getMoviesReview")
    public List<MovieEntity> getMoviesReview() {
        return movieService.getMoviesByReview(List.of("Completed"));
    }

    @GetMapping("/getMoviesByStatus")
    public List<MovieEntity> getMoviesByStatus(
            @RequestParam Long userId) {
        // statuses hard‑coded here, or you could accept as parameter too
        return movieService.getMoviesByStatusForUser(
                userId,
                List.of("To Watch", "In Progress"));
    }

    @GetMapping("/getCompletedMovies")
    public List<MovieEntity> getCompletedMovies(@RequestParam Long userId) {
        return movieService.getMoviesByStatusForUser(
                userId,
                List.of("Completed"));
    }

    @GetMapping("/getMovie/{id}")
    public ResponseEntity<MovieEntity> getMovie(@PathVariable Long id) {
        MovieEntity movie = movieService.getMovieById(id);
        return ResponseEntity.ok(movie);
    }

}
