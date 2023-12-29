package com.rating.service.RatingService.controller;

import com.rating.service.RatingService.entities.Rating;
import com.rating.service.RatingService.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This is the RatingController class which handles the HTTP requests related to ratings.
 */


@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    /**
     * Creates a new rating.
     * Requires the user to have 'Admin' authority.
     *
     * @param rating The rating to be created.
     * @return The created rating.
     */

    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping
    public ResponseEntity<Rating> create(@RequestBody Rating rating){
        return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.create(rating));
    }

    /**
     * Retrieves all ratings.
     *
     * @return A list of all ratings.
     */
    @GetMapping
    public ResponseEntity<List<Rating>> getRatings(){
        return ResponseEntity.ok(ratingService.getAllRatings());
    }

    /**
     * Retrieves ratings by user ID.
     *
     * @param userId The ID of the user.
     * @return A list of ratings associated with the user ID.
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Rating>> getRatingsByUserId(@PathVariable String userId){
        return ResponseEntity.ok(ratingService.getRatingByUserId(userId));
    }


    /**
     * Retrieves ratings by hotel ID.
     *
     * @param hotelId The ID of the hotel.
     * @return A list of ratings associated with the hotel ID.
     */
    @GetMapping("/hotels/{hotelId}")
    public ResponseEntity<List<Rating>> getRatingsByHotelId(@PathVariable String hotelId){
        return ResponseEntity.ok(ratingService.getRatingByHotelId(hotelId));
    }

}
