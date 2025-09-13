package com.bms.service;


import com.bms.dto.TheaterDTO;
import com.bms.exception.ResourceNotFoundException;
import com.bms.models.Theater;
import com.bms.repo.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class TheaterService {

    @Autowired
    private TheaterRepository theaterRepository;

    public TheaterDTO createTheater(TheaterDTO theaterDTO){
        Theater theater= mapToEntity(theaterDTO);
        Theater savedTheater= theaterRepository.save(theater);
        return mapToDto(savedTheater);
    }

    public TheaterDTO getTheaterById(Long id){
        Theater theater= theaterRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Theater Not Found By id : "+id));
        return mapToDto(theater);
    }

    public List<TheaterDTO> getByCity(String city){
        List<TheaterDTO> theaters= theaterRepository.findByCity(city).stream()
                .map(theater -> {
                    return mapToDto(theater);
                }).collect(Collectors.toList());
        return theaters;
    }

    public List<TheaterDTO> getAllTheaters(){
        List<TheaterDTO> theaters= theaterRepository.findAll().stream()
                .map(theater -> {
                    return mapToDto(theater);
                }).collect(Collectors.toList());
        return theaters;
    }

    private TheaterDTO mapToDto(Theater theater) {
        TheaterDTO theaterDto=new TheaterDTO();
        theaterDto.setId(theater.getId());
        theaterDto.setName(theater.getName());
        theaterDto.setCity(theater.getCity());
        theaterDto.setAddress(theater.getAddress());
        theaterDto.setTotalScreens(theater.getTotalScreens());
        return theaterDto;
    }

    public Theater mapToEntity(TheaterDTO theaterDTO){
        Theater theater= new Theater();
        theater.setName(theaterDTO.getName());
        theater.setAddress(theaterDTO.getAddress());
        theater.setCity(theaterDTO.getCity());
        theater.setTotalScreens(theaterDTO.getTotalScreens());
        return theater;
    }
}
