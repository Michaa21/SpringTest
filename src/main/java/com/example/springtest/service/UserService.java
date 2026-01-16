package com.example.springtest.service;

import com.example.springtest.model.Profile;
import com.example.springtest.model.User;
import com.example.springtest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User createUser(User user) {
        if (user.getProfile() != null) {
            user.getProfile().setUser(user);
        }
        return userRepository.save(user);
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с id " + id +
                        " не найден"));
    }

    @Transactional
    public User updateUser(UUID id, User userDetails) {

        User user = getUserById(id);

        user.setUsername(userDetails.getUsername());

        if (userDetails.getProfile() != null) {
            if (user.getProfile() == null) {
                Profile newProfile = new Profile();
                newProfile.setUser(user);
                user.setProfile(newProfile);
            }
            user.getProfile().setFirstName(userDetails.getProfile().getFirstName());
            user.getProfile().setLastName(userDetails.getProfile().getLastName());
        }
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(UUID id){
        User user = getUserById(id);
        userRepository.delete(user);
    }
}
