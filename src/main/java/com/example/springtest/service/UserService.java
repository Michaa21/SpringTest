package com.example.springtest.service;

import com.example.springtest.model.Profile;
import com.example.springtest.model.User;
import com.example.springtest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user) {
        if (user.getProfile() != null) {
            user.getProfile().setUser(user);
        }
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с id " + id +
                        " не найден"));
    }

    public User updateUser(UUID id, User userDetails) {

        User user = getUserById(id);

        if (userDetails.getUsername() != null) {
            user.setUsername(userDetails.getUsername());
        }

        if (userDetails.getProfile() != null) {
            Profile profile = user.getProfile();
            if (profile == null) {
                profile = new Profile();
                profile.setUser(user);
                user.setProfile(profile);
            }
            profile.setFirstName(userDetails.getProfile().getFirstName());
            profile.setLastName(userDetails.getProfile().getLastName());
        }
        return user;
    }

    public void deleteUser(UUID id) {
        userRepository.delete(getUserById(id));
    }
}
