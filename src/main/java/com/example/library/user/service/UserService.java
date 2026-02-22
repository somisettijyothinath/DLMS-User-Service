package com.example.library.user.service;

import com.example.library.user.dto.*;

import java.util.List;

public interface UserService {
    
    UserResponseDto createUser(UserRequestDto requestDto);
    UserResponseDto getUserById(Long id);
    List<UserResponseDto> getAllUsers();
    UserResponseDto updateUser(Long id, UserRequestDto requestDto);
    void deleteUser(Long id);
    UserResponseDto login(LoginRequestDto loginDto);
    List<UserResponseDto> getActiveStudents();
    List<UserResponseDto> searchUsersByName(String name);
    Long countUsersByRole(String role);
}
