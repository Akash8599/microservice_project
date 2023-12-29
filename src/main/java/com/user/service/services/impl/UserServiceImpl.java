package com.user.service.services.impl;

import com.user.service.entities.Hotel;
import com.user.service.entities.Ratings;
import com.user.service.entities.User;
import com.user.service.exceptions.ResourceNotFoundException;
import com.user.service.external.services.HotelService;
import com.user.service.repositories.UserRepository;
import com.user.service.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private HotelService hotelService;

    @Override
    public User saveUser(User user) {

        //generate unique userId
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        List<User> users = userRepository.findAll();

        List<User> userList = users
                .stream()
                .peek(data ->{

                    Ratings[] arrayRatingsOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+ data.getUserId(), Ratings[].class);
                    List<Ratings> ratings = Arrays.stream(arrayRatingsOfUser).toList();

                    List<Ratings> ratingsList = ratings
                            .stream()
                            .map(rating -> {
                                ResponseEntity<Hotel> hotelResponseEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/ " + rating.getHotelId(), Hotel.class);
                                Hotel hotel = hotelResponseEntity.getBody();
                                logger.info("Response status code: {}",  hotelResponseEntity.getStatusCode());
                                rating.setHotel(hotel);
                                return rating;
                            }).collect(Collectors.toList());

                    data.setRatings(ratingsList);
                })
                .collect(Collectors.toList());
        logger.info("Retrieved {} users with ratings and hotels", userList.size());


        return userList;
    }

    @Override
    public User getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Resource not found on server"));

        //fetch rating of above user from rating service
        //http://localhost:8083/ratings/users/{userId}  - rating api
        Ratings[] arrayRatingsOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+ user.getUserId(), Ratings[].class);
        List<Ratings> ratings = Arrays.stream(arrayRatingsOfUser).toList();

        //http://localhost:8082/hotels
        List<Ratings> ratingsList = ratings
                .stream()
                .map(rating -> {

//                    <---Using Rest Template-->
//                    ResponseEntity<Hotel> hotelResponseEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/ " + rating.getHotelId(), Hotel.class);
//                    Hotel hotel = hotelResponseEntity.getBody();
//                    logger.info("Response status code: {}",  hotelResponseEntity.getStatusCode());

//                  <--using Feign client-->
                    Hotel hotel = hotelService.getHotel(rating.getHotelId());
                    logger.info("Hotel details fetched using feign client: {}", hotel.toString());
                    rating.setHotel(hotel);
                    return rating;

                }).collect(Collectors.toList());

        logger.info("Array List details : {}", ratingsList);
        user.setRatings(ratingsList);

        return user;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public void delete(String userId) {
        userRepository.deleteById(userId);
        logger.info("User with ID {} has been deleted", userId);

    }
}
