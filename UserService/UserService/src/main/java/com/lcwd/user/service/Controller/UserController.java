package com.lcwd.user.service.Controller;

import com.lcwd.user.service.entites.User;
import com.lcwd.user.service.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    // create
   @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User user1 = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }
    // single user get
    @GetMapping("/{userId}")
    @CircuitBreaker(name="ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId){
        User user = userService.getUser(userId);
         return ResponseEntity.ok(user);
   }

   // creating fallback method for circuitbreaker

    public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex){
        log.info("fallback is executed because service is down:", ex.getMessage());
        User user = User.builder()
                .email("dummy@gmail.com")
                .name("dummy")
                .userId("12345")
                .about("This user created dummy beacuse some service is down")
                .build();


        return new ResponseEntity<>(user, HttpStatus.OK);
    }

   @GetMapping
     public ResponseEntity<List<User>> getAllUser(){
         List<User> allUser = userService.getAllUser();
         return ResponseEntity.ok(allUser);
     }



    // all user get

}
