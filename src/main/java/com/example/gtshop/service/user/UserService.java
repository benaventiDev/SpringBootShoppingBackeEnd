package com.example.gtshop.service.user;

import com.example.gtshop.dto.UserDto;
import com.example.gtshop.exceptions.AlreadyExistsException;
import com.example.gtshop.exceptions.ResourceNotFoundException;
import com.example.gtshop.model.User;
import com.example.gtshop.repository.UserRepository;
import com.example.gtshop.request.user.CreateUserRequest;
import com.example.gtshop.request.user.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
    }

    @Override
    public User createUser(CreateUserRequest createUserRequest) {
        return Optional.of(createUserRequest).filter(
                user -> !userRepository.existsByEmail(createUserRequest.getEmail()))
                .map(request -> {
                    User user = new User();
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(createUserRequest.getPassword());
        user.setFirstName(createUserRequest.getFirstName());
        user.setLastName(createUserRequest.getLastName());
        return userRepository.save(user);
                }).orElseThrow(() -> new AlreadyExistsException(createUserRequest.getEmail() + " already exists!"));
    }

    @Override
    public User updateUser(UpdateUserRequest updateUserRequest, Long userId) {
        return userRepository.findById(userId).map(existingUser ->{
            existingUser.setFirstName(updateUserRequest.getFirstName());
            existingUser.setLastName(updateUserRequest.getLastName());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete, () -> {
            throw new ResourceNotFoundException("User with id " + userId + " not found");
        });
    }

    @Override
    public UserDto convertUserToDto(User user){
        System.out.println("total items: " + user.getCart().getItems().size());
        user.getCart().getItems().forEach(item -> {
            System.out.println("Item: " + item.getProduct().getName());
        });
        return modelMapper.map(user, UserDto.class);
    }
}
