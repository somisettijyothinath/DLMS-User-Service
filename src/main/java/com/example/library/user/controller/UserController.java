package com.example.library.user.controller;

import com.example.library.user.dto.*;
import com.example.library.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")  // For frontend testing
public class UserController {

    @Autowired
    private UserService userService;

    // CREATE - POST /user/api/users
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto requestDto) {
        UserResponseDto createdUser = userService.createUser(requestDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // READ - GET /user/api/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // READ ALL - GET /user/api/users
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // UPDATE - PUT /user/api/users/{id}
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, 
                                                    @RequestBody UserRequestDto requestDto) {
        UserResponseDto updatedUser = userService.updateUser(id, requestDto);
        return ResponseEntity.ok(updatedUser);
    }

    // DELETE - DELETE /user/api/users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // LOGIN - POST /user/api/users/login
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody LoginRequestDto loginDto) {
        UserResponseDto user = userService.login(loginDto);
        return ResponseEntity.ok(user);
    }

    // LIBRARIAN DASHBOARD - GET /user/api/users/students/active
    @GetMapping("/students/active")
    public ResponseEntity<List<UserResponseDto>> getActiveStudents() {
        List<UserResponseDto> students = userService.getActiveStudents();
        return ResponseEntity.ok(students);
    }

    // SEARCH - GET /user/api/users/search?name=john
    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> searchUsersByName(@RequestParam String name) {
        List<UserResponseDto> users = userService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/count/students")
    public ResponseEntity<Long> countStudents() {
		Long count = userService.countUsersByRole("STUDENT");
		return ResponseEntity.ok(count);
	}
    
    @GetMapping("/count/librarians")
    public ResponseEntity<Long> countLibrarians() {
		Long count = userService.countUsersByRole("LIBRARIAN");
		return ResponseEntity.ok(count);
	}
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponseDto> updateUserStatus(@PathVariable Long id, @RequestParam boolean status) {
		UserRequestDto requestDto = new UserRequestDto();
		requestDto.setStatus(status);
		UserResponseDto updatedUser = userService.updateUser(id, requestDto);
		return ResponseEntity.ok(updatedUser);
	}
}
