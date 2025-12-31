package com.lcwd.user.service.services.impl;

import com.lcwd.user.service.entites.Hotel;
import com.lcwd.user.service.entites.Rating;
import com.lcwd.user.service.entites.User;
import com.lcwd.user.service.exception.ResourceNotFoundException;
import com.lcwd.user.service.repository.UserRepository;
import com.lcwd.user.service.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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


        Rating[] ratingOfUser = restTemplate.getForObject("http://RARINGSERVUICE/ratings/users/"+user.getUserId(), Rating[].class);
        logger.info("{} ",ratingOfUser);  // we find only ratings
        List<Rating> ratings = Arrays.stream(ratingOfUser).toList();

        //we find rating list in that contain ratings and hotels both
        List<Rating> ratingList = ratings.stream().map(rating -> {
            // api call to hotel service to get the hotel

          //  http://localhost:8082/hotels/d2c65e6d-d038-40f9-95c9-a120884da164
            ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTELSERVICE/hotels/"+rating.getHotelId(), Hotel.class);
            Hotel hotel = forEntity.getBody();
            logger.info("response status code: {} ", forEntity.getStatusCode());

            // set the hotel to rating
            rating.setHotel(hotel);

            // return the rating
            return rating;
        }).collect(Collectors.toList());


        user.setRatings(ratingList); //

        return user;

    }
}
