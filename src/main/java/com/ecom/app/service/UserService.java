package com.ecom.app.service;

import com.ecom.app.dto.AddressDTO;
import com.ecom.app.dto.UserRequest;
import com.ecom.app.dto.UserResponse;
import com.ecom.app.model.Address;
import com.ecom.app.model.User;
import com.ecom.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

//    private final List<User> userList = new ArrayList<>();
//    private Long nextId=1L;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public List<UserResponse> fetchAllUsers(){
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(this::mapTOUserResponse)
                .toList();
    }

    public Optional<UserResponse> fetchUser(Long id){
//        return userList.stream().filter(user -> user.getId().equals(id)).findFirst();
        return userRepository.findById(id).map(this::mapTOUserResponse);
    }

    public void createUser(UserRequest userRequest){
//        user.setId(nextId++);
//        userList.add(user);
        User user = new User();
        setUserDetails(user,userRequest);
        userRepository.save(user);
    }

    private void setUserDetails(User user, UserRequest userRequest) {
        user.setId(userRequest.getId());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNo(userRequest.getPhoneNo());
        if(userRequest.getAddress()!= null) {
            Address address = new Address();
            address.setStreet(userRequest.getAddress().getStreet());
            address.setCity(userRequest.getAddress().getCity());
            address.setState(userRequest.getAddress().getState());
            address.setCountry(userRequest.getAddress().getCountry());
            user.setAddress(address);
        }
    }

    public boolean updateUser(Long id, UserRequest updatedUserRequest){
//        return userList.stream()
//                .filter(user -> user.getId().equals(id))
//                .findFirst()
//                .map(existingUser -> {
//                    existingUser.setId(existingUser.getId());
//                    existingUser.setFirstName(updatedUser.getFirstName());
//                    existingUser.setLastName(updatedUser.getLastName());
//                    return true;
//                }).orElse(false);
        return userRepository.findById(id).map(existingUser -> {
            setUserDetails(existingUser, updatedUserRequest);
            userRepository.save(existingUser);
            return true;
        }).orElse(false);
    }

    private UserResponse mapTOUserResponse(User user){
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhoneNo(user.getPhoneNo());
        response.setRole(user.getRole());
        if(user.getAddress()!= null){
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setZipcode(user.getAddress().getZipcode());
            response.setAddress(addressDTO);
        }
        return response;
    }

}
