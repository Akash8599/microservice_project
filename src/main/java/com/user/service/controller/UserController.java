package com.user.service.controller;

import com.user.service.entities.User;
import com.user.service.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User createdUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);

    }

    @GetMapping("/{userId}")
    @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId){
       User user = userService.getUser(userId);

       return ResponseEntity.ok(user);
    }


    @GetMapping
    // circuit breaker method
//    @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelsFallback")
    //Retry method
//    @Retry(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelsFallback")
    // Rate limiter
    @RateLimiter(name = "userRateLimiter", fallbackMethod = "ratingHotelsFallback")
    public ResponseEntity<List<User>> getAllUser(){
//        int retryCount = 0;
        logger.info("Get all User handler: User Controller");
//        retryCount++;
//        logger.info("Retry Count: {}", retryCount);

        List<User> users = userService.getAllUser();

        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable String userId){
        userService.delete(userId);
    }
    //creating fall back method for circuit breaker
    public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex){
        logger.info("Fallback is executed because service is down : {}", ex.getMessage());
        User user = User.builder()
                .email("dummy@gmail.com")
                .name("Dummy")
                .about("This user is created dummy because service is down")
                .userId("12345")
                .build();
        return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<User>> ratingHotelsFallback( Exception ex){

        ex.printStackTrace();
        logger.info("Fallback is executed because service is down : {}", ex.getMessage());
        User user = User.builder()
                .email("dummy@gmail.com")
                .name("Dummy")
                .about("This user is created dummy because service is down")
                .userId("12345")
                .build();
        List<User> list = new ArrayList<>();
        list.add(user);
        return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
    }
}
