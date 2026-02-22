package com.example.library.user.dto;

public class LoginRequestDto {
    
    private String email;
    private String password;

    // Constructors
    public LoginRequestDto() {}

    // Getters & Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
