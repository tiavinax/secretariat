package com.ecole.service.Secretaire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecole.repository.Secretaire.UserRepository;
import com.ecole.entity.Secretaire.User;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User getUser(String email) {
        return userRepository.findByEmail(email);
    }    

    public void save(User user){
        userRepository.save(user);
    }

}
