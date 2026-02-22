package com.example.library.user.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.library.user.dto.LoginRequestDto;
import com.example.library.user.dto.UserRegisteredEvent;
import com.example.library.user.dto.UserRequestDto;
import com.example.library.user.dto.UserResponseDto;
import com.example.library.user.entity.UserEntity;
import com.example.library.user.exception.AppException;
import com.example.library.user.repository.UserRepository;
import com.example.library.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;


//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto createUser(UserRequestDto requestDto) {
        // Validate required fields
        if (requestDto.getEmail() == null || requestDto.getEmail().trim().isEmpty()) {
            throw new AppException(400, "Email is required");
        }
        if (requestDto.getPassword() == null || requestDto.getPassword().isEmpty()) {
            throw new AppException(400, "Password is required");
        }
        if (requestDto.getName() == null || requestDto.getName().trim().isEmpty()) {
            throw new AppException(400, "Name is required");
        }
        if (requestDto.getRole() == null) {
            throw new AppException(400, "Role is required");
        }
        
        // Check email uniqueness
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new AppException(409, "Email already exists: " + requestDto.getEmail());
        }

        UserEntity user = new UserEntity();
        mapRequestToEntity(requestDto, user);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setActive(true);

        UserEntity savedUser = userRepository.save(user);
        
        UserRegisteredEvent event = new UserRegisteredEvent(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );

        kafkaTemplate.send("user-registered", event);
        
        return mapEntityToResponse(savedUser);
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        if (id == null || id <= 0) {
            throw new AppException(400, "Invalid user ID: " + id);
        }
        
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(404, "User not found with id: " + id));
        return mapEntityToResponse(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto requestDto) {
        if (id == null || id <= 0) {
            throw new AppException(400, "Invalid user ID: " + id);
        }
        
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(404, "User not found with id: " + id));

        // Partial update - only update provided fields
        updateEntityFields(user, requestDto);
        
        // Check email change conflict
        if (requestDto.getEmail() != null && !requestDto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(requestDto.getEmail())) {
                throw new AppException(409, "Email already exists: " + requestDto.getEmail());
            }
            user.setEmail(requestDto.getEmail());
        }

        UserEntity updatedUser = userRepository.save(user);
        return mapEntityToResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (id == null || id <= 0) {
            throw new AppException(400, "Invalid user ID: " + id);
        }
        
        if (!userRepository.existsById(id)) {
            throw new AppException(404, "User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDto login(LoginRequestDto loginDto) {
        if (loginDto.getEmail() == null || loginDto.getEmail().trim().isEmpty()) {
            throw new AppException(400, "Email is required for login");
        }
        if (loginDto.getPassword() == null || loginDto.getPassword().isEmpty()) {
            throw new AppException(400, "Password is required for login");
        }
        
        UserEntity user = userRepository.findActiveUserByEmail(loginDto.getEmail())
                .orElseThrow(() -> new AppException(404, "Invalid email or inactive user"));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new AppException(401, "Invalid password");
        }
        
        if (user.isActive() == false) {
            throw new AppException(403, "User is Inactive please contact Head Of Department");
        }

        return mapEntityToResponse(user);
    }

    @Override
    public List<UserResponseDto> getActiveStudents() {
        return userRepository.findByRoleAndActive(UserEntity.Role.STUDENT, true).stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> searchUsersByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new AppException(400, "Name parameter cannot be empty");
        }
        return userRepository.searchActiveUsersByName(name.trim()).stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    // Update entity fields selectively
    private void updateEntityFields(UserEntity user, UserRequestDto dto) {
        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            user.setName(dto.getName().trim());
        }
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone().trim());
        }
        if (dto.getAddress() != null) {
            user.setAddress(dto.getAddress().trim());
        }
        if (dto.getStudentId() != null) {
            user.setStudentId(dto.getStudentId().trim());
        }
        if (dto.getProfilePhoto() != null) {
            user.setProfilePhoto(dto.getProfilePhoto());
        }
        if (dto.getPhotoFilename() != null) {
            user.setPhotoFilename(dto.getPhotoFilename().trim());
        }
        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }
        if (dto.isStatus() != user.isActive()) {
            user.setActive(dto.isStatus());
        }
    }
    
   public Long countUsersByRole(String role) {
		return userRepository.countByRole(UserEntity.Role.valueOf(role));
	}

    // Map RequestDto to Entity (create)
    private void mapRequestToEntity(UserRequestDto dto, UserEntity entity) {
        entity.setEmail(dto.getEmail().trim());
        entity.setName(dto.getName().trim());
        entity.setRole(dto.getRole());
        if (dto.getPhone() != null) entity.setPhone(dto.getPhone().trim());
        if (dto.getAddress() != null) entity.setAddress(dto.getAddress().trim());
        if (dto.getStudentId() != null) entity.setStudentId(dto.getStudentId().trim());
        if (dto.getProfilePhoto() != null) entity.setProfilePhoto(dto.getProfilePhoto());
        if (dto.getPhotoFilename() != null) entity.setPhotoFilename(dto.getPhotoFilename().trim());
    }

    // Map Entity to ResponseDto
    private UserResponseDto mapEntityToResponse(UserEntity entity) {
        return new UserResponseDto(
            entity.getId(),
            entity.getEmail(),
            entity.getName(),
            entity.getRole(),
            entity.getPhone(),
            entity.getAddress(),
            entity.getStudentId(),
            entity.getProfilePhoto(),
            entity.getPhotoFilename(),
            entity.isActive()
        );
    }
}
