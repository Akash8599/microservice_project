package com.user.service.external.services;

import com.user.service.entities.Ratings;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@FeignClient(name = "RATING-SERVICE")
public interface RatingService {

    //post request
    @PostMapping("/ratings")
    public Ratings createRating(Ratings values);

    //put method
    // we can use a response entity return type as well
    @PutMapping("/ratings/{ratingId}")
    ResponseEntity<Ratings> updateRating(@PathVariable("ratingId") String ratingId, Ratings ratings);
}
