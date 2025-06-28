package com.example.MyMovieBox.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.MyMovieBox.Entity.MovieEntity;
import com.example.MyMovieBox.Entity.UserEntity;
import com.example.MyMovieBox.Service.MovieService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
public class MovieController {

    @Autowired
    private MovieService movieService;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserEntity user) {
        return movieService.SaveUser(user);
    }
    
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password) {
        return movieService.loginUser(username, password);
    }

    @PostMapping("/addMovie")
    public ResponseEntity<String> addMovie(@RequestBody MovieEntity movie, @RequestParam Long userId) {
    return movieService.addMovie(movie, userId);
   }

    @PostMapping("/updateMovie/{id}")
    public ResponseEntity<String> updateMovie(@PathVariable Long id, @RequestParam String status, @RequestParam String description) {
        return movieService.updateMovie(id, status, description);
    }
    @DeleteMapping("/deleteMovie/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok("Movie deleted successfully");
    }

    @GetMapping("/getMovie/{id}")
    public ResponseEntity<MovieEntity> getMovie(@PathVariable Long id) {
        MovieEntity movie = movieService.getMovieById(id);
        return ResponseEntity.ok(movie);
        }
    

}
