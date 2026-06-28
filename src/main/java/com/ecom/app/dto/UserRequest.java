package com.ecom.app.dto;

import com.ecom.app.model.UserRole;
import lombok.Data;

@Data
public class UserRequest {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
    private AddressDTO address;
}
