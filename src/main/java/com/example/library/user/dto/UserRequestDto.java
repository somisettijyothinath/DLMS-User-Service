package com.example.library.user.dto;

import com.example.library.user.entity.UserEntity;

public class UserRequestDto {
    
    private String email;
    private String password;
    private String name;
    private UserEntity.Role role;
    private String phone;
    private String address;
    private String studentId;
    private String profilePhoto;  // Base64 image
    private String photoFilename;
    private boolean status;

	// Constructors
    public UserRequestDto() {}

    // Getters & Setters (simple like catalog-service)
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public UserEntity.Role getRole() { return role; }
    public void setRole(UserEntity.Role role) { this.role = role; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getProfilePhoto() { return profilePhoto; }
    public void setProfilePhoto(String profilePhoto) { this.profilePhoto = profilePhoto; }

    public String getPhotoFilename() { return photoFilename; }
    public void setPhotoFilename(String photoFilename) { this.photoFilename = photoFilename; }
    
    public boolean isStatus() {	return status;	}
	public void setStatus(boolean status) {	this.status = status; }
}
