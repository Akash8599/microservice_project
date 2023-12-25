package com.hotel.service.HotelService.services;

import com.hotel.service.HotelService.entities.Hotel;

import java.util.List;

public interface HotelService {

    Hotel createHotel(Hotel hotel);

    List<Hotel> getAll();

    Hotel getHotel(String id);


}
