package com.lcwd.user.service.services;

import com.lcwd.user.service.entites.User;
import com.lcwd.user.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface UserService {


    //create

    User saveUser(User user);


    // get all user
    List<User> getAllUser();

    // get single user of given userId

    User getUser(String userId);
}
