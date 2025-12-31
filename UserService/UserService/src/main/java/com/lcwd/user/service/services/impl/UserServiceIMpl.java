package com.lcwd.user.service.services.impl;

import com.lcwd.user.service.entites.Rating;
import com.lcwd.user.service.entites.User;
import com.lcwd.user.service.exception.ResourceNotFoundException;
import com.lcwd.user.service.repository.UserRepository;
import com.lcwd.user.service.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceIMpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;


    private Logger logger = LoggerFactory.getLogger(UserServiceIMpl.class);

    @Override
    public User saveUser(User user) {
        String randamUserId = UUID.randomUUID().toString(); //  it will generate a new ID every time
        user.setUserId(randamUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String userId) {

        // get User from database with the help of user repository
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with given id is not found on server !! " + userId));

        // fetch rating of the above user from Rating Service
        //http://localhost:8083/ratings/users/userId


        ArrayList<Rating> ratingOfUser = restTemplate.getForObject("http://localhost:8083/ratings/users/"+user.getUserId(), ArrayList.class);
        logger.info("{} ",ratingOfUser);
        user.setRatings(ratingOfUser);

        return user;

    }
}
