package com.hotel.service.HotelService.services;

import com.hotel.service.HotelService.entities.Hotel;
import com.hotel.service.HotelService.exceptions.ResourceNotFoundException;
import com.hotel.service.HotelService.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelServiceImpl implements HotelService{

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
        return hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Data not found"));
    }
}
