package com.hotel.service.HotelService.services;

import com.hotel.service.HotelService.entities.Hotel;
import com.hotel.service.HotelService.exceptions.ResourceNotFoundException;
import com.hotel.service.HotelService.repositories.HotelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelServiceImpl implements HotelService{

    Logger logger = LoggerFactory.getLogger(HotelServiceImpl.class);

    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    public List<Hotel> getAll() {
        List<Hotel> hotels = hotelRepository.findAll();
        return hotels;
    }

    @Override
    public Hotel getHotel(String id) {
        logger.info("Retrieved hotel for given hotel id : {}", id);
        return hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Data not found"));
    }
}
