package com.example.gtshop.service.user;

import com.example.gtshop.dto.UserDto;
import com.example.gtshop.model.User;
import com.example.gtshop.request.user.CreateUserRequest;
import com.example.gtshop.request.user.UpdateUserRequest;

public interface IUserService {

    User getUserById(Long userId);
    User createUser(CreateUserRequest createUserRequest);
    User updateUser(UpdateUserRequest updateUserRequest, Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);
}
