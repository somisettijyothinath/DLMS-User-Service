package com.example.library.user.dto;

import com.example.library.user.entity.UserEntity;

public class UserResponseDto {
    
    private Long id;
    private String email;
    private String name;
    private UserEntity.Role role;
    private String phone;
    private String address;
    private String studentId;
    private String profilePhoto;
    private String photoFilename;
    private boolean active;

    // Constructor (for mapping)
    public UserResponseDto(Long id, String email, String name, UserEntity.Role role, 
                          String phone, String address, String studentId,
                          String profilePhoto, String photoFilename, boolean active) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.address = address;
        this.studentId = studentId;
        this.profilePhoto = profilePhoto;
        this.photoFilename = photoFilename;
        this.active = active;
    }

    // Getters only (response - no setters needed)
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public UserEntity.Role getRole() { return role; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getStudentId() { return studentId; }
    public String getProfilePhoto() { return profilePhoto; }
    public String getPhotoFilename() { return photoFilename; }
    public boolean isActive() { return active; }
}
