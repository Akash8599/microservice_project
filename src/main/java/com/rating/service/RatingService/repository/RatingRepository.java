package com.rating.service.RatingService.repository;

import com.rating.service.RatingService.entities.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RatingRepository extends MongoRepository<Rating, String> {

    //custom finder method

    List<Rating> findByUserId(String userId);
    List<Rating> findByHotelId(String hotelId);
}
