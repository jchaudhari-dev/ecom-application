package com.ecom.app;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final List<User> userList = new ArrayList<>();

    private Long nextId=1L;

    public List<User> fetchAllUsers(){
        return userList;
    }

    public Optional<User> fetchUser(Long id){
        return userList.stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    public void createUser(User user){
        user.setId(nextId++);
        userList.add(user);
    }

    public boolean updateUser(Long id, User updatedUser){
        return userList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .map(existingUser -> {
                    existingUser.setId(existingUser.getId());
                    existingUser.setFirstName(updatedUser.getFirstName());
                    existingUser.setLastName(updatedUser.getLastName());
                    return true;
                }).orElse(false);
    }

}
