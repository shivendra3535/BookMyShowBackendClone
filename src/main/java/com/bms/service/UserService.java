package com.bms.service;


import com.bms.dto.UserDTO;
import com.bms.exception.ResourceNotFoundException;
import com.bms.models.User;
import com.bms.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserDTO createUser(UserDTO userDto)
    {
        User user=mapToEntity(userDto);
        User savedUser=userRepository.save(user);
        return mapToDto(savedUser);
    }

    public UserDTO getUserById(Long id)
    {
        User user=userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Use not found with id: "+id));
        return mapToDto(user);
    }

    public List<UserDTO> getAllUsers()
    {
        List<User> users=userRepository.findAll();
        return users.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    //update user
    public UserDTO updateUser(Long id, UserDTO userDTO){
        User user= userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User Not Found With the Id :"+id));
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhone());
        User savedUser= userRepository.save(user);
        return mapToDto(user);
    }


    //delete user

    public void deleteUser(Long id){
        User user= userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User Not Found With the Id :"+id));

        userRepository.delete(user);
    }

    private User mapToEntity(UserDTO userDto) {
        User user=new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPhoneNumber(userDto.getPhone());
        return user;
    }

    private UserDTO mapToDto(User user)
    {
        UserDTO userDto=new UserDTO();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhoneNumber());
        return userDto;

    }
}
