package com.ecom.app;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.fetchAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id){
        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody User user){
        userService.createUser(user);
        return ResponseEntity.ok("User added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUserDetails(@PathVariable Long id, @RequestBody User updatedUser){
        boolean userUpdated =  userService.updateUser(id, updatedUser);
        if(userUpdated)
           return ResponseEntity.ok("User updated successfully");
        return ResponseEntity.notFound().build();
    }
}
