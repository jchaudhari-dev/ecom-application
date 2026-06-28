package com.ecom.app.dto;

import com.ecom.app.model.UserRole;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
    private UserRole role;
    private AddressDTO address;
}
